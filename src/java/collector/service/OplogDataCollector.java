package collector.service;

import collector.domain.TimeEntry;
import com.mongodb.*;
import org.bson.types.BSONTimestamp;
import org.bson.types.ObjectId;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.springframework.beans.factory.InitializingBean;

import java.net.UnknownHostException;

/**
 * Created by benjamin on 6/30/14.
 */

public class OplogDataCollector implements InitializingBean {


    //configurable fields
    int destinationPort = 27017;
    int sourcePort = 27027;
    String destinationHost = "localhost";
    String sourceHost = "10.0.0.151";
    String destinationDb = "savant";
    String sourceDb = "local";
    String destinationOplogInfoCol = "storedOplogInformation";
    String destinationTimeSliceCol = "storedTimeSliceData";
    String sourceOplog = "oplog.rs";
    String tsInformation = "ts";
    //Field to monitor
    String monitoredField  = "deliveryProfileCode";

    public void collectData(String filterToDbCollection) throws Exception{
    //public static void main(String[] args) throws Exception{
        try {
            //Fields for time frame
            long timeStart = 0l;//1405522955290l; //sorted by createdDtm
            long timeEnd;

            MongoCollection oplogUpdates = useJongo(destinationHost, destinationPort, destinationDb, destinationOplogInfoCol);
            MongoCollection timeSliceData = useJongo(destinationHost, destinationPort, destinationDb, destinationTimeSliceCol);
            MongoCollection tsData = useJongo(destinationHost, destinationPort, destinationDb, tsInformation);
            DBCollection sourceCol = useDBCollection(sourceHost, sourcePort, sourceDb, sourceOplog);

            //filter and create cursor
            BasicDBObject query = new BasicDBObject();
            query.put("ns", filterToDbCollection);
            query.put("op", new BasicDBObject("$in", new String[] {"u","i"}));

            Iterable<TimeEntry> obj = tsData.find().limit(1).as(TimeEntry.class);
            ObjectId startId = null;
            BSONTimestamp startTs;
            if(obj.iterator().hasNext()) {
                TimeEntry entry = obj.iterator().next();
                startId = entry.getId();
                startTs = entry.getTs();
            }
            startTs = new BSONTimestamp(1405515243, 11);
            query.put("ts", new BasicDBObject("$gt", startTs));

            DBCursor cursor = sourceCol.find(query).addOption(Bytes.QUERYOPTION_TAILABLE).addOption(Bytes.QUERYOPTION_AWAITDATA).addOption(Bytes.QUERYOPTION_NOTIMEOUT);

            //todo take out all cursorCounts
            int cursorCount = 0;
            while(true){
                System.out.println("reading data");
                System.out.println(cursor.count());
                long avg = 0l;
                while (cursor.hasNext()) {
                    long before = System.currentTimeMillis();

                    timeEnd = System.currentTimeMillis();
                    cursorCount++;
                    System.out.println("updating... ");
                    System.out.println("action number: " + cursorCount);
                    System.out.println();

                    //store data with cursor
                    BasicDBObject doc = (BasicDBObject)cursor.next();
                    //Long statusCreatedDtm = (Long)((BasicDBObject) (((BasicDBObject) doc.get("o")).get("status"))).get("createdDtm");
                    ObjectId id = (ObjectId)((BasicDBObject) doc.get("o")).get("_id");
                    Long createdDtm = (Long)((BasicDBObject) doc.get("o")).get("createdDtm");
                    String newMonitoredField = (String)((BasicDBObject) doc.get("o")).get(monitoredField);
                    String clientId = (String) (((BasicDBObject) doc.get("o")).get("clientId"));
                    String deliveryProfileCode = (String) (((BasicDBObject) doc.get("o")).get("deliveryProfileCode"));
                    String statusCode = null;
                    if((((BasicDBObject) doc.get("o")).get("status"))!=null) {
                        statusCode = (String) ((BasicDBObject) (((BasicDBObject) doc.get("o")).get("status"))).get("code");
                    }


                    //update oplogUpdates
                    oplogUpdates.update("{'_id': #}", id).upsert()
                           .with("{$set: {createdDtm: #, monitoredField: #}}", createdDtm, newMonitoredField);
                    //update timesliceData
                    for(Timeslices slice: Timeslices.values()) {
                        long increments = slice.value;
                        for (long currentTimeSegment = timeStart; currentTimeSegment < timeEnd; currentTimeSegment += increments) {
                            if (createdDtm != null) {
                                if (createdDtm >= currentTimeSegment && createdDtm < currentTimeSegment + increments) {
                                    timeSliceData.update("{'slice': #,'group.status': #, 'group.client': #, 'group.deliveryProfile': #, 'size': #}",
                                            currentTimeSegment, statusCode, clientId, deliveryProfileCode, slice.name())
                                            .upsert().with("{$inc: {count: 1}}");
                                }
                            }
                        }
                    }
                    //create or update tsData
                    BSONTimestamp ts = (BSONTimestamp) doc.get("ts");
                    if(startId == null){
                        tsData.insert("{ts: #}", ts);
                        obj = tsData.find().limit(1).as(TimeEntry.class);
                        TimeEntry entry = obj.iterator().next();
                        startId = entry.getId();
                    }
                    else {
                        tsData.update("{_id: #}", startId).upsert().with("{$set: {ts: #}}", ts);
                    }
                    if(cursorCount == cursor.count()) {
                        System.out.println("ready to read new information");
                    }
                    long after = System.currentTimeMillis();
                    long x = (after-before)/1000;
                    System.out.println("processed in "+x+" seconds");
                    avg += x;
                    avg /= cursor.count();
                    System.out.println("average milliseconds "+avg);
                }
            }
        }  catch (MongoException e) {
            e.printStackTrace();
        }
    }

    private MongoCollection useJongo(String Host, int Port, String Db, String Col) throws UnknownHostException {
        MongoClient mongoClient = new MongoClient(Host, Port);
        DB db = mongoClient.getDB(Db);
        Jongo jongo = new Jongo(db);
        MongoCollection mongoCollection = jongo.getCollection(Col);
        return mongoCollection;
    }

    private DBCollection useDBCollection(String Host, int Port, String Db, String Col) throws UnknownHostException {
        MongoClient mongoClient = new MongoClient(Host, Port);
        DB db = mongoClient.getDB(Db);
        DBCollection dbCollection = db.getCollection(Col);
        return dbCollection;
    }

    @Override
    public void afterPropertiesSet() throws Exception{
    }

    public enum Timeslices {
        FIVE_MINUTES(300000l),
        TEN_MINUTES(600000l),
        THIRTY_MINUTES(1800000l),
        ONE_HOUR(3600000l),
        ONE_DAY(86400000l);

        private long value;
        private Timeslices(long value){
            this.value = value;
        }
    };

}