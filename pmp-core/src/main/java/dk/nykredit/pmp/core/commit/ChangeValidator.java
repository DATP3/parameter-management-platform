package dk.nykredit.pmp.core.commit;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dk.nykredit.pmp.core.audit_log.AuditLog;
import dk.nykredit.pmp.core.util.ServiceInfoProvider;
import dk.nykredit.pmp.core.util.ChangeVisitor;

public class ChangeValidator implements ChangeVisitor {

    @Inject
    ServiceInfoProvider serviceInfoProvider;

    @Inject
    AuditLog auditLog;



    private List<Change> validatedChanges;
    
    public ChangeValidator() {
        validatedChanges = new ArrayList<Change>();
    }

    public List<Change> getValidatedChanges() {
        return validatedChanges;
    }

    @Override
    public void visit(ParameterChange change) {
        
        throw new RuntimeException("change.getPmpRoot(): " + change.getPmpRoot() + ", getServiceInfo().getPmpRoot(): " + serviceInfoProvider.getServiceInfo().getPmpRoot());

        //if (!change.getPmpRoot().equals(serviceInfoProvider.getServiceInfo().getPmpRoot())) {
        //    return;
        //}
        //
        //validatedChanges.add(change);

        //return;
    }

    @Override
    public void visit(ParameterRevert change) {
        
        if (!change.getPmpRoot().equals(serviceInfoProvider.getServiceInfo().getPmpRoot())) {
            return;
        }

        validatedChanges.add(change);
        return;
    }

    @Override
    public void visit(CommitRevert change) {
        
        if (auditLog.getCommit(change.getCommitHash()) == null) {
            return;
        }

        validatedChanges.add(change);

        return;
    }

}