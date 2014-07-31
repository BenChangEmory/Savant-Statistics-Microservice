package collector.domain;

import org.jongo.MongoCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import spring.CollectorConfig;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by benjamin on 7/21/14.
 */

@Component
public class CompiledData {

    @Autowired
    CollectorConfig collectorConfig;

    int port;
    String host;
    String db;
    String col;
    DbConfig dbConfig = new DbConfig();
    MongoCollection dataCollection;

    public void connectDb() throws UnknownHostException {
        setPort(collectorConfig.getMongoDestination().getPort());
        setHost(collectorConfig.getMongoDestination().getHost());
        setDb(collectorConfig.getMongoDestination().getDatabase());
        setCol("graphData");
        setDataCollection(dbConfig.useJongo(host, port, db, col));
    }

    public List<TimesliceObject> getDataGroupedByStatus(long lowerTimeInput, long upperTimeInput, String sizeInput) throws UnknownHostException {
        connectDb();
        List<TimesliceObject> compiledData = new ArrayList<TimesliceObject>();
        Map<String, String> allPossibleStatuses = getAllExistingStatusesInMongo(dataCollection);
        for (String status : allPossibleStatuses.values()) {
            if(status != null) {
                Iterable<TimesliceObject> jongoData = dataCollection.find("{slice: {$gte: #, $lt: #}, group.status: #, size: #}"
                        , lowerTimeInput, upperTimeInput, status, sizeInput).as(TimesliceObject.class);
                for (TimesliceObject outputData : jongoData) {
                    compiledData.add(outputData);
                }
            }
        }
        return compiledData;
    }
    public List<TimesliceObject> getDataGroupedByClient(long lowerTimeInput, long upperTimeInput, String sizeInput, String[] clientInput) throws UnknownHostException {
        connectDb();
        List<TimesliceObject> compiledData = new ArrayList<TimesliceObject>();
        for(int i = 0; i < clientInput.length; i ++){

            Iterable<TimesliceObject> jongoData = dataCollection.find("{slice: {$gte: #, $lt: #}, group.client: #, size: #}"
                    , lowerTimeInput, upperTimeInput, clientInput[i], sizeInput).as(TimesliceObject.class);
            for (TimesliceObject outputData : jongoData) {
                compiledData.add(outputData);
            }
        }
        return compiledData;
    }
    public List<TimesliceObject> getDataGroupedByDeliveryProfile(long lowerTimeInput, long upperTimeInput, String sizeInput, String[] profileInput) throws UnknownHostException {
        connectDb();
        List<TimesliceObject> compiledData = new ArrayList<TimesliceObject>();
        for(int i = 0; i < profileInput.length; i ++){
            Iterable<TimesliceObject> jongoData = dataCollection.find("{slice: {$gte: #, $lt: #}, group.deliveryProfile: #, size: #}"
                    , lowerTimeInput, upperTimeInput, profileInput[i], sizeInput).as(TimesliceObject.class);
            for (TimesliceObject outputData : jongoData) {
                compiledData.add(outputData);
            }
        }
        return compiledData;
    }

    public List<TimesliceObject> getDataGroupedByStatusClient(long lowerTimeInput, long upperTimeInput, String sizeInput, String[] clientInput) throws UnknownHostException {
        connectDb();
        List<TimesliceObject> compiledData = new ArrayList<TimesliceObject>();
        Map<String, String> allPossibleStatuses = getAllExistingStatusesInMongo(dataCollection);
        for(int i = 0; i < clientInput.length; i ++){
                for (String status : allPossibleStatuses.values()) {
                    if(status != null) {
                        Iterable<TimesliceObject> jongoData = dataCollection.find("{slice: {$gte: #, $lt: #}, group:{status: #, client: #}, size: #}"
                                , lowerTimeInput, upperTimeInput, status, clientInput[i], sizeInput).as(TimesliceObject.class);
                        for (TimesliceObject outputData : jongoData) {
                            compiledData.add(outputData);
                        }

                }
            }
        }
        return compiledData;
    }
    public List<TimesliceObject> getDataGroupedByStatusDeliveryProfile(long lowerTimeInput, long upperTimeInput, String sizeInput, String[] profileInput) throws UnknownHostException {
        connectDb();
        List<TimesliceObject> compiledData = new ArrayList<TimesliceObject>();
        Map<String, String> allPossibleStatuses = getAllExistingStatusesInMongo(dataCollection);
            for(int j = 0; j< profileInput.length; j++){
                for (String status : allPossibleStatuses.values()) {
                    if(status != null) {
                        Iterable<TimesliceObject> jongoData = dataCollection.find("{slice: {$gte: #, $lt: #}, group:{status: #, deliveryProfile: #}, size: #}"
                                , lowerTimeInput, upperTimeInput, status, profileInput[j], sizeInput).as(TimesliceObject.class);
                        for (TimesliceObject outputData : jongoData) {
                            compiledData.add(outputData);
                        }
                    }

            }
        }
        return compiledData;
    }
    public List<TimesliceObject> getDataGroupedByClientDeliveryProfile(long lowerTimeInput, long upperTimeInput, String sizeInput, String[] clientInput, String[] profileInput) throws UnknownHostException {
        connectDb();
        List<TimesliceObject> compiledData = new ArrayList<TimesliceObject>();
        for(int i = 0; i < clientInput.length; i ++){
            for(int j = 0; j< profileInput.length; j++){

                Iterable<TimesliceObject> jongoData = dataCollection.find("{slice: {$gte: #, $lt: #}, group:{client: #, deliveryProfile: #}, size: #}"
                        , lowerTimeInput, upperTimeInput, clientInput[i], profileInput[j], sizeInput).as(TimesliceObject.class);
                for (TimesliceObject outputData : jongoData) {
                    compiledData.add(outputData);
                }

                }

        }
        return compiledData;
    }






