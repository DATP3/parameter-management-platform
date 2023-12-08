package dk.nykredit.pmp.core.remote;

import dk.nykredit.pmp.core.util.ServiceInfo;
import dk.nykredit.pmp.core.util.ServiceInfoProvider;
import dk.nykredit.pmp.core.util.ServiceInfoProviderImpl;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;

public class PMPServerImpl implements PMPServer {

    // TODO: This can be injected but Weld is being rude :(
    private final TrackerService trackerService = new TrackerServiceImpl();
    private final PMPHandlerFactory handlerFactory = new PMPHandlerFactoryImpl();
    private final ServiceInfoProvider serviceInfoProvider = new ServiceInfoProviderImpl();

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
            // ServiceInfo serviceInfo = serviceInfoProvider.getServiceInfo();
            try {
                // trackerService.announce(serviceInfo.getPmpRoot(), serviceInfo.getName(),
                // serviceInfo.getEnvironment());
                trackerService.announce("http://localhost:64017", "Example service", "prod");
            } catch (Exception e) {
                throw new Error(e);
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
