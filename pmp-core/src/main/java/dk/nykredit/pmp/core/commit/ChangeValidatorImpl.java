package dk.nykredit.pmp.core.commit;

import javax.inject.Inject;

import dk.nykredit.pmp.core.audit_log.AuditLog;
import dk.nykredit.pmp.core.util.ServiceInfoProvider;

public class ChangeValidatorImpl implements ChangeValidator {

    @Inject
    ServiceInfoProvider serviceInfoProvider;

    @Inject
    AuditLog auditLog;

    @Override
    public boolean validateChange(ParameterChange change) {
        
        if (!change.getPmpRoot().equals(serviceInfoProvider.getServiceInfo().getPmpRoot())) {
            return false;
        }
        return true;
    }

    @Override
    public boolean validateChange(ParameterRevert change) {
        
        if (!change.getPmpRoot().equals(serviceInfoProvider.getServiceInfo().getPmpRoot())) {
            return false;
        }

        return true;
    }

    @Override
    public boolean validateChange(CommitRevert change) {
        
        if (auditLog.getCommit(change.getCommitHash()) == null) {
            return false;
        }
        return true;
    }
}