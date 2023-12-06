package dk.nykredit.pmp.core.audit_log;

import dk.nykredit.pmp.core.commit.Change;
import dk.nykredit.pmp.core.commit.CommitRevert;

public class CommitRevertChangeEntityFactory extends ChangeEntityFactory {

    public CommitRevertChangeEntityFactory(AuditLogEntry auditLogEntry) {
        super(auditLogEntry);
    }

    @Override
    public ChangeEntity createChangeEntity(Change change) {
        ChangeEntity changeEntity = new ChangeEntity();
        CommitRevert commitRevert = (CommitRevert) change;

        changeEntity.setCommit(auditLogEntry);
        changeEntity.setChangeType(ChangeType.COMMIT_REVERT);
        changeEntity.setCommitRevertRef(commitRevert.getCommitHash());

        return changeEntity;
    }
}
