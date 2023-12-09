package dk.nykredit.pmp.core.util;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dk.nykredit.pmp.core.audit_log.AuditLog;
import dk.nykredit.pmp.core.commit.Change;
import dk.nykredit.pmp.core.commit.CommitRevert;
import dk.nykredit.pmp.core.commit.ParameterChange;
import dk.nykredit.pmp.core.commit.ParameterRevert;

public class ChangeValidator implements ChangeVisitor {

    ServiceInfo serviceInfo;

    @Inject
    AuditLog auditLog;



    private List<Change> validatedChanges;
    
    public ChangeValidator(ServiceInfo serviceInfo) {
        this.serviceInfo = serviceInfo;
        validatedChanges = new ArrayList<Change>();
    }

    public ChangeValidator() {
        validatedChanges = new ArrayList<Change>();
    }

    public List<Change> getValidatedChanges() {
        return validatedChanges;
    }

    @Override
    public void visit(ParameterChange change) {
        
        if (change.getService() == null || 
            change.getService().getPmpRoot() == null) {

            throw new IllegalArgumentException("PmpRoot cannot be null");
        }

        if (serviceInfo.getPmpRoot() == null) {
            throw new IllegalArgumentException("ServicePmpRoot cannot be null");
        }


        if (!change.getService().getPmpRoot().equals(serviceInfo.getPmpRoot())) {
            return;
        }
        
        validatedChanges.add(change);
        return;
    }

    @Override
    public void visit(ParameterRevert change) {

        if (change.getService() == null || 
            change.getService().getPmpRoot() == null) {
                
            throw new IllegalArgumentException("PmpRoot cannot be null");
        }
        
        if (!change.getService().getPmpRoot().equals(serviceInfo.getPmpRoot())) {
            return;
        }

        validatedChanges.add(change);
        return;
    }

    @Override
    public void visit(CommitRevert change) {
        
        if (auditLog.getAuditLogEntry(change.getCommitHash()) == null) {
            return;
        }

        validatedChanges.add(change);

        return;
    }

}