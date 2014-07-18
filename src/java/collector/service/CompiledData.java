package collector.service;

import collector.domain.StatusCodes;
import collector.domain.TimesliceObject;
import org.jongo.MongoCollection;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by benjamin on 7/17/14.
 */

@Component
public class CompiledData {
    int port = 27017;
    String host = "localhost";
    String db = "savant";
    String col = "storedTimeSliceData";
    DbConfig dbConfig = new DbConfig();

    public void getDataPhaseCount() throws Exception{

        MongoCollection dataCol = dbConfig.useJongo(host, port, db, col);
        for(StatusCodes codes: StatusCodes.values()) {
            int count = 0;
            Iterable<TimesliceObject> obj = dataCol.find("{'group.status': #, 'size': #}"
                    , codes.toString(), "ONE_DAY").as(TimesliceObject.class);
            ArrayList<String> deliveryProfiles= new ArrayList<String>();
            while (obj.iterator().hasNext()) {
                TimesliceObject timeslice = obj.iterator().next();
                Map<String,String> group = timeslice.getGroup();
                String deliveryProfile = group.get("deliveryProfile");
                if(deliveryProfile != null) {
                    deliveryProfiles.add(group.get("deliveryProfile"));
                }
                count += timeslice.getCount();
            }
            //part 1
            if (count != 0 && deliveryProfiles.size() != 0) {
                System.out.println();
                System.out.println(codes.toString() + "     " + "Profiles: " + deliveryProfiles.size() + "      Count: " + count);
            }
            //part 2
            int size = deliveryProfiles.size();
            for(int i = 0; i < size; i++){
                if(deliveryProfiles.get(i) != null){
                    int myCount = 1;
                    for(int j = i+1; j < deliveryProfiles.size(); j++){
                        if(deliveryProfiles.get(i).equals(deliveryProfiles.get(j))){
                            myCount++;
                            deliveryProfiles.remove(j);
                        }
                    }
                    size = deliveryProfiles.size();
                    System.out.println(deliveryProfiles.get(i) + "    dispatches: " + myCount);
                }
            }


        }
    }

    public void getDataDeliveryProfile() throws Exception{

        MongoCollection dataCol = dbConfig.useJongo(host, port, db, col);
        Map<String,String> dProfs= new HashMap<String,String>();
        Iterable<TimesliceObject> myObj = dataCol.find("{},{'group.deliveryProfile': #}", 1).as(TimesliceObject.class);

        for(TimesliceObject ts : myObj){
            Map<String,String> group = ts.getGroup();
            String deliveryProfile = group.get("deliveryProfile");
            dProfs.put(deliveryProfile,deliveryProfile);
        }

        for(String profile : dProfs.values()){
            if(profile != null){
                System.out.print(profile + " --");
                for(StatusCodes codes: StatusCodes.values()) {
                    int count = 0;
                    Iterable<TimesliceObject> obj = dataCol.find("{'group.deliveryProfile': #, 'group.status': #, 'size': #}"
                            , profile, codes.toString(), "ONE_DAY").as(TimesliceObject.class);
                    while (obj.iterator().hasNext()) {
                        TimesliceObject timeslice = obj.iterator().next();
                        count += timeslice.getCount();
                    }
                    if (count != 0) {
                        System.out.print("      " + codes.toString() + ": " + count);
                    }
                }
                System.out.println();
            }
        }
        //System.out.println("Delivery Profile    Quality Check Failed    Delivery Successful         Delivery Failed         Generation Rejected     Request Error");
    }

}
