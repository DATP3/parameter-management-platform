package dk.nykredit.pmp.core.remote;

import dk.nykredit.pmp.core.remote.servlet.CommitServlet;
import dk.nykredit.pmp.core.remote.servlet.LogServlet;
import dk.nykredit.pmp.core.remote.servlet.ParametersServlet;
import org.eclipse.jetty.cdi.CdiDecoratingListener;
import org.eclipse.jetty.cdi.CdiServletContainerInitializer;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.servlet.ListenerHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.jboss.weld.environment.servlet.EnhancedListener;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.EnumSet;

public class PMPHandlerFactoryImpl implements PMPHandlerFactory {
    @Override
    public Handler getHandler() {
        ServletContextHandler cx = new ServletContextHandler();

        // Set the root path of the context
        cx.setContextPath("/pmp");

        // Add the servlets to the context
        cx.addServlet(ParametersServlet.class, "/parameters");
        cx.addServlet(CommitServlet.class, "/commit");
        cx.addServlet(LogServlet.class, "/log");

        // Add the required CORS headers to all requests
        cx.addFilter(CorsFilter.class, "/*",
                EnumSet.of(javax.servlet.DispatcherType.REQUEST,
                        javax.servlet.DispatcherType.ASYNC,
                        javax.servlet.DispatcherType.ERROR,
                        javax.servlet.DispatcherType.FORWARD,
                        javax.servlet.DispatcherType.INCLUDE));

        // Announce the service to the tracker when the API starts up
        cx.getServletHandler().addListener(new ListenerHolder(ServiceContextInitializer.class));

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

    /**
     * A filter that accepts GET and POST requests and adds CORS headers to allow
     * requests from all origins
     */
    public static class CorsFilter implements Filter {

        @Override
        public void init(FilterConfig filterConfig) throws ServletException {

        }

        @Override
        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
                throws IOException, ServletException {

            if (response instanceof HttpServletResponse && request instanceof HttpServletRequest) {
                HttpServletResponse httpRes = ((HttpServletResponse) response);
                HttpServletRequest httpReq = ((HttpServletRequest) request);

                // Allow any origin for now
                httpRes.setHeader("Access-Control-Allow-Origin", httpReq.getHeader("Origin"));
                httpRes.setHeader("Access-Control-Allow-Credentials", "true");
                httpRes.setHeader("Access-Control-Allow-Methods", "GET, POST");
                httpRes.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization, *");
            }
            chain.doFilter(request, response);
        }

        @Override
        public void destroy() {

        }
    }
}
