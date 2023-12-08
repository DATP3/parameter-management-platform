package dk.nykredit.pmp.tracker;

import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.GET;

@Path("environment")
public class EnvironmentResource {

    private Tracker tracker;

    public EnvironmentResource() {
        tracker = Tracker.getTracker();
    }

    @Context
    HttpHeaders headers;

    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    public Response environments() {

        if (headers == null ||
                headers.getRequestHeader("Authorization") == null ||
                headers.getRequestHeader("Authorization").get(0) == null ||
                headers.getRequestHeader("Authorization").get(0).isBlank()) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        EnvironmentResponse environmentResponse = new EnvironmentResponse(tracker.getEnvironmentNames());

        return Response.ok(environmentResponse).build();
    }

}
