package dk.nykredit.example.pmp;

import dk.nykredit.pmp.core.service.ParameterService;
import org.eclipse.jetty.util.log.Log;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class HelloServlet extends HttpServlet {

    @Inject
    private ParameterService parameterService;

    @Override
    public void init() throws ServletException {
        // Place a default value for the `person` parameter in the database on startup
        parameterService.persistParameter("person", "Test Person");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("text/plain");
        res.setStatus(HttpServletResponse.SC_OK);
        res.getWriter().println("Person is: " + parameterService.findParameterByName("person"));
    }
}
