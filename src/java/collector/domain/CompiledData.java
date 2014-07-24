package collector.domain;

import collector.service.DbConfig;
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
        int port = 27017;
        String host = "localhost";
        String db = "savant";
        String col = "graphData";
        DbConfig dbConfig = new DbConfig();

        public List<TimesliceObject> getByProfileAndStatus(String size) throws UnknownHostException {
            List<TimesliceObject> list = new ArrayList<TimesliceObject>();
            MongoCollection dataCol = dbConfig.useJongo(host, port, db, col);
            Map<String, String> dProfiles = fetchProfiles(dataCol);
            Map<String, String> statuses = fetchStatuses(dataCol);
            for (String profile : dProfiles.values()) {
                for (String status : statuses.values()) {
                    Iterable<TimesliceObject> obj = dataCol.find("{group:{status: #, deliveryProfile: #}, 'size': #}"
                            , status, profile, size).as(TimesliceObject.class);
                    for (TimesliceObject allData : obj) {
                        list.add(allData);
                    }
                }
            }
            return list;
        }
        public List<TimesliceObject> getByProfileAndClient(String size) throws UnknownHostException {
            List<TimesliceObject> list = new ArrayList<TimesliceObject>();

            MongoCollection dataCol = dbConfig.useJongo(host, port, db, col);
            Map<String, String> dProfiles = fetchProfiles(dataCol);
            Map<String, String> clients = fetchClients(dataCol);
            for (String profile : dProfiles.values()) {
                for (String client : clients.values()) {
                    Iterable<TimesliceObject> obj = dataCol.find("{group:{client: #, deliveryProfile: #}, 'size': #}"
                            ,client, profile, size).as(TimesliceObject.class);
                    for (TimesliceObject allData : obj) {
                        list.add(allData);
                    }

                }
            }
            return list;
        }
        public List<TimesliceObject> getByStatusAndClient(String size) throws UnknownHostException {
            List<TimesliceObject> list = new ArrayList<TimesliceObject>();
            MongoCollection dataCol = dbConfig.useJongo(host, port, db, col);
            Map<String, String> statuses = fetchStatuses(dataCol);
            Map<String, String> clients = fetchClients(dataCol);
            for (String status : statuses.values()) {
                for (String client : clients.values()) {
                    Iterable<TimesliceObject> obj = dataCol.find("{group:{status: #, client: #}, 'size': #}"
                            ,status, client, size).as(TimesliceObject.class);
                    for (TimesliceObject allData : obj) {
                        list.add(allData);
                    }
                }

            }
            return list;
        }
        public List<TimesliceObject> getByStatus(String size) throws UnknownHostException {
            List<TimesliceObject> list = new ArrayList<TimesliceObject>();
            MongoCollection dataCol = dbConfig.useJongo(host, port, db, col);
            Map<String, String> statuses = fetchStatuses(dataCol);
            for (String status : statuses.values()) {
                Iterable<TimesliceObject> obj = dataCol.find("{group:{status: #}, 'size': #}"
                        ,status, size).as(TimesliceObject.class);
                for (TimesliceObject allData : obj) {
                    list.add(allData);

                }

            }
            return list;
        }
        public List<TimesliceObject> getByClient(String size) throws UnknownHostException {
            List<TimesliceObject> list = new ArrayList<TimesliceObject>();
            MongoCollection dataCol = dbConfig.useJongo(host, port, db, col);
            Map<String, String> clients = fetchClients(dataCol);
            for (String client : clients.values()) {
                Iterable<TimesliceObject> obj = dataCol.find("{group:{client: #}, 'size': #}"
                        ,client, size).as(TimesliceObject.class);
                for (TimesliceObject allData : obj) {
                    list.add(allData);
                }
            }
            return list;
        }
        public List<TimesliceObject> getByProfile(String size) throws UnknownHostException {
            List<TimesliceObject> list = new ArrayList<TimesliceObject>();
            MongoCollection dataCol = dbConfig.useJongo(host, port, db, col);
            Map<String, String> dProfiles = fetchProfiles(dataCol);
            for (String profile : dProfiles.values()) {
                Iterable<TimesliceObject> obj = dataCol.find("{group:{deliveryProfile: #}, 'size': #}"
                        ,profile, size).as(TimesliceObject.class);
                for (TimesliceObject allData : obj) {
                    list.add(allData);
                }
            }
            return list;
        }
        public List<TimesliceObject> getByAll(String size) throws UnknownHostException {
            List<TimesliceObject> list = new ArrayList<TimesliceObject>();

            MongoCollection dataCol = dbConfig.useJongo(host, port, db, col);
            Map<String, String> dProfiles = fetchProfiles(dataCol);
            Map<String, String> statuses = fetchStatuses(dataCol);
            Map<String, String> clients = fetchClients(dataCol);
            for (String profile : dProfiles.values()) {
                for (String status : statuses.values()) {
                    for (String client : clients.values()) {
                        Iterable<TimesliceObject> obj = dataCol.find("{group:{status: #, client: #, deliveryProfile: #}, 'size': #}"
                                ,status, client, profile, size).as(TimesliceObject.class);
                        for (TimesliceObject allData : obj) {
                            //TimesliceObject fixed = new TimesliceObject();
                            list.add(allData);
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
