/*
package collector.service;

import collector.domain.*;
import org.jongo.MongoCollection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

*/
/**
 * Created by benjamin on 7/21/14.
 *//*

public class Examples {



    /*@POST
    @Produces("application/json")
    @Path(value = "/types/")
    @Consumes(MediaType.TEXT_PLAIN)
    public List<TimesliceObject> processRequest(String size) throws Exception{
        String fixed = size.substring(5);
        if(fixed.isEmpty()){
            fixed = "ONE_DAY";
        }
        return compiledData.getType(fixed);
    }

    @GET
    @Produces("application/json")
    @Path(value = "/types/")
    public List<TimesliceObject> processRequest() throws Exception{
        String defaultSize = "ONE_DAY";
        return compiledData.getType(defaultSize);
    }

    *//*@POST
    @Path(value = "/db/")
    @Consumes(MediaType.TEXT_PLAIN)
    public void processRequest(String dbName) throws Exception{
        String fixed = dbName.substring(7);
        oplogDataCollectorService.collectData(fixed);
    }*//*
    *//*@GET
    @Path(value = "/client/")
    public void processByClient() throws Exception{
        compiledData.getDataPhaseCount();
    }*//*
    *//*@GET
    @Path(value = "/status/")
    public void processByStatus(String status) throws Exception{
        System.out.println(status);
        compiledData.getDataPhaseCount();
    }*//*
    @GET
    @Produces("application/json")
    @Path(value = "/statuses/{statusCode}")
    public List<PhaseCount> processByStatus(@PathParam("statusCode") String status) throws Exception{
        return compiledData.getDataPhaseCount(status);
    }
    @GET
    @Produces("application/json")
    @Path(value = "/statuses/")
    public List<PhaseCount> processByStatus() throws Exception{
        return compiledData.getDataPhaseCount();
    }
    @GET
    @Produces("application/json")
    @Path(value = "/delivery_profiles/")
    public List<SortedByDeliveryProfile> processByDeliveryProfile() throws Exception{
        return compiledData.getDataDeliveryProfile();
    }
    @GET
     @Produces("application/json")
     @Path(value = "/delivery_profiles/{dprof}")
     public List<SortedByDeliveryProfile> processByDeliveryProfile(@PathParam("dprof") String dprof) throws Exception{
        return compiledData.getDataDeliveryProfile(dprof);
    }


    public List<PhaseCount> getDataPhaseCount() throws Exception {
        List<PhaseCount> list = new ArrayList<PhaseCount>();
        MongoCollection dataCol = dbConfig.useJongo(host, port, db, col);
        for (StatusCodes codes : StatusCodes.values()) {
            int count = 0;
            Iterable<TimesliceObject> obj = dataCol.find("{'group.status': #, 'size': #}"
                    , codes.toString(), "ONE_DAY").as(TimesliceObject.class);
            ArrayList<String> deliveryProfiles = new ArrayList<String>();
            while (obj.iterator().hasNext()) {
                TimesliceObject timeslice = obj.iterator().next();
                Map<String, String> group = timeslice.getGroup();
                String deliveryProfile = group.get("deliveryProfile");
                if (deliveryProfile != null) {
                    deliveryProfiles.add(group.get("deliveryProfile"));
                }
                count += timeslice.getCount();
            }
            List<ProfilesAndDispatches> x = new ArrayList<ProfilesAndDispatches>();
            int size = deliveryProfiles.size();
            for (int i = 0; i < size; i++) {
                if (deliveryProfiles.get(i) != null) {
                    int myCount = 1;
                    for (int j = i + 1; j < deliveryProfiles.size(); j++) {
                        if (deliveryProfiles.get(i).equals(deliveryProfiles.get(j))) {
                            myCount++;
                            deliveryProfiles.remove(j);
                        }
                    }
                    size = deliveryProfiles.size();
                    x.add(new ProfilesAndDispatches(deliveryProfiles.get(i), myCount));
                }
            }
            if (count != 0 && deliveryProfiles.size() != 0) {
                PhaseCount object = new PhaseCount(codes.toString(), deliveryProfiles.size(), count, x);
                list.add(object);
            }
        }
        return list;
    }

    public List<PhaseCount> getDataPhaseCount(String status) throws Exception {
        List<PhaseCount> list = new ArrayList<PhaseCount>();
        MongoCollection dataCol = dbConfig.useJongo(host, port, db, col);
        int count = 0;
        Iterable<TimesliceObject> obj = dataCol.find("{'group.status': #, 'size': #}"
                , status, "ONE_DAY").as(TimesliceObject.class);
        ArrayList<String> deliveryProfiles = new ArrayList<String>();
        while (obj.iterator().hasNext()) {
            TimesliceObject timeslice = obj.iterator().next();
            Map<String, String> group = timeslice.getGroup();
            String deliveryProfile = group.get("deliveryProfile");
            if (deliveryProfile != null) {
                deliveryProfiles.add(group.get("deliveryProfile"));
            }
            count += timeslice.getCount();
        }
        List<ProfilesAndDispatches> x = new ArrayList<ProfilesAndDispatches>();
        int size = deliveryProfiles.size();
        for (int i = 0; i < size; i++) {
            if (deliveryProfiles.get(i) != null) {
                int myCount = 1;
                for (int j = i + 1; j < deliveryProfiles.size(); j++) {
                    if (deliveryProfiles.get(i).equals(deliveryProfiles.get(j))) {
                        myCount++;
                        deliveryProfiles.remove(j);
                    }
                }
                size = deliveryProfiles.size();
                x.add(new ProfilesAndDispatches(deliveryProfiles.get(i), myCount));
            }
        }
        if (count != 0 && deliveryProfiles.size() != 0) {
            PhaseCount object = new PhaseCount(status, deliveryProfiles.size(), count, x);
            list.add(object);
        }

        return list;
    }

    public List<SortedByDeliveryProfile> getDataDeliveryProfile() throws Exception {
        List<SortedByDeliveryProfile> list = new ArrayList<SortedByDeliveryProfile>();
        MongoCollection dataCol = dbConfig.useJongo(host, port, db, col);
        Map<String, String> dProfs = new HashMap<String, String>();
        Iterable<TimesliceObject> timesliceObjects = dataCol.find("{},{'group.deliveryProfile': #}", 1).as(TimesliceObject.class);

        for (TimesliceObject ts : timesliceObjects) {
            Map<String, String> group = ts.getGroup();
            String deliveryProfile = group.get("deliveryProfile");
            dProfs.put(deliveryProfile, deliveryProfile);
        }
        int[] ints = new int[StatusCodes.size];
        for (String profile : dProfs.values()) {
            if (profile != null) {
                int numCodes = 0;
                for (StatusCodes codes : StatusCodes.values()) {
                    int count = 0;
                    Iterable<TimesliceObject> obj = dataCol.find("{'group.deliveryProfile': #, 'group.status': #, 'size': #}"
                            , profile, codes.toString(), "ONE_DAY").as(TimesliceObject.class);
                    while (obj.iterator().hasNext()) {
                        TimesliceObject timeslice = obj.iterator().next();
                        count += timeslice.getCount();
                    }
                    ints[numCodes] = count;
                    numCodes++;
                }
                SortedByDeliveryProfile data = new SortedByDeliveryProfile(profile, ints);
                list.add(data);

            }
        }
        return list;
    }

    public List<SortedByDeliveryProfile> getDataDeliveryProfile(String dProf) throws Exception {
        List<SortedByDeliveryProfile> list = new ArrayList<SortedByDeliveryProfile>();
        MongoCollection dataCol = dbConfig.useJongo(host, port, db, col);
        Iterable<TimesliceObject> timesliceObjects = dataCol.find("{},{'group.deliveryProfile': #}", 1).as(TimesliceObject.class);

        Map<String, String> dProfs = new HashMap<String, String>();
        for (TimesliceObject ts : timesliceObjects) {
            Map<String, String> group = ts.getGroup();
            String deliveryProfile = group.get("deliveryProfile");
            dProfs.put(deliveryProfile, deliveryProfile);
        }
        int[] ints = new int[StatusCodes.size];
        int numCodes = 0;
        for (StatusCodes codes : StatusCodes.values()) {
            int count = 0;
            Iterable<TimesliceObject> obj = dataCol.find("{'group': {'deliveryProfile': #, 'status': #}, 'size': #}"
                    , dProf, codes.toString(), "ONE_DAY").as(TimesliceObject.class);
            while (obj.iterator().hasNext()) {
                TimesliceObject timeslice = obj.iterator().next();
                count += timeslice.getCount();
            }
            ints[numCodes] = count;
            numCodes++;
        }
        SortedByDeliveryProfile data = new SortedByDeliveryProfile(dProf, ints);
        list.add(data);
        return list;
    }

    public List<TimesliceObject> getType(String size) throws Exception {
        List<TimesliceObject> list = new ArrayList<TimesliceObject>();
        MongoCollection dataCol = dbConfig.useJongo(host, port, db, col);
        Iterable<TimesliceObject> timesliceObjectIterable = dataCol.find("{'size': #}", size).as(TimesliceObject.class);
        for (TimesliceObject allData : timesliceObjectIterable) {
            list.add(allData);
        }
        return list;
    }
}
*/
