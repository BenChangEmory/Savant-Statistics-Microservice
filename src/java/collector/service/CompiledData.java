package collector.service;

import collector.domain.*;
import org.bson.types.ObjectId;
import org.jongo.MongoCollection;
import org.springframework.stereotype.Component;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by benjamin on 7/17/14.
 */

@Component
public class CompiledData {
    int port = 27017;
    String host = "localhost";
    String db = "savant";
    String col = "graphData";
    DbConfig dbConfig = new DbConfig();


    public List<TimesliceObject> getByEverything(String size) throws Exception {
        List<TimesliceObject> list = new ArrayList<TimesliceObject>();
        MongoCollection dataCol = dbConfig.useJongo(host, port, db, col);
        Iterable<TimesliceObject> timesliceObjectIterable = dataCol.find("{'size': #}", size).as(TimesliceObject.class);
        for (TimesliceObject allData : timesliceObjectIterable) {
            list.add(allData);
        }
        return list;
    }

    public List<TimesliceObject> getByProfileAndStatus(String size) throws UnknownHostException {
        List<TimesliceObject> list = new ArrayList<TimesliceObject>();
        MongoCollection dataCol = dbConfig.useJongo(host, port, db, col);
        Map<String, String> dProfiles = fetchProfiles(dataCol);
        Map<String, String> statuses = fetchStatuses(dataCol);
        for (String profile : dProfiles.values()) {
            for (String status : statuses.values()) {
                System.out.println("sorting through profile: " + profile + "    status:" + status);
                Iterable<TimesliceObject> obj = dataCol.find("{'group.status': #, 'group.deliveryProfile': #, 'size': #}"
                        , status, profile, size).as(TimesliceObject.class);
                int count = 0;
                long slice = 0l;
                ObjectId id = null;
                for (TimesliceObject allData : obj) {
                    slice = allData.getSlice();
                    id = allData.getId();
                    list.add(allData);
                    System.out.println(allData.getCount());
                    count += allData.getCount();
                }
                /*Map<String, String> group = new HashMap<String,String>();
                group.put("status", status);
                group.put("profile", profile);
                list.add(new TimesliceObject(id, count, group, size, slice));*/
            }
        }
        return list;
    }

    public List<TimesliceObject> getByAll(String size) throws UnknownHostException {
        List<TimesliceObject> list = new ArrayList<TimesliceObject>();

        MongoCollection dataCol = dbConfig.useJongo(host, port, db, col);
        System.out.println("stop 0");
        Map<String, String> dProfiles = fetchProfiles(dataCol);
        System.out.println("stop 1");
        Map<String, String> statuses = fetchStatuses(dataCol);
        System.out.println("stop 2");
        Map<String, String> clients = fetchClients(dataCol);
        System.out.println("stop 3");
        for (String profile : dProfiles.values()) {
            for (String status : statuses.values()) {
                for (String client : clients.values()) {
                    System.out.println("sorting through profile: " + profile + "    status:"+ status + "    client:" + client);

                    Iterable<TimesliceObject> obj = dataCol.find("{group.status: #, group.client: #, group.deliveryProfile: #, 'size': #}"
                            ,status, client, profile, size).as(TimesliceObject.class);
                    /*Iterable<TimesliceObject> obj = dataCol.find("{'group': {'status': #, 'client': #, 'deliveryProfile': #}, 'size': #}"
                            ,status, client, profile, size).as(TimesliceObject.class);*/
                    int count = 0;
                    for (TimesliceObject allData : obj) {
                        list.add(allData);
                        System.out.println(allData.getCount());
                        count += allData.getCount();
                    }
                }
            }
        }
        return list;
    }

    private Map<String, String> fetchClients(MongoCollection dataCol) {
        Iterable<TimesliceObject> allClients = dataCol.find("{},{'group.client': #}", 1).as(TimesliceObject.class);
        Map<String, String> clients = new HashMap<String, String>();
        for (TimesliceObject ts : allClients) {
            Map<String, String> group = ts.getGroup();
            String client = group.get("client");
            clients.put(client, client);
        }
        return clients;
    }

    Map<String, String> fetchStatuses(MongoCollection dataCol) {
        Iterable<TimesliceObject> allStatuses = dataCol.find("{},{'group.status': #}", 1).as(TimesliceObject.class);
        Map<String, String> statuses = new HashMap<String, String>();
        for (TimesliceObject ts : allStatuses) {
            Map<String, String> group = ts.getGroup();
            String status = group.get("status");
            statuses.put(status, status);
        }
        return statuses;
    }

    Map<String, String> fetchProfiles(MongoCollection dataCol) {
        Iterable<TimesliceObject> allProfiles = dataCol.find("{},{'group.deliveryProfile': #}", 1).as(TimesliceObject.class);
        Map<String, String> dProfiles = new HashMap<String, String>();
        for (TimesliceObject ts : allProfiles) {
            Map<String, String> group = ts.getGroup();
            String deliveryProfile = group.get("deliveryProfile");
            dProfiles.put(deliveryProfile, deliveryProfile);
        }
        return dProfiles;
    }
}