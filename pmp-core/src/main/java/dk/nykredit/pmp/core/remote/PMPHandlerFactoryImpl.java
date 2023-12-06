package dk.nykredit.pmp.core.remote;

import dk.nykredit.pmp.core.remote.servlet.LogServlet;
import org.eclipse.jetty.cdi.CdiDecoratingListener;
import org.eclipse.jetty.cdi.CdiServletContainerInitializer;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.jboss.weld.environment.servlet.EnhancedListener;

import dk.nykredit.pmp.core.remote.servlet.CommitServlet;
import dk.nykredit.pmp.core.remote.servlet.ParametersServlet;

public class PMPHandlerFactoryImpl implements PMPHandlerFactory {
	@Override
	public Handler getHandler() {
		ServletContextHandler cx = new ServletContextHandler();
		cx.setContextPath("/");
		cx.addServlet(ParametersServlet.class, "/parameters");
		cx.addServlet(CommitServlet.class, "/commit");
		cx.addServlet(LogServlet.class, "/log");

		// Initialize CDI
		cx.setInitParameter(
				CdiServletContainerInitializer.CDI_INTEGRATION_ATTRIBUTE,
				CdiDecoratingListener.MODE);
		cx.addBean(new ServletContextHandler.Initializer(cx,
				new EnhancedListener()));
		cx.addBean(new ServletContextHandler.Initializer(cx,
				new CdiServletContainerInitializer()));

		return cx;
	}
}
