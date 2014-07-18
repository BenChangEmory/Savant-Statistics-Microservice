package collector.service;

import collector.domain.TimeEntry;
import collector.domain.Timeslices;
import com.mongodb.*;
import org.bson.types.BSONTimestamp;
import org.bson.types.ObjectId;
import org.jongo.MongoCollection;
import org.springframework.beans.factory.InitializingBean;

/**
 * Created by benjamin on 6/30/14.
 */

public class OplogDataCollector implements InitializingBean {


    DbConfig dbConfig = new DbConfig();

    //configurable fields
    int destinationPort = 27017;
    int sourcePort = 27027;
    String destinationHost = "localhost";
    String sourceHost = "10.0.0.151";
    String destinationDb = "savant";
    String sourceDb = "local";
    String destinationOplogInfoCol = "generalInfo";
    String destinationTimeSliceCol = "graphData";
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

            MongoCollection generalInfo = dbConfig.useJongo(destinationHost, destinationPort, destinationDb, destinationOplogInfoCol);
            MongoCollection graphData = dbConfig.useJongo(destinationHost, destinationPort, destinationDb, destinationTimeSliceCol);
            MongoCollection tsData = dbConfig.useJongo(destinationHost, destinationPort, destinationDb, tsInformation);
            DBCollection sourceCol = dbConfig.useDBCollection(sourceHost, sourcePort, sourceDb, sourceOplog);

            //filter and create cursor
            BasicDBObject query = new BasicDBObject();
            query.put("ns", filterToDbCollection);
            query.put("op", new BasicDBObject("$in", new String[] {"u","i"}));

            Iterable<TimeEntry> obj = tsData.find().limit(1).as(TimeEntry.class);
            ObjectId startId = null;
            BSONTimestamp startTs = null;
            if(obj.iterator().hasNext()) {
                TimeEntry entry = obj.iterator().next();
                startId = entry.getId();
                startTs = entry.getTs();
            }
            //startTs = new BSONTimestamp(1405515243, 11);
            query.put("ts", new BasicDBObject("$gte", startTs));

            DBCursor cursor = sourceCol.find(query).addOption(Bytes.QUERYOPTION_TAILABLE).addOption(Bytes.QUERYOPTION_AWAITDATA).addOption(Bytes.QUERYOPTION_NOTIMEOUT);

            //todo take out all cursorCounts
            int cursorCount = 0;
            while(true){
                System.out.println("reading data");
                System.out.println(cursor.count() + " initial objects to read");
                System.out.println("----------------------------------");
                System.out.println();
                long sum = 0l;
                long after = 0l;
                long before = 0l;
                while (cursor.hasNext()) {
                    cursorCount++;
                    if(cursorCount != 1){

                        after = System.currentTimeMillis();
                        long x = (after-before);
                        System.out.println("processed in "+x+" milliseconds");
                        sum += x;
                        long avg = sum/(cursorCount-1);
                        System.out.println("average milliseconds "+avg);
                        if(cursorCount%20 == 0){
                            long timeLeft = (cursor.count() - cursorCount);
                            timeLeft *= avg;
                            timeLeft /= 1000;
                            timeLeft /= 60;
                            System.out.println("time remaining " + timeLeft + " minutes");
                        }
                    }

                    before = System.currentTimeMillis();

                    timeEnd = System.currentTimeMillis();
                    System.out.println("updating... ");
                    System.out.println("action number: " + cursorCount);

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
                    generalInfo.update("{'_id': #}", id).upsert()
                            .with("{$set: {createdDtm: #, monitoredField: #}}", createdDtm, newMonitoredField);
                    //update timesliceData
                    for(Timeslices slice: Timeslices.values()) {
                        long increments = slice.value;
                        for (long currentTimeSegment = timeStart; currentTimeSegment < timeEnd; currentTimeSegment += increments) {
                            if (createdDtm != null) {
                                if (createdDtm >= currentTimeSegment && createdDtm < currentTimeSegment + increments) {
                                    graphData.update("{'slice': #,'group': {status': #, 'client': #, 'deliveryProfile': #}, 'size': #}",
                                            currentTimeSegment, statusCode, clientId, deliveryProfileCode, slice.name())
                                            .multi().upsert().with("{$inc: {count: 1}}");
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
                    System.out.println();
                    System.out.println();


                }
            }
        }  catch (MongoException e) {
            e.printStackTrace();
        }
    }



    @Override
    public void afterPropertiesSet() throws Exception{
    }


}