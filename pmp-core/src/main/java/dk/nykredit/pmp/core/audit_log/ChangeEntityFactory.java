package dk.nykredit.pmp.core.audit_log;

import dk.nykredit.pmp.core.commit.ParameterChange;
import dk.nykredit.pmp.core.commit.ParameterRevert;
import dk.nykredit.pmp.core.commit.PersistableChange;

public class ChangeEntityFactory {
    private AuditLogEntry auditLogEntry;

    public ChangeEntityFactory(AuditLogEntry auditLogEntry) {
        this.auditLogEntry = auditLogEntry;
    }

    public ChangeEntity createChangeEntity(PersistableChange change) {

        if (!(change instanceof ParameterChange)) {
            return null;
        }

        ChangeEntity changeEntity = new ChangeEntity();
        ParameterChange paramChange = (ParameterChange) change;
        changeEntity.setCommit(auditLogEntry);
        changeEntity.setParameterName(paramChange.getName());
        changeEntity.setParameterType(paramChange.getType());
        changeEntity.setOldValue(paramChange.getOldValue());
        changeEntity.setNewValue(paramChange.getNewValue());
        changeEntity.setChangeType(ChangeType.PARAMETER_CHANGE);

        if (change instanceof ParameterRevert) {
            ParameterRevert paramRevert = (ParameterRevert) change;
            changeEntity.setChangeType(paramRevert.getRevertType());
            changeEntity.setCommitRevertRef(auditLogEntry.getCommitId());
        }

        return changeEntity;
    }
}