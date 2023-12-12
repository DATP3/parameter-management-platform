package dk.nykredit.pmp.core.remote.json;

import dk.nykredit.pmp.core.audit_log.AuditLogEntry;
import dk.nykredit.pmp.core.util.ServiceInfoProvider;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.inject.Inject;
import java.util.List;

@Getter
@Setter(AccessLevel.PRIVATE)
public class AuditLogResponse {

	// Force use of factory
	private AuditLogResponse() {
	}

	private List<AuditLogEntry> commits;

	private String name;

	public static class Factory {
		@Inject
		private ServiceInfoProvider serviceInfo;

		public AuditLogResponse fromEntries(List<AuditLogEntry> entries) {
			AuditLogResponse res = new AuditLogResponse();
			res.setCommits(entries);
			res.setName(serviceInfo.getServiceInfo().getName());

			return res;
		}
	}
}
