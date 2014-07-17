package collector.service;

import collector.domain.StatusCodes;
import collector.domain.TimesliceObject;
import org.jongo.MongoCollection;
import java.util.*;

/**
 * Created by benjamin on 7/17/14.
 */
public class DataStatusByDeliveryProfile {

    public static void main(String[] args) throws Exception{
        int port = 27017;
        String host = "localhost";
        String db = "savant";
        String col = "storedTimeSliceData";
        DbConfig dbConfig = new DbConfig();

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
