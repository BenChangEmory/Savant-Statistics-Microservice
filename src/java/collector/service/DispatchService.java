/*
package collector.service;

import collector.domain.Dispatch;
import collector.domain.DispatchStatus;
import collector.repository.DispatchRepository;
import org.joda.time.DateTime;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Scanner;

*/
/**
 * Created by benjamin on 6/11/14.
 *//*


public class DispatchService implements InitializingBean{


    @Autowired
    private DispatchRepository dispatchRepository;

    public void testDispatchTimeDifference(){

        for(int i = 0; i < 5; i++) {
            System.out.println("HEYYY LOOOOK HERE =======>>  Enter dispatch id");

            Scanner scanner = new Scanner(System.in);

            String input = scanner.next();

            long difference = getTimeDifference(input);

            System.out.println("HEYYY LOOOOK HERE =======>>" + difference);
        }
    }



    public long getTimeDifference(String id) {

        Dispatch dispatch = dispatchRepository.getDispatchById(id);

        List<DispatchStatus> statusHistory = dispatch.getStatusHistory();

        int histSize = statusHistory.size();

        DispatchStatus newStat = statusHistory.get(histSize - 1);

        DispatchStatus beforeStat = statusHistory.get(histSize -2);

        long timeDiff = getTimeDifference(newStat.getTime(), beforeStat.getTime());

        return timeDiff;
    }

    private long getTimeDifference(DateTime newTime, DateTime lastTime) {

        long newTimeMil = newTime.getMillis();
        long oldTimeMil = lastTime.getMillis();

        long difference = newTimeMil - oldTimeMil;

        return difference;

    }


    @Override
    public void afterPropertiesSet() throws Exception{
        Assert.notNull(dispatchRepository, "Dispatch repository should be specified");
    }

}
*/
