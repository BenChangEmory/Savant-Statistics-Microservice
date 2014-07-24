package collector.resources;

import collector.domain.CompiledData;
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
public class TimesliceResource {

    @Autowired
    CompiledData compiledData;
    String defaultSize = "ONE_HOUR";

    @GET
    @Produces("application/json")
    public List<TimesliceObject> processAll() throws Exception{
        return compiledData.getByAll(defaultSize);
    }
    @GET
    @Produces("application/json")
    @Path(value = "/by_profiles_and_statuses/")
    public List<TimesliceObject> processProfilesStatuses() throws Exception{
        return compiledData.getByProfileAndStatus(defaultSize);
    }
    @GET
    @Produces("application/json")
    @Path(value = "/by_clients_and_statuses/")
    public List<TimesliceObject> processClientsStatuses() throws Exception{
        return compiledData.getByStatusAndClient(defaultSize);
    }
    @GET
    @Produces("application/json")
    @Path(value = "/by_profiles_and_clients/")
    public List<TimesliceObject> processProfilesClients() throws Exception{
        return compiledData.getByProfileAndClient(defaultSize);
    }
    @GET
    @Produces("application/json")
    @Path(value = "/by_clients/")
    public List<TimesliceObject> processClients() throws Exception{
        return compiledData.getByClient(defaultSize);
    }
    @GET
    @Produces("application/json")
    @Path(value = "/by_statuses/")
    public List<TimesliceObject> processStatuses() throws Exception{
        return compiledData.getByStatus(defaultSize);
    }
    @GET
    @Produces("application/json")
    @Path(value = "/by_profiles/")
    public List<TimesliceObject> processProfiles() throws Exception{
        return compiledData.getByProfile(defaultSize);
    }
}
