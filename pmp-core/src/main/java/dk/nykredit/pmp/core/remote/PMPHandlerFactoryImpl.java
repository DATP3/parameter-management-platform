package dk.nykredit.pmp.core.remote;

import dk.nykredit.pmp.core.remote.servlet.LogServlet;

import java.io.IOException;
import java.util.EnumSet;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

        cx.addFilter(CorsFilter.class, "/*",
                EnumSet.of(javax.servlet.DispatcherType.REQUEST,
                        javax.servlet.DispatcherType.ASYNC,
                        javax.servlet.DispatcherType.ERROR,
                        javax.servlet.DispatcherType.FORWARD,
                        javax.servlet.DispatcherType.INCLUDE));

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

    public static class CorsFilter implements Filter {

        @Override
        public void init(FilterConfig filterConfig) throws ServletException {

        }

        @Override
        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
                throws IOException, ServletException {
            System.out.println("Filtering request");

            if (response instanceof HttpServletResponse && request instanceof HttpServletRequest) {
                HttpServletResponse httpRes = ((HttpServletResponse) response);
                HttpServletRequest httpReq = ((HttpServletRequest) request);

                System.out.println("HTTP!");

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
