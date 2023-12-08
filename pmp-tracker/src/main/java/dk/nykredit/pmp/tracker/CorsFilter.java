package dk.nykredit.pmp.tracker;

import java.io.IOException;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;

@Provider
public class CorsFilter implements ContainerResponseFilter {

    @Override
    public void filter(final ContainerRequestContext requestContext,
            final ContainerResponseContext cres) throws IOException {
        String origin = requestContext.getHeaderString("Origin");
        if (origin == null || origin.isBlank()) {
            origin = "*";
        }
        cres.getHeaders().add("Access-Control-Allow-Origin", origin);
        cres.getHeaders().add("Access-Control-Allow-Headers", "Origin, content-type, accept, Authorization, pmp-environment");
        cres.getHeaders().add("Access-Control-Allow-Credentials", "true");
        cres.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
        cres.getHeaders().add("Access-Control-Max-Age", "1209600");
    }

}
