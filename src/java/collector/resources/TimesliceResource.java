package collector.resources;

import collector.domain.PhaseCount;
import collector.domain.SortedByDeliveryProfile;
import collector.domain.TimesliceObject;
import collector.service.CompiledData;
import com.wordnik.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by benjamin on 7/10/14.
 */

@Component
@Path("/statistics")
@Produces(MediaType.TEXT_PLAIN)
@Api(value = "/statistics")
public class TimesliceResource {


    @Autowired
    CompiledData compiledData;

    @POST
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
    /*@POST
    @Path(value = "/db/")
    @Consumes(MediaType.TEXT_PLAIN)
    public void processRequest(String dbName) throws Exception{
        String fixed = dbName.substring(7);
        oplogDataCollectorService.collectData(fixed);
    }*/
    /*@GET
    @Path(value = "/client/")
    public void processByClient() throws Exception{
        compiledData.getDataPhaseCount();
    }*/
    /*@GET
    @Path(value = "/status/")
    public void processByStatus(String status) throws Exception{
        System.out.println(status);
        compiledData.getDataPhaseCount();
    }*/
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

}
