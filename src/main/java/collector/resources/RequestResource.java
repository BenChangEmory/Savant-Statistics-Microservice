package collector.resources;

import collector.domain.OplogDataCollector;
import com.wordnik.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import java.util.ArrayList;

/**
 * Created by benjamin on 7/10/14.
 */

@Component
@Path("/request")
@Api(value = "/request", description = "Requesting count for db")
public class RequestResource {

    @Autowired
    OplogDataCollector oplogDataCollectorService;
    ArrayList<String> databasesThatAreRunning = new ArrayList<String>();


    @GET
    @Path(value = "/view/")
    @Produces("application/json")
    public ArrayList<String> getRunningDbs() throws Exception{
        return databasesThatAreRunning;
    }

    @POST
    @Path(value = "/db/")
    public void runOplogDataCollector(String dbNameInput) throws Exception{
        boolean isRepeatedQuery = false;
        for(int i=0; i < databasesThatAreRunning.size(); i++) {
            if(databasesThatAreRunning.get(i).equals(dbNameInput)) {
                isRepeatedQuery = true;
            }
        }
        if(!isRepeatedQuery) {
            databasesThatAreRunning.add(dbNameInput);
            oplogDataCollectorService.collectData(dbNameInput);
        }
    }

}
