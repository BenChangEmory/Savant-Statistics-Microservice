package collector.domain;

import com.mongodb.*;
import org.bson.types.BSONTimestamp;
import org.bson.types.ObjectId;
import org.jongo.MongoCollection;
import org.springframework.beans.factory.InitializingBean;

/**
 * Created by benjamin on 6/30/14.
 */

public class OplogDataCollector implements InitializingBean {
    public void collectData(String dbNameInput) throws Exception{
        try {
            DbConfig dbConfig = new DbConfig();
            //configurable fields
            int destinationPort = 27017;
            int sourcePort = 27027;
            String destinationHost = "localhost";
            String sourceHost = "10.0.0.151";
            String destinationDb = "savant";
            String sourceDb = "local";
            String destinationOplogInfoCol = "generalData";
            String destinationTimeSliceCol = "graphData";
            String sourceOplog = "oplog.rs";
            String tsInformation = "ts";
            String monitoredField  = "deliveryProfileCode";

            MongoCollection generalData = dbConfig.useJongo(destinationHost, destinationPort, destinationDb, destinationOplogInfoCol);
            MongoCollection graphData = dbConfig.useJongo(destinationHost, destinationPort, destinationDb, destinationTimeSliceCol);
            MongoCollection tsData = dbConfig.useJongo(destinationHost, destinationPort, destinationDb, tsInformation);
            DBCollection sourceCol = dbConfig.useDBCollection(sourceHost, sourcePort, sourceDb, sourceOplog);

            //filter and create cursor
            BasicDBObject tailableCursorQuery = new BasicDBObject();
            tailableCursorQuery.put("ns", dbNameInput);
            tailableCursorQuery.put("op", new BasicDBObject("$in", new String[]{"u", "i"}));

            Iterable<TimeEntry> iterableForTimeStart = tsData.find().limit(1).as(TimeEntry.class);
            BSONTimestamp initialStoredTimestamp = new BSONTimestamp(0, 0);
            if(iterableForTimeStart.iterator().hasNext()) {
                TimeEntry entry = iterableForTimeStart.iterator().next();
                initialStoredTimestamp = entry.getTs();
            }
//            startTs = new BSONTimestamp(1406552482, 11);
            tailableCursorQuery.put("ts", new BasicDBObject("$gt", initialStoredTimestamp));

            DBCursor tailableCursor = sourceCol.find(tailableCursorQuery).addOption(Bytes.QUERYOPTION_TAILABLE).addOption(Bytes.QUERYOPTION_AWAITDATA)
                    .addOption(Bytes.QUERYOPTION_NOTIMEOUT).addOption(Bytes.QUERYOPTION_OPLOGREPLAY);

            //NOTE tailableCursor.count() takes about 2 seconds to process
            int processedCount = 0;
            while(true){

                long after;
                long before = 0l;

                while (tailableCursor.hasNext()) {

                    processedCount++;
                    if(processedCount != 1){
                        after = System.currentTimeMillis();
                        long processingTime = (after-before);
                        System.out.println("processed in "+processingTime+" milliseconds");
                    }
                    before = System.currentTimeMillis();

                    //store data with cursor
                    BasicDBObject document = (BasicDBObject)tailableCursor.next();
                    //Long statusCreatedDtm = (Long)((BasicDBObject) (((BasicDBObject) document.get("o")).get("status"))).get("createdDtm");
                    ObjectId objectId = (ObjectId)((BasicDBObject) document.get("o")).get("_id");
                    Long createdDtm = (Long)((BasicDBObject) document.get("o")).get("createdDtm");
                    String designatedMonitoredField = (String)((BasicDBObject) document.get("o")).get(monitoredField);
                    String clientId = (String) (((BasicDBObject) document.get("o")).get("clientId"));
                    String deliveryProfileCode = (String) (((BasicDBObject) document.get("o")).get("deliveryProfileCode"));
                    String statusCode = null;
                    if((((BasicDBObject) document.get("o")).get("status"))!=null) {
                        statusCode = (String) ((BasicDBObject) (((BasicDBObject) document.get("o")).get("status"))).get("code");
                    }

                    //update generalData
                    generalData.update("{'_id': #}", objectId).upsert()
                            .with("{$set: {createdDtm: #, "+ monitoredField +": #}}", createdDtm, designatedMonitoredField);
                    //update graphData
                    long currentTime = System.currentTimeMillis();
                    for(Timeslices slice: Timeslices.values()) {
                        long sizeIncrements = slice.value;
                        for (long currentTimeSegment = 0l; currentTimeSegment < currentTime; currentTimeSegment += sizeIncrements) {
                            if (createdDtm != null) {
                                if (createdDtm >= currentTimeSegment && createdDtm < currentTimeSegment + sizeIncrements) {
                                    graphData.update("{'slice': #,'group': {status: #, 'client': #, 'deliveryProfile': #}, 'size': #}",
                                            currentTimeSegment, statusCode, clientId, deliveryProfileCode, slice.name())
                                            .upsert().with("{$inc: {count: 1}}");
                                    graphData.update("{'slice': #,'group': {status: #, 'client': #}, 'size': #}",
                                            currentTimeSegment, statusCode, clientId,  slice.name())
                                            .upsert().with("{$inc: {count: 1}}");
                                    graphData.update("{'slice': #,'group': {status: #, 'deliveryProfile': #}, 'size': #}",
                                            currentTimeSegment, statusCode, deliveryProfileCode, slice.name())
                                            .upsert().with("{$inc: {count: 1}}");
                                    graphData.update("{'slice': #,'group': {'client': #, 'deliveryProfile': #}, 'size': #}",
                                            currentTimeSegment, clientId, deliveryProfileCode, slice.name())
                                            .upsert().with("{$inc: {count: 1}}");
                                    graphData.update("{'slice': #,'group': {'client': #}, 'size': #}",
                                            currentTimeSegment, clientId, slice.name())
                                            .upsert().with("{$inc: {count: 1}}");
                                    graphData.update("{'slice': #,'group': {'deliveryProfile': #}, 'size': #}",
                                            currentTimeSegment, deliveryProfileCode, slice.name())
                                            .upsert().with("{$inc: {count: 1}}");
                                    graphData.update("{'slice': #,'group': {status: #}, 'size': #}",
                                            currentTimeSegment, statusCode, slice.name())
                                            .upsert().with("{$inc: {count: 1}}");
                                }
                            }
                        }
                    }
                    //create or update tsData
                    BSONTimestamp ts = (BSONTimestamp) document.get("ts");
                    tsData.update("{_id: #}", null).upsert().with("{$set: {ts: #}}", ts);

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