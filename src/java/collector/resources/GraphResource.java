package collector.resources;

import collector.domain.CompiledData;
import collector.domain.GraphRequest;
import collector.domain.TimesliceObject;
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
public class GraphResource {

    @Autowired
    CompiledData compiledData;


    @POST
    @Produces("application/json")
    @Consumes("application/json")
    public List<TimesliceObject> getDataGroupedByStatusClientDeliveryProfile(GraphRequest input) throws Exception{
        return compiledData.getDataGroupedByStatusClientDeliveryProfile(input.getLowerTime(), input.getUpperTime(), input.getSize(), input.getClients(), input.getProfiles());
    }

    @POST
    @Path(value = "/client_deliveryprofile/")
    @Produces("application/json")
    @Consumes("application/json")
    public List<TimesliceObject> getDataGroupedByClientDeliveryProfile(GraphRequest input) throws Exception{
        return compiledData.getDataGroupedByClientDeliveryProfile(input.getLowerTime(), input.getUpperTime(), input.getSize(), input.getClients(), input.getProfiles());
    }

    @POST
    @Path(value = "/status_deliveryprofile/")
    @Produces("application/json")
    @Consumes("application/json")
    public List<TimesliceObject> getDataGroupedByStatusDeliveryProfile(GraphRequest input) throws Exception{
        return compiledData.getDataGroupedByStatusDeliveryProfile(input.getLowerTime(), input.getUpperTime(), input.getSize(), input.getProfiles());
    }

    @POST
    @Path(value = "/status_client/")
    @Produces("application/json")
    @Consumes("application/json")
    public List<TimesliceObject> getDataGroupedByStatusClient(GraphRequest input) throws Exception{
        return compiledData.getDataGroupedByStatusClient(input.getLowerTime(), input.getUpperTime(), input.getSize(), input.getClients());
    }

    @POST
    @Path(value = "/status/")
    @Produces("application/json")
    @Consumes("application/json")
    public List<TimesliceObject> getDataGroupedByStatus(GraphRequest input) throws Exception{
        return compiledData.getDataGroupedByStatus(input.getLowerTime(), input.getUpperTime(), input.getSize());
    }

    @POST
    @Path(value = "/client/")
    @Produces("application/json")
    @Consumes("application/json")
    public List<TimesliceObject> getDataGroupedByClient(GraphRequest input) throws Exception{
        return compiledData.getDataGroupedByClient(input.getLowerTime(), input.getUpperTime(), input.getSize(), input.getClients());
    }

    @POST
    @Path(value = "/deliveryprofile/")
    @Produces("application/json")
    @Consumes("application/json")
    public List<TimesliceObject> getDataGroupedByDeliveryProfile(GraphRequest input) throws Exception{
        return compiledData.getDataGroupedByDeliveryProfile(input.getLowerTime(), input.getUpperTime(), input.getSize(), input.getProfiles());
    }

}