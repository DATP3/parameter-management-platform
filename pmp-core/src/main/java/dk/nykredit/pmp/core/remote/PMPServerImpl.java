package dk.nykredit.pmp.core.remote;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;

public class PMPServerImpl implements PMPServer {

    private final PMPHandlerFactory handlerFactory = new PMPHandlerFactoryImpl();

    private final Server server;
    private final int port;

    /**
     * Create a new PMP server on port 64017.
     */
    public PMPServerImpl() {
        port = Integer.parseInt(System.getProperty("dk.nykredit.pmp.remote.port", "64017"));
        server = new Server(port);
    }

    /**
     * Create a new PMP server on the specified port.
     */
    public void start() {

        // Get the handler for the PMP API
        Handler handler = handlerFactory.getHandler();
        server.setHandler(handler);

        // Start the server
        System.out.println("Starting PMP remote on port " + port);
        try {
            server.start();
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    /**
     * Stop the PMP server.
     */
    public void stop() {
        try {
            server.stop();
        } catch (Exception e) {
            throw new Error(e);
        }
    }

}
