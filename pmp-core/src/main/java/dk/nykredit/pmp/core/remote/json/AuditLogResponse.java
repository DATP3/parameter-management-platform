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

    // Implements its own factory to make sure AuditLogResponse can only be
    // instantiated through the factory, therby making sure the name is always the
    // name of the service.
    public static class Factory {
        @Inject
        private ServiceInfoProvider serviceInfo;

        /**
         * Creates a new AuditLogResponse object from a list of AuditLogEntry objects,
         * with the name being the name of the serivce gathered from the
         * ServiceInfoProvider.
         * 
         * @param entries to make the response from
         * @return a new AuditLogResponse object
         */
        public AuditLogResponse fromEntries(List<AuditLogEntry> entries) {
            AuditLogResponse res = new AuditLogResponse();
            res.setCommits(entries);
            res.setName(serviceInfo.getServiceInfo().getName());

            return res;
        }
    }
}
