package dk.nykredit.pmp.core.remote;

import org.eclipse.jetty.server.Handler;

public interface PMPHandlerFactory {
    /**
     * Get the handler for a Jetty server to host PMP
     * @return The handler that exposes the PMP API
     */
    Handler getHandler();
}
