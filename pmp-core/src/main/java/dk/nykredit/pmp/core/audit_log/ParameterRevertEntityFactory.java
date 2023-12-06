package dk.nykredit.pmp.core.audit_log;

import dk.nykredit.pmp.core.commit.Change;
import dk.nykredit.pmp.core.commit.ParameterRevert;

public class ParameterRevertEntityFactory extends ChangeEntityFactory {

    public ParameterRevertEntityFactory(AuditLogEntry auditLogEntry) {
        super(auditLogEntry);
    }

    @Override
    public ChangeEntity createChangeEntity(Change change) {
        ChangeEntity changeEntity = new ChangeEntity();
        ParameterRevert paramRevert = (ParameterRevert) change;

        changeEntity.setCommit(auditLogEntry);
        changeEntity.setCommitRevertRef(paramRevert.getCommitHash());
        changeEntity.setParameterName(paramRevert.getParameterName());
        changeEntity.setChangeType(ChangeType.PARAMETER_REVERT);

        return changeEntity;
    }
}
