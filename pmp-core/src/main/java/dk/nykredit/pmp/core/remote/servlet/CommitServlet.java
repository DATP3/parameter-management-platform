package dk.nykredit.pmp.core.remote.servlet;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import dk.nykredit.pmp.core.commit.Commit;
import dk.nykredit.pmp.core.commit.CommitDirector;
import dk.nykredit.pmp.core.commit.CommitFactory;
import dk.nykredit.pmp.core.commit.exception.OldValueInconsistentException;
import dk.nykredit.pmp.core.commit.exception.StoredValueNullException;
import dk.nykredit.pmp.core.commit.exception.TypeInconsistentException;
import dk.nykredit.pmp.core.remote.json.ObjectMapperFactory;
import dk.nykredit.pmp.core.remote.json.raw_types.RawCommit;

public class CommitServlet extends HttpServlet {

    @Inject
    private ObjectMapperFactory objectMapperFactory;

    @Inject
    private CommitDirector commitDirector;

    @Inject
    private CommitFactory commitFactory;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        ObjectMapper mapper = objectMapperFactory.getObjectMapper();

        // Reads the raw commit from the request body and creates a Commit object
        RawCommit rawCommit = mapper.readValue(req.getInputStream(), RawCommit.class);
        Commit commit = commitFactory.createCommit(rawCommit);

        // Apply commit, and if any exceptions are thrown, return the appropriate HTTP
        // status code
        try {
            commitDirector.apply(commit);
            res.setStatus(HttpServletResponse.SC_OK);
            res.getWriter().write("OK");
        } catch (TypeInconsistentException e) {
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            res.getWriter().write("Could not apply commit, type is mismatched");
        } catch (OldValueInconsistentException e) {
            res.setStatus(HttpServletResponse.SC_CONFLICT);
            res.getWriter().write("Could not apply commit, old value does not match stored value");
        } catch (StoredValueNullException e) {
            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
            res.getWriter().write("Could not apply commit, parameter not found.");
        }
    }
}
