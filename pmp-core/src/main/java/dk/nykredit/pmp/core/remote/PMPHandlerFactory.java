package dk.nykredit.pmp.core.remote;

import org.eclipse.jetty.server.Handler;

public interface PMPHandlerFactory {
    Handler getHandler();
}
