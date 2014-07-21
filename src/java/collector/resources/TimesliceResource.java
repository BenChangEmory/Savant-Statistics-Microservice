package collector.resources;

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
    String defaultSize = "ONE_DAY";

    @GET
    @Produces("application/json")
    @Path(value = "/by_all/")
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
    @Path(value = "/by_everything/")
    public List<TimesliceObject> processEverything() throws Exception{
        return compiledData.getByEverything(defaultSize);
    }

}
