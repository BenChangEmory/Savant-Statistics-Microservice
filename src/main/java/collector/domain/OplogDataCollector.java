package collector.domain;

import com.mongodb.*;
import org.bson.types.BSONTimestamp;
import org.bson.types.ObjectId;
import org.jongo.MongoCollection;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import spring.CollectorConfig;

/**
 * Created by benjamin on 6/30/14.
 */

public class OplogDataCollector implements InitializingBean {

    @Autowired
    CollectorConfig collectorConfig;

    public void collectData(String dbNameInput) throws Exception {
        try {
//            String dbNameInput = collectorConfig.getTargettedDb().getDatabase();
            DbConfig dbConfig = new DbConfig();
            //configurable fields
            int destinationPort = collectorConfig.getMongoDestination().getPort();
            int sourcePort = collectorConfig.getMongoSource().getPort();
            String destinationHost = collectorConfig.getMongoDestination().getHost();
            String sourceHost = collectorConfig.getMongoSource().getHost();
            String destinationDb = collectorConfig.getMongoDestination().getDatabase();
            String sourceDb = "local";
            String destinationGeneralDataCol = "generalData";
            String destinationGraphDataCol = "graphData";
            String destinationStatusCreatedDtmGraphDataCol = "statusCreatedGraphData";
            String sourceOplog = "oplog.rs";
            String tsCollection = "ts";
            String monitoredField = "deliveryProfileCode";

            MongoCollection generalData = dbConfig.useJongo(destinationHost, destinationPort, destinationDb, destinationGeneralDataCol);
            MongoCollection graphData = dbConfig.useJongo(destinationHost, destinationPort, destinationDb, destinationGraphDataCol);
            MongoCollection statusCreatedGraphData = dbConfig.useJongo(destinationHost, destinationPort, destinationDb, destinationStatusCreatedDtmGraphDataCol);
            MongoCollection tsData = dbConfig.useJongo(destinationHost, destinationPort, destinationDb, tsCollection);
            DBCollection sourceCol = dbConfig.useDBCollection(sourceHost, sourcePort, sourceDb, sourceOplog);

            //filter and create cursor
            BasicDBObject tailableCursorQuery = new BasicDBObject();
            tailableCursorQuery.put("ns", dbNameInput);
            tailableCursorQuery.put("op", new BasicDBObject("$in", new String[]{"u", "i"}));

            TimeEntry timeEntry = tsData.findOne().as(TimeEntry.class);
            BSONTimestamp initialStoredTimestamp;
            if (timeEntry != null) {
                initialStoredTimestamp = timeEntry.getTs();
            } else {
                initialStoredTimestamp = new BSONTimestamp(0, 0);
            }

            tailableCursorQuery.put("ts", new BasicDBObject("$gt", initialStoredTimestamp));
            //note query status so that it is not null


            DBCursor tailableCursor = sourceCol.find(tailableCursorQuery)
                    .addOption(Bytes.QUERYOPTION_TAILABLE)
                    .addOption(Bytes.QUERYOPTION_AWAITDATA)
                    .addOption(Bytes.QUERYOPTION_NOTIMEOUT)
                    .addOption(Bytes.QUERYOPTION_OPLOGREPLAY);

            //NOTE tailableCursor.count() method takes about 2 seconds to process
            while (true) {

                while (tailableCursor.hasNext()) {
                    //store data with cursor
                    BasicDBObject oplogDocument = (BasicDBObject) tailableCursor.next();
                    ObjectId objectId = (ObjectId) ((BasicDBObject) oplogDocument.get("o")).get("_id");
                    Long objectCreatedDtm = (Long) ((BasicDBObject) oplogDocument.get("o")).get("createdDtm");
                    String objectMonitoredField = (String) ((BasicDBObject) oplogDocument.get("o")).get(monitoredField);
                    String objectClientId = (String) (((BasicDBObject) oplogDocument.get("o")).get("clientId"));
                    String objectdeliveryProfileCode = (String) (((BasicDBObject) oplogDocument.get("o")).get("deliveryProfileCode"));
                    String objectStatusCode = null;
                    Long objectStatusCreatedDtm = null;
                    if ((((BasicDBObject) oplogDocument.get("o")).get("status")) != null) {
                        objectStatusCode = (String) ((BasicDBObject) (((BasicDBObject) oplogDocument.get("o")).get("status"))).get("code");
                        objectStatusCreatedDtm = (Long) ((BasicDBObject) (((BasicDBObject) oplogDocument.get("o")).get("status"))).get("createdDtm");
                    }

                    //update generalData
                    generalData.update("{'_id': #}", objectId).upsert()
                            .with("{$set: {createdDtm: #, " + monitoredField + ": #}}", objectCreatedDtm, objectMonitoredField);
                    //update graphData
                    long currentTime = System.currentTimeMillis();
                    for (Timeslices slice : Timeslices.values()) {
                        long sizeIncrements = slice.value;
                        for (long currentTimeSegment = 0l; currentTimeSegment < currentTime; currentTimeSegment += sizeIncrements) {
                            if (objectCreatedDtm != null) {
                                //TODO: get rid of these loops and put in hashing algorithm to bucket the time slice
                                if (objectCreatedDtm >= currentTimeSegment && objectCreatedDtm < currentTimeSegment + sizeIncrements) {
                                    graphData.update("{'slice': #,'group': {status: #, 'client': #, 'deliveryProfile': #}, 'size': #}",
                                            currentTimeSegment, objectStatusCode, objectClientId, objectdeliveryProfileCode, slice.name())
                                            .upsert().with("{$inc: {count: 1}}");
                                    graphData.update("{'slice': #,'group': {status: #, 'client': #}, 'size': #}",
                                            currentTimeSegment, objectStatusCode, objectClientId, slice.name())
                                            .upsert().with("{$inc: {count: 1}}");
                                    graphData.update("{'slice': #,'group': {status: #, 'deliveryProfile': #}, 'size': #}",
                                            currentTimeSegment, objectStatusCode, objectdeliveryProfileCode, slice.name())
                                            .upsert().with("{$inc: {count: 1}}");
                                    graphData.update("{'slice': #,'group': {'client': #, 'deliveryProfile': #}, 'size': #}",
                                            currentTimeSegment, objectClientId, objectdeliveryProfileCode, slice.name())
                                            .upsert().with("{$inc: {count: 1}}");
                                    graphData.update("{'slice': #,'group': {'client': #}, 'size': #}",
                                            currentTimeSegment, objectClientId, slice.name())
                                            .upsert().with("{$inc: {count: 1}}");
                                    graphData.update("{'slice': #,'group': {'deliveryProfile': #}, 'size': #}",
                                            currentTimeSegment, objectdeliveryProfileCode, slice.name())
                                            .upsert().with("{$inc: {count: 1}}");
                                    graphData.update("{'slice': #,'group': {status: #}, 'size': #}",
                                            currentTimeSegment, objectStatusCode, slice.name())
                                            .upsert().with("{$inc: {count: 1}}");
                                }
                            }
                        }
                        for (long currentTimeSegment = 0l; currentTimeSegment < currentTime; currentTimeSegment += sizeIncrements) {
                            if (objectStatusCreatedDtm != null) {
                                if (objectStatusCreatedDtm >= currentTimeSegment && objectStatusCreatedDtm < currentTimeSegment + sizeIncrements) {
                                    statusCreatedGraphData.update("{'slice': #,'group': {status: #, 'client': #, 'deliveryProfile': #}, 'size': #}",
                                            currentTimeSegment, objectStatusCode, objectClientId, objectdeliveryProfileCode, slice.name())
                                            .upsert().with("{$inc: {count: 1}}");
                                    statusCreatedGraphData.update("{'slice': #,'group': {status: #, 'client': #}, 'size': #}",
                                            currentTimeSegment, objectStatusCode, objectClientId, slice.name())
                                            .upsert().with("{$inc: {count: 1}}");
                                    statusCreatedGraphData.update("{'slice': #,'group': {status: #, 'deliveryProfile': #}, 'size': #}",
                                            currentTimeSegment, objectStatusCode, objectdeliveryProfileCode, slice.name())
                                            .upsert().with("{$inc: {count: 1}}");
                                    statusCreatedGraphData.update("{'slice': #,'group': {'client': #, 'deliveryProfile': #}, 'size': #}",
                                            currentTimeSegment, objectClientId, objectdeliveryProfileCode, slice.name())
                                            .upsert().with("{$inc: {count: 1}}");
                                    statusCreatedGraphData.update("{'slice': #,'group': {'client': #}, 'size': #}",
                                            currentTimeSegment, objectClientId, slice.name())
                                            .upsert().with("{$inc: {count: 1}}");
                                    statusCreatedGraphData.update("{'slice': #,'group': {'deliveryProfile': #}, 'size': #}",
                                            currentTimeSegment, objectdeliveryProfileCode, slice.name())
                                            .upsert().with("{$inc: {count: 1}}");
                                    statusCreatedGraphData.update("{'slice': #,'group': {status: #}, 'size': #}",
                                            currentTimeSegment, objectStatusCode, slice.name())
                                            .upsert().with("{$inc: {count: 1}}");
                                }
                            }
                        }
                    }
                    //create or update tsData
                    BSONTimestamp ts = (BSONTimestamp) oplogDocument.get("ts");
                    tsData.update("{_id: #}", "lastEntryProcessedTimestamp").upsert().with("{$set: {ts: #}}", ts);
                }
            }
        } catch (MongoException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void afterPropertiesSet() throws Exception {
    }


}