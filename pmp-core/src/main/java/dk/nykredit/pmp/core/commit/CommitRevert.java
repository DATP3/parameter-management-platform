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

@Setter
@Getter
public class CommitRevert implements Change {
    private long commitHash;
    private ChangeType revertType;

    public CommitRevert() {
        // Default revert type. Can also be SERVICE_COMMIT_REVERT
        revertType = ChangeType.COMMIT_REVERT;
    }

    public CommitRevert(long commitHash, ChangeType revertType) {
        this.commitHash = commitHash;
        this.revertType = revertType;
    }

    public List<Change> apply(CommitDirector commitDirector) throws CommitException {
        if (commitHash == 0) {
            throw new IllegalArgumentException("rommitHash cannot be 0 when applying revert");
        }

        if (revertType == null) {
            throw new IllegalArgumentException("revertType cannot be null when applying revert");
        }

        AuditLog auditLog = commitDirector.getAuditLog();
        AuditLogEntry auditLogEntry = auditLog.getAuditLogEntry(commitHash);
        List<ChangeEntity> changeEntities = auditLogEntry.getChangeEntities();
        List<Change> appliedChanges = new ArrayList<>();

        for (ChangeEntity changeEntity : changeEntities) {
            if (changeEntity.getParameterName() != null) {
                AuditLogEntry latestChange = auditLog.getLatestCommitToParameter(changeEntity.getParameterName());
                if (latestChange == null || latestChange.getCommitId() != commitHash) {
                    continue;
                }
            }

            Change refencedChange = changeEntity.toChange();
            refencedChange.undo(commitDirector);
            appliedChanges.add(this);
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

    @Override
    public int hashCode() {
        return Long.hashCode(commitHash) * 2;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof CommitRevert)) {
            return false;
        }

        CommitRevert other = (CommitRevert) obj;
        return commitHash == other.getCommitHash() && revertType == other.getRevertType();
    }

    @Override
    public String toString() {
        return "CommitRevert {\n    commitHash=" + commitHash + ", \n    revertType=" + revertType + "\n}";
    }
}
