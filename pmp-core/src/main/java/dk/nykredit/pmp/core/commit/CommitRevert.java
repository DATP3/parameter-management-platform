package dk.nykredit.pmp.core.commit;

import dk.nykredit.pmp.core.audit_log.AuditLog;
import dk.nykredit.pmp.core.audit_log.AuditLogEntry;
import dk.nykredit.pmp.core.audit_log.ChangeEntity;
import dk.nykredit.pmp.core.commit.exception.CommitException;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class CommitRevert implements Change {
    private long commitHash;

    @Override
    public void apply(CommitDirector commitDirector) throws CommitException {
        AuditLog auditLog = commitDirector.getAuditLog();
        AuditLogEntry auditLogEntry = auditLog.getAuditLogEntry(commitHash);
        List<ChangeEntity> changeEntities = auditLogEntry.getChangeEntities();

        for (ChangeEntity change : changeEntities) {
            AuditLogEntry latestChange = auditLog.getLatestCommitToParameter(change.getParameterName());
            if (latestChange == null || latestChange.getCommitId() != commitHash) {
                continue;
            }

            change.toChange().undo(commitDirector);
            /*
             * TODO: Make sure only changes made here, is included in the commit stored in
             * the audit log after the revert
             */
        }
    }

    public void undo(CommitDirector commitDirector) {
        AuditLog auditLog = commitDirector.getAuditLog();
        AuditLogEntry auditLogEntry = auditLog.getAuditLogEntry(commitHash);
        List<ChangeEntity> changeEntities = auditLogEntry.getChangeEntities();

        for (ChangeEntity change : changeEntities) {
            AuditLogEntry latestChange = auditLog.getLatestCommitToParameter(change.getParameterName());
            if (latestChange == null || latestChange.getCommitId() != commitHash) {
                continue;
            }

            change.toChange().apply(commitDirector);
        }
    }
}
