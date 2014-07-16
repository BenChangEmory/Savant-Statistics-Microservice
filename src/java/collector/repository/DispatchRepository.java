/*
package collector.repository;

import collector.domain.Dispatch;
import collector.domain.DispatchStatus;
import org.joda.time.DateTime;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

*/
/**
 * Created by benjamin on 6/11/14.
 *//*

@Service
public class DispatchRepository implements InitializingBean {

    @Autowired
    public Jongo jongo;

    private MongoCollection dispatchCollection;


    @Override
    public void afterPropertiesSet() throws Exception{
        dispatchCollection = jongo.getCollection("bensDispatches");
    }

    public Dispatch getDispatchById(String id){
        Dispatch dispatch = dispatchCollection.findOne("{id: #}", id).as(Dispatch.class);

        return dispatch;
    }


}*/
