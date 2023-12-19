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

    /**
     * @param commitDirector The commit director is used to edit parameters as well
     *                       as finding the commit to be reverted from the audit
     *                       log.
     * @return A list of change entities representing the changes made by the commit
     *         revert
     * @throws IllegalArgumentException If the commit hash is 0 or if a change from
     *                                  the commit to be reverted, does not have a
     *                                  parameter name.
     */
    @Override
    public List<ChangeEntity> apply(CommitDirector commitDirector) throws CommitException {
        if (commitHash == 0) {
            throw new IllegalArgumentException("commitHash cannot be 0 when applying revert");
        }

        // Finds the list of changes that was made in the commit should be reverted.
        AuditLog auditLog = commitDirector.getAuditLog();
        AuditLogEntry auditLogEntry = auditLog.getAuditLogEntry(commitHash);
        List<ChangeEntity> changeEntities = auditLogEntry.getChangeEntities();

        // Defines a list of changes that will be applied
        List<ChangeEntity> appliedChanges = new ArrayList<>();

        // Reverts each change from the commit that should be reverted if the change is
        // the latest.
        for (ChangeEntity changeEntity : changeEntities) {
            if (changeEntity.getParameterName() == null) {
                throw new IllegalArgumentException("Parameter name cannot be null when reverting commit");
            }

            // If the parameter has been changed since the commit the change to be reverted
            // was from, it should not be reverted unless the latest change was a revert.
            AuditLogEntry latestChange = auditLog.getLatestCommitToParameter(changeEntity.getParameterName());
            if (latestChange == null || latestChange.getCommitId() != commitHash) {

                // In case the latet change was a revert, the parameter could also have been
                // changed to the current state by a parameter change. This is then found here.
                latestChange = auditLog
                        .getLatestNotRevertedChangeToParameter(changeEntity.getParameterName());
            }

            // If the found change commit hash does not match the commit hash of the revert,
            // it should not be reverted.
            if (latestChange == null || latestChange.getCommitId() != commitHash) {
                continue;
            }

            // Types the old value of the change to be reverted
            Object oldValueTyped = commitDirector.getParameterService().getTypeParsers()
                    .parse(changeEntity.getOldValue(), changeEntity.getParameterType());

            // Updates the parameter to the old value of the change thereby reverting the
            // change.
            commitDirector.getParameterService().updateParameter(changeEntity.getParameterName(),
                    oldValueTyped);

            // Creates a change entity representing the change that was made by the revert
            // and adds it to the applied changes list.
            ChangeEntity resultingChangeEntity = new RevertPartChangeEntityFactory().createChangeEntity(changeEntity,
                    ChangeType.COMMIT_REVERT, commitHash);

            appliedChanges.add(resultingChangeEntity);
        }

        return appliedChanges;
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
