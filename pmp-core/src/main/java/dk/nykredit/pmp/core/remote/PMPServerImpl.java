package dk.nykredit.pmp.core.remote;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;

public class PMPServerImpl implements PMPServer {

    // TODO: This can be injected but Weld is being rude :(
    private final TrackerService trackerService = new TrackerServiceImpl();
    private final PMPHandlerFactory handlerFactory = new PMPHandlerFactoryImpl();

    private final Server server;
    private final int port;

    public PMPServerImpl() {
        port = Integer.parseInt(System.getProperty("dk.nykredit.pmp.remote.port", "64017"));
        server = new Server(port);
    }

    public void start() {

        Handler handler = handlerFactory.getHandler();
        server.setHandler(handler);

        System.out.println("Starting PMP remote on port " + port);
        try {
            server.start();
            // TODO: Get the right data about the service
            System.out.println("Announcing to tracker");
            boolean success = trackerService.announce("http://localhost:" + port + "/", "Test Service", "Test1");
            if (success) {
                System.out.println("Registered to tracker successfully");
            } else {
                throw new RuntimeException("Could not register PMP to tracker");
            }
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    public void stop() {
        try {
            server.stop();
        } catch (Exception e) {
            throw new Error(e);
        }
    }

}
