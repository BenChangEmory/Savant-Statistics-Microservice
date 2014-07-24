package collector.resources;

import collector.service.OplogDataCollector;
import com.wordnik.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * Created by benjamin on 7/10/14.
 */

@Component
@Path("/request")
@Api(value = "/request", description = "Requesting count for db")
public class RequestResource {


    @Autowired
    OplogDataCollector oplogDataCollectorService;

    @POST
    @Path(value = "/db/")
    @Consumes(MediaType.TEXT_PLAIN)
    public void processRequest(String dbName) throws Exception{
        String fixed = dbName.substring(7);
        oplogDataCollectorService.collectData(fixed);
    }

}
