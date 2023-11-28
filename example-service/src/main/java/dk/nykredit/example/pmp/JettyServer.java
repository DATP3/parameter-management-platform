package dk.nykredit.example.pmp;

import dk.nykredit.pmp.core.remote.PMPServer;
import dk.nykredit.pmp.core.remote.PMPServerImpl;
import org.eclipse.jetty.cdi.CdiDecoratingListener;
import org.eclipse.jetty.cdi.CdiServletContainerInitializer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;
import org.jboss.weld.environment.servlet.EnhancedListener;

public class JettyServer {
	private static final int SERVER_PORT = 40535;
	private final Server server;

	private static final Logger LOGGER = Log.getLogger(JettyServer.class);

	public JettyServer() {
		server = new Server(SERVER_PORT);
	}

	public void start() throws Exception {
		ServletContextHandler cx = new ServletContextHandler();
		cx.setContextPath("/");

		// Even though DatabaseInitializer is marked as @Startup and @Singleton,
		// it refuses to run when CDI is initialized...
		DatabaseInitializer databaseInitializer = new DatabaseInitializer();
		databaseInitializer.startDatabase();

		server.setHandler(cx);

		cx.addServlet(HelloServlet.class, "/");

		// Initialize CDI
		cx.setInitParameter(
				CdiServletContainerInitializer.CDI_INTEGRATION_ATTRIBUTE,
				CdiDecoratingListener.MODE);
		cx.addBean(new ServletContextHandler.Initializer(cx,
				new EnhancedListener()));
		cx.addBean(new ServletContextHandler.Initializer(cx,
				new CdiServletContainerInitializer()));

		// Either start a fresh Jetty server like this or add the handler from
		// `PMPHandlerFactory.getHandler()` to
		// an existing server
		PMPServer pmpServer = new PMPServerImpl();

		LOGGER.info("Starting Jetty Server");

		server.start();
		pmpServer.start();
		server.join();
		databaseInitializer.stopDatabase();
	}
}
