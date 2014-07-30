package collector.domain;

import org.jongo.MongoCollection;
import org.springframework.stereotype.Component;

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

    private int port = 27017;
    private String host = "localhost";
    private String db = "savant";
    private String col = "graphData";
    private DbConfig dbConfig = new DbConfig();

        public List<TimesliceObject> getData(long startTimeInput, long endTimeInput, String sizeInput, String[] clientInput, String[] profileInput) throws UnknownHostException {
            List<TimesliceObject> compiledData = new ArrayList<TimesliceObject>();
            MongoCollection dataCollection = dbConfig.useJongo(host, port, db, col);
            Map<String, String> allPossibleStatuses = fetchStatuses(dataCollection);
            for(int i = 0; i < clientInput.length; i ++){
                for(int j = 0; j< profileInput.length; j++){
                    for (String status : allPossibleStatuses.values()) {
                        if(status != null) {
                            Iterable<TimesliceObject> jongoData = dataCollection.find("{slice: {$gte: #, $lt: #}, group:{status: #, client: #, deliveryProfile: #}, size: #}"
                                    , startTimeInput, endTimeInput, status, clientInput[i], profileInput[j], sizeInput).as(TimesliceObject.class);
                            for (TimesliceObject outputData : jongoData) {
                                compiledData.add(outputData);
                            }
                        }
                    }
                }
            }
            return compiledData;
            /*Map<String, String> myClients = fetchClients(dataCol);
            for (String client: myClients.values()){
                System.out.println(client);
            }
            Map<String, String> myProfiles = fetchProfiles(dataCol);
            for (String profile: myProfiles.values()){
                System.out.println(profile);
            }*/
        }

    Map<String, String> fetchStatuses(MongoCollection dataCol) {
        Iterable<TimesliceObject> statusData = dataCol.find("{},{'group.status': #}", 1).as(TimesliceObject.class);
        Map<String, String> compiledStatuses = new HashMap<String, String>();
        for (TimesliceObject data : statusData) {
            Map<String, String> group = data.getGroup();
            String status = group.get("status");
            compiledStatuses.put(status, status);
        }
        return compiledStatuses;
    }

    /*Map<String,String> fetchProfiles(MongoCollection dataCol) {
        Iterable<TimesliceObject> allProfiles = dataCol.find("{},{'group.deliveryProfile': #}", 1).as(TimesliceObject.class);
        Map<String, String> profiles = new HashMap<String, String>();
        for (TimesliceObject ts : allProfiles) {
            Map<String, String> group = ts.getGroup();
            String status = group.get("deliveryProfile");
            profiles.put(status, status);
        }
        return profiles;
    }
    Map<String, String> fetchClients(MongoCollection dataCol) {
        Iterable<TimesliceObject> allClients = dataCol.find("{},{'group.client': #}", 1).as(TimesliceObject.class);
        Map<String, String> clients = new HashMap<String, String>();
        for (TimesliceObject ts : allClients) {
            Map<String, String> group = ts.getGroup();
            String client = group.get("client");
            clients.put(client, client);
        }
        return clients;
    }*/


}
