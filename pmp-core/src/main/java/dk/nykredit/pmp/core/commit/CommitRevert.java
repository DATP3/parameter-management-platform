package dk.nykredit.pmp.core.commit;

import java.util.ArrayList;
import java.util.List;

import dk.nykredit.pmp.core.audit_log.AuditLog;
import dk.nykredit.pmp.core.audit_log.AuditLogEntry;
import dk.nykredit.pmp.core.audit_log.ChangeEntity;
import dk.nykredit.pmp.core.audit_log.ChangeType;
import dk.nykredit.pmp.core.audit_log.RevertPartChangeEntityFactory;
import dk.nykredit.pmp.core.commit.exception.CommitException;
import dk.nykredit.pmp.core.util.ChangeVisitor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CommitRevert implements Change {
    private long commitHash;

    public CommitRevert() {
    }

    public CommitRevert(long commitHash) {
        this.commitHash = commitHash;
    }

    public List<ChangeEntity> apply(CommitDirector commitDirector) throws CommitException {
        if (commitHash == 0) {
            throw new IllegalArgumentException("rommitHash cannot be 0 when applying revert");
        }

        AuditLog auditLog = commitDirector.getAuditLog();
        AuditLogEntry auditLogEntry = auditLog.getAuditLogEntry(commitHash);
        List<ChangeEntity> changeEntities = auditLogEntry.getChangeEntities();
        List<ChangeEntity> appliedChanges = new ArrayList<>();

        for (ChangeEntity changeEntity : changeEntities) {
            if (changeEntity.getParameterName() == null) {
                throw new IllegalArgumentException("Parameter name cannot be null when reverting commit");
            }

            AuditLogEntry latestChange = auditLog.getLatestCommitToParameter(changeEntity.getParameterName());
            if (latestChange == null || latestChange.getCommitId() != commitHash) {
                continue;
            }

            Object oldValueTyped = commitDirector.getParameterService().getTypeParsers()
                    .parse(changeEntity.getOldValue(), changeEntity.getParameterType());

            commitDirector.getParameterService().updateParameter(changeEntity.getParameterName(),
                    oldValueTyped);

            ChangeEntity resultingChangeEntity = new RevertPartChangeEntityFactory().createChangeEntity(changeEntity,
                    ChangeType.COMMIT_REVERT, commitHash);

            appliedChanges.add(resultingChangeEntity);
        }

        return appliedChanges;
    }

    public void undo(CommitDirector commitDirector) {
        AuditLog auditLog = commitDirector.getAuditLog();
        AuditLogEntry auditLogEntry = auditLog.getAuditLogEntry(commitHash);
        List<ChangeEntity> changeEntities = auditLogEntry.getChangeEntities();

        for (ChangeEntity changeEntity : changeEntities) {
            if (changeEntity.getParameterName() == null) {
                throw new IllegalArgumentException("Parameter name cannot be null when reverting commit");
            }

            AuditLogEntry latestChange = auditLog.getLatestCommitToParameter(changeEntity.getParameterName());
            if (latestChange == null || latestChange.getCommitId() != commitHash) {
                continue;
            }

            Object newValueTyped = commitDirector.getParameterService().getTypeParsers()
                    .parse(changeEntity.getNewValue(), changeEntity.getParameterType());

            commitDirector.getParameterService().updateParameter(changeEntity.getParameterName(), newValueTyped);
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
        return commitHash == other.getCommitHash();
    }

    @Override
    public String toString() {
        return "CommitRevert {\n    commitHash=" + commitHash + "\n}";
    }

    @Override
    public void acceptVisitor(ChangeVisitor changeVisitor) {
        changeVisitor.visit(this);
    }
}
