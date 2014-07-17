package collector.service;

import collector.domain.StatusCodes;
import collector.domain.TimesliceObject;
import org.jongo.MongoCollection;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by benjamin on 7/17/14.
 */

public class DataStatusByPhaseCount {

    public static void main(String[] args) throws Exception{
        int port = 27017;
        String host = "localhost";
        String db = "savant";
        String col = "storedTimeSliceData";
        DbConfig dbConfig = new DbConfig();

        MongoCollection dataCol = dbConfig.useJongo(host, port, db, col);
        Iterable<TimesliceObject> myObj = dataCol.find("{},{'group.deliveryProfile': #}", 1).as(TimesliceObject.class);

        for(StatusCodes codes: StatusCodes.values()) {
            int count = 0;
            Iterable<TimesliceObject> obj = dataCol.find("{'group.status': #, 'size': #}"
                    , codes.toString(), "ONE_DAY").as(TimesliceObject.class);
            Map<String,String> dProfs= new HashMap<String,String>();
            while (obj.iterator().hasNext()) {
                TimesliceObject timeslice = obj.iterator().next();
                Map<String,String> group = timeslice.getGroup();
                String deliveryProfile = group.get("deliveryProfile");
                if(deliveryProfile != null) {
                    dProfs.put(deliveryProfile, deliveryProfile);
                }
                count += timeslice.getCount();
            }
            if (count != 0 && dProfs.size() != 0) {
                System.out.println();
                System.out.println(codes.toString() + "     " + "Profiles: " + dProfs.size() + "      Count: " + count);
            }
            for(String profile : dProfs.values()) {
                if(profile != null) {
                    System.out.println(profile);
                }
            }
        }
    }

}
