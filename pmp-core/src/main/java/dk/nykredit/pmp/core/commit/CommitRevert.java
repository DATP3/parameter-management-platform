package dk.nykredit.pmp.core.commit;

import java.util.ArrayList;
import java.util.List;

import dk.nykredit.pmp.core.audit_log.AuditLog;
import dk.nykredit.pmp.core.audit_log.AuditLogEntry;
import dk.nykredit.pmp.core.audit_log.ChangeEntity;
import dk.nykredit.pmp.core.audit_log.ChangeType;
import dk.nykredit.pmp.core.commit.exception.CommitException;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class CommitRevert implements Change {
    private long commitHash;

	public List<PersistableChange> apply(CommitDirector commitDirector) throws CommitException {
		AuditLog auditLog = commitDirector.getAuditLog();
		AuditLogEntry auditLogEntry = auditLog.getAuditLogEntry(commitHash);
		List<ChangeEntity> changeEntities = auditLogEntry.getChangeEntities();
		List<PersistableChange> appliedChanges = new ArrayList<>();

		for (ChangeEntity changeEntity : changeEntities) {
			AuditLogEntry latestChange = auditLog.getLatestCommitToParameter(changeEntity.getParameterName());
			if (latestChange == null || latestChange.getCommitId() != commitHash) {
				continue;
			}

			PersistableChange resultingParameterRevert = RevertFactory.createChange(changeEntity,
					ChangeType.COMMIT_REVERT);
			resultingParameterRevert.apply(commitDirector);
			appliedChanges.add(resultingParameterRevert);
		}

		return appliedChanges;
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
