package dk.nykredit.pmp.core.remote.servlet;

import dk.nykredit.pmp.core.persistence.ParameterEntity;
import dk.nykredit.pmp.core.repository.ParameterRepository;

import org.eclipse.jetty.util.ajax.JSON;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ParametersServlet extends HttpServlet {

	@Inject
	ParameterRepository parameterRepository;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		List<ParameterEntity> entities = parameterRepository.getParameters();

		String json = JSON.toString(Map.of("parameters", entities.toArray()));

		res.setStatus(HttpServletResponse.SC_OK);
		res.setContentType("application/json");
		res.getWriter().write(json);
	}
}