    public List<TimesliceObject> getDataGroupedByStatusClientDeliveryProfile(long lowerTimeInput, long upperTimeInput, String sizeInput, String[] clientInput, String[] profileInput) throws UnknownHostException {
        connectDb();
        List<TimesliceObject> compiledData = new ArrayList<TimesliceObject>();
        Map<String, String> allPossibleStatuses = getAllExistingStatusesInMongo(dataCollection);
        for(int i = 0; i < clientInput.length; i ++){
            for(int j = 0; j< profileInput.length; j++){
                for (String status : allPossibleStatuses.values()) {
                    if(status != null) {
                        Iterable<TimesliceObject> jongoData = dataCollection.find("{slice: {$gte: #, $lt: #}, group:{status: #, client: #, deliveryProfile: #}, size: #}"
                                , lowerTimeInput, upperTimeInput, status, clientInput[i], profileInput[j], sizeInput).as(TimesliceObject.class);
                        for (TimesliceObject outputData : jongoData) {
                            compiledData.add(outputData);
                        }
                    }
                }
            }
        }
        return compiledData;
    }








    Map<String, String> getAllExistingStatusesInMongo(MongoCollection dataCol) {
        Iterable<TimesliceObject> statusData = dataCol.find("{},{'group.status': #}", 1).as(TimesliceObject.class);
        Map<String, String> compiledStatuses = new HashMap<String, String>();
        for (TimesliceObject data : statusData) {
            Map<String, String> group = data.getGroup();
            String status = group.get("status");
            compiledStatuses.put(status, status);
        }
        return compiledStatuses;
    }

    Map<String,String> getAllExistingProfilesInMongo(MongoCollection dataCol) {
        Iterable<TimesliceObject> allProfiles = dataCol.find("{},{'group.deliveryProfile': #}", 1).as(TimesliceObject.class);
        Map<String, String> profiles = new HashMap<String, String>();
        for (TimesliceObject ts : allProfiles) {
            Map<String, String> group = ts.getGroup();
            String status = group.get("deliveryProfile");
            profiles.put(status, status);
        }
        return profiles;
    }
    Map<String, String> getAllExistingClientsInMongo(MongoCollection dataCol) {
        Iterable<TimesliceObject> allClients = dataCol.find("{},{'group.client': #}", 1).as(TimesliceObject.class);
        Map<String, String> clients = new HashMap<String, String>();
        for (TimesliceObject ts : allClients) {
            Map<String, String> group = ts.getGroup();
            String client = group.get("client");
            clients.put(client, client);
        }
        return clients;
    }



    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getDb() {
        return db;
    }

    public void setDb(String db) {
        this.db = db;
    }

    public String getCol() {
        return col;
    }

    public void setCol(String col) {
        this.col = col;
    }

    public DbConfig getDbConfig() {
        return dbConfig;
    }

    public void setDbConfig(DbConfig dbConfig) {
        this.dbConfig = dbConfig;
    }

    public MongoCollection getDataCollection() {
        return dataCollection;
    }

    public void setDataCollection(MongoCollection dataCollection) {
        this.dataCollection = dataCollection;
    }

}
