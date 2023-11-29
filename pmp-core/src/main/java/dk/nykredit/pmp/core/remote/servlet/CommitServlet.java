package dk.nykredit.pmp.core.remote.servlet;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import dk.nykredit.pmp.core.commit.Commit;
import dk.nykredit.pmp.core.remote.json.ObjectMapperFactory;
import dk.nykredit.pmp.core.service.ParameterService;

public class CommitServlet extends HttpServlet {
    @Inject
    private ParameterService parameterService;

    @Inject
    private ObjectMapperFactory objectMapperFactory;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        ObjectMapper mapper = objectMapperFactory.getObjectMapper();

        Commit commit = mapper.readValue(req.getInputStream(), Commit.class);

        System.out.println("Applying commit: " + commit.toString());

        boolean result = commit.apply(parameterService);
        if (result) {
            res.setStatus(HttpServletResponse.SC_OK);
            res.getWriter().write("OK");
        } else {
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            res.getWriter().write("Could not apply commit");
        }
    }

}
