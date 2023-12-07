package dk.nykredit.pmp.core.remote.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import dk.nykredit.pmp.core.audit_log.AuditLog;
import dk.nykredit.pmp.core.audit_log.AuditLogEntry;
import dk.nykredit.pmp.core.remote.json.AuditLogResponse;
import dk.nykredit.pmp.core.remote.json.ObjectMapperFactory;

import javax.inject.Inject;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class LogServlet extends HttpServlet {

	@Inject
	private AuditLog auditLog;

	@Inject
	private ObjectMapperFactory objectMapperFactory;

	@Inject
	private AuditLogResponse.Factory responseFactory;

	private ObjectMapper objectMapper;

	@Override
	public void init() {
		this.objectMapper = objectMapperFactory.getObjectMapper();
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
		List<AuditLogEntry> entries = auditLog.getEntries();
		AuditLogResponse response = responseFactory.fromEntries(entries);

		res.setStatus(HttpServletResponse.SC_OK);
		objectMapper.writeValue(res.getWriter(), response);
	}
}
