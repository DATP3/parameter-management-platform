package dk.nykredit.pmp.tracker;

import java.util.ArrayList;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("services")
public class ServiceResource {

	private Tracker tracker;

	public ServiceResource() {
		tracker = Tracker.getTracker();
	}

	@Context
	HttpHeaders getHeaders;

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public Response services() {

		// Check for required headers
		// Check authorization header
		if (getHeaders == null ||
				getHeaders.getRequestHeader("Authorization") == null ||
				getHeaders.getRequestHeader("Authorization").get(0) == null ||
				getHeaders.getRequestHeader("Authorization").get(0).isBlank()) {

			return Response.status(Response.Status.UNAUTHORIZED).build();
		}

		// Check environment header
		if (getHeaders.getRequestHeader("pmp-environment") == null ||
				getHeaders.getRequestHeader("pmp-environment").get(0) == null ||
				getHeaders.getRequestHeader("pmp-environment").get(0).isBlank()) {

			return Response.status(Response.Status.BAD_REQUEST).build();
		}

		// Get services in environment from tracker if any exist.
		ArrayList<Service> services = tracker.getServices(getHeaders.getRequestHeader("pmp-environment").get(0));

		// If no services exist in the environment, return 404.
		if (services == null) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}

		// Return services in environment.
		return Response.ok(new ServiceResponse(services, getHeaders.getRequestHeader("pmp-environment").get(0)))
				.build();
	}

	@Context
	HttpHeaders postHeaders;

	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	public Response services(Service service) {

		// Check environment header and service object.
		if (postHeaders == null ||
				postHeaders.getRequestHeader("pmp-environment") == null ||
				postHeaders.getRequestHeader("pmp-environment").get(0) == null ||
				postHeaders.getRequestHeader("pmp-environment").get(0).isBlank() ||
				!serviceIsValid(service)) {

			return Response.status(Response.Status.BAD_REQUEST).build();
		}

		String environment = postHeaders.getRequestHeader("pmp-environment").get(0);

		// Attempt to retrieve pre-existing service in environment.
		Service existingService = tracker.getServiceFromAddress(service.getPmpRoot(), environment);

		// If service already exitsts, refresh its stale-timer.
		if (existingService != null) {
			existingService.refresh();
			return Response.ok().build();
		}

		// If service didn't exist, register it.
		tracker.registerService(service, environment);

		// respond with statuscode 200.
		return Response.ok().build();
	}

	/**
	 * Validates the presence of required atributes in a Service object.
	 * 
	 * @param service the object to be validated.
	 * @return true if all required fields were validated, otherwise false.
	 */
	private boolean serviceIsValid(Service service) {

		if (service == null ||
				service.getPmpRoot() == null ||
				service.getPmpRoot().isBlank() ||
				service.getName() == null ||
				service.getName().isBlank()) {
			return false;
		}

		return true;
	}
}