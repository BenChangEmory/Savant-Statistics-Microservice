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
    public List<TimesliceObject> getDataGroupedByStatusClientDeliveryProfile(String input) throws Exception{
        String[] arrayInput = input.split("/");
        String sizeInput = arrayInput[0];
        long lowerTimeInput = Long.parseLong(arrayInput[1]);
        long upperTimeInput = Long.parseLong(arrayInput[2]);
        String[] clientInput = arrayInput[3].split(",");
        String[] profileInput = arrayInput[4].split(",");
        return compiledData.getDataGroupedByStatusClientDeliveryProfile(lowerTimeInput, upperTimeInput, sizeInput, clientInput, profileInput);
    }


    @POST
    @Path("/client_deliveryprofile")
    @Produces("application/json")
    public List<TimesliceObject> getDataGroupedByClientDeliveryProfile(String input) throws Exception{
        String[] arrayInput = input.split("/");
        String sizeInput = arrayInput[0];
        long lowerTimeInput = Long.parseLong(arrayInput[1]);
        long upperTimeInput = Long.parseLong(arrayInput[2]);
        String[] clientInput = arrayInput[3].split(",");
        String[] profileInput = arrayInput[4].split(",");
        return compiledData.getDataGroupedByClientDeliveryProfile(lowerTimeInput, upperTimeInput, sizeInput, clientInput, profileInput);
    }

    @POST
    @Path("/status_deliveryprofile")
    @Produces("application/json")
    public List<TimesliceObject> getDataGroupedByStatusDeliveryProfile(String input) throws Exception{
        String[] arrayInput = input.split("/");
        String sizeInput = arrayInput[0];
        long lowerTimeInput = Long.parseLong(arrayInput[1]);
        long upperTimeInput = Long.parseLong(arrayInput[2]);
        String[] profileInput = arrayInput[3].split(",");
        return compiledData.getDataGroupedByStatusDeliveryProfile(lowerTimeInput, upperTimeInput, sizeInput, profileInput);
    }

    @POST
    @Path("/status_client")
    @Produces("application/json")
    public List<TimesliceObject> getDataGroupedByStatusClient(String input) throws Exception{
        String[] arrayInput = input.split("/");
        String sizeInput = arrayInput[0];
        long lowerTimeInput = Long.parseLong(arrayInput[1]);
        long upperTimeInput = Long.parseLong(arrayInput[2]);
        String[] clientInput = arrayInput[3].split(",");
        return compiledData.getDataGroupedByStatusClient(lowerTimeInput, upperTimeInput, sizeInput, clientInput);
    }

    @POST
    @Path("/status")
    @Produces("application/json")
    public List<TimesliceObject> getDataGroupedByStatus(String input) throws Exception{
        String[] arrayInput = input.split("/");
        String sizeInput = arrayInput[0];
        long lowerTimeInput = Long.parseLong(arrayInput[1]);
        long upperTimeInput = Long.parseLong(arrayInput[2]);
        return compiledData.getDataGroupedByStatus(lowerTimeInput, upperTimeInput, sizeInput);
    }

    @POST
    @Path("/client")
    @Produces("application/json")
    public List<TimesliceObject> getDataGroupedByClient(String input) throws Exception{
        String[] arrayInput = input.split("/");
        String sizeInput = arrayInput[0];
        long lowerTimeInput = Long.parseLong(arrayInput[1]);
        long upperTimeInput = Long.parseLong(arrayInput[2]);
        String[] clientInput = arrayInput[3].split(",");
        return compiledData.getDataGroupedByClient(lowerTimeInput, upperTimeInput, sizeInput, clientInput);
    }

    @POST
    @Path("/deliveryprofile")
    @Produces("application/json")
    public List<TimesliceObject> getDataGroupedByDeliveryProfile(String input) throws Exception{
        String[] arrayInput = input.split("/");
        String sizeInput = arrayInput[0];
        long lowerTimeInput = Long.parseLong(arrayInput[1]);
        long upperTimeInput = Long.parseLong(arrayInput[2]);
        String[] profileInput = arrayInput[3].split(",");
        return compiledData.getDataGroupedByDeliveryProfile(lowerTimeInput, upperTimeInput, sizeInput, profileInput);
    }

}