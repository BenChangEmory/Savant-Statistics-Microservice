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
public class GraphResource {

    @Autowired
    CompiledData compiledData;

    @POST
    @Produces("application/json")
    public List<TimesliceObject> getData(String input) throws Exception{
        String[] arrayInput = input.split("/");
        String sizeInput = arrayInput[0];
        long startTimeInput = Long.parseLong(arrayInput[1]);
        long endTimeInput = Long.parseLong(arrayInput[2]);
        String[] clientInput = arrayInput[3].split(",");
        String[] profileInput = arrayInput[4].split(",");
        return compiledData.getData(startTimeInput, endTimeInput, sizeInput, clientInput, profileInput);
    }
}