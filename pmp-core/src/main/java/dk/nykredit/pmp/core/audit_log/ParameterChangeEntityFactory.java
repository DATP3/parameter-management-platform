package dk.nykredit.pmp.core.audit_log;

import dk.nykredit.pmp.core.commit.Change;
import dk.nykredit.pmp.core.commit.ParameterChange;

public class ParameterChangeEntityFactory extends ChangeEntityFactory {

    public ParameterChangeEntityFactory(AuditLogEntry auditLogEntry) {
        super(auditLogEntry);
    }

    @Override
    public ChangeEntity createChangeEntity(Change change) {
        ChangeEntity changeEntity = new ChangeEntity();
        ParameterChange paramChange = (ParameterChange) change;
        changeEntity.setCommit(auditLogEntry);
        changeEntity.setParameterName(paramChange.getName());
        changeEntity.setParameterType(paramChange.getType());
        changeEntity.setOldValue(paramChange.getOldValue());
        changeEntity.setNewValue(paramChange.getNewValue());
        changeEntity.setChangeType(ChangeType.PARAMETER_CHANGE);

        return changeEntity;
    }
}