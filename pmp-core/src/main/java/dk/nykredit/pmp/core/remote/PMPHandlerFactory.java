package dk.nykredit.pmp.core.remote;

import org.eclipse.jetty.server.Handler;

public interface PMPHandlerFactory {
    /**
     * Get the handler for the PMP API
     *
     * @return The handler for the PMP API
     */
    Handler getHandler();
}
