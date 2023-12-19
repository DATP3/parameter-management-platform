package dk.nykredit.pmp.core.commit;

import java.util.ArrayList;
import java.util.List;

import dk.nykredit.pmp.core.audit_log.AuditLog;
import dk.nykredit.pmp.core.audit_log.AuditLogEntry;
import dk.nykredit.pmp.core.audit_log.ChangeEntity;
import dk.nykredit.pmp.core.audit_log.ChangeType;
import dk.nykredit.pmp.core.audit_log.RevertPartChangeEntityFactory;
import dk.nykredit.pmp.core.util.ChangeVisitor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParameterRevert implements Change {
    private long commitHash;
    private String revertType;
    private Service service;
    private String parameterName;

    public ParameterRevert() {
    }

    public ParameterRevert(String parameterName, long commitHash, String type, Service service) {
        this.parameterName = parameterName;
        this.commitHash = commitHash;
        this.revertType = type;
        this.service = service;
    }

    public ParameterRevert(String parameterName, long commitHash) {
        this.parameterName = parameterName;
        this.commitHash = commitHash;
    }

    /**
     * @param commitDirector The commit director is used to get the parameter
     *                       service so it can be used to change parameters, as well
     *                       as the audit log to find the change to be reverted.
     * @return A list of change entities representing the changes made by the
     *         parameter revert. In this case a list of one element.
     */
    @Override
    public List<ChangeEntity> apply(CommitDirector commitDirector) {
        // Properbility of commitHash being 0 is very low, so if it is, its most likely
        // a mistake
        if (commitHash == 0) {
            throw new IllegalArgumentException("commitHash cannot be 0 when applying revert");
        }

        // Finds the list of changes that was made in the commit that the change to be
        // reverted was in.
        AuditLog auditLog = commitDirector.getAuditLog();
        AuditLogEntry auditLogEntry = auditLog.getAuditLogEntry(commitHash);
        List<ChangeEntity> changeEntities = auditLogEntry.getChangeEntities();

        for (ChangeEntity changeEntity : changeEntities) {
            // If the parameter name matches the parameter name of the change to be
            // reverted, the change to be reverted is found.
            if (parameterName.equals(changeEntity.getParameterName())) {

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
                    // No revertable change was found
                    return new ArrayList<>();
                }

                // The old value of the parameter is typed.
                Object oldValueTyped = commitDirector.getParameterService().getTypeParsers()
                        .parse(changeEntity.getOldValue(), changeEntity.getParameterType());

                // The parameter is updated to the old value of the found change, thereby
                // reverting it to what it was before the change.
                commitDirector.getParameterService().updateParameter(changeEntity.getParameterName(),
                        oldValueTyped);

                // A change entity is created to represent the change that was made by the
                // revert.
                ChangeEntity resultingChangeEntity = new RevertPartChangeEntityFactory().createChangeEntity(
                        changeEntity,
                        ChangeType.COMMIT_REVERT, commitHash);

                return List.of(resultingChangeEntity);
            }
        }

        throw new IllegalArgumentException(
                "No parameter change found with parameter name: " + parameterName + " in commit: "
                        + Long.toHexString(commitHash) + ".");
    }

    @Override
    public String toString() {
        return "ParameterRevert {\n"
                + "commitHash: " + commitHash +
                ", \nparameterName: " + parameterName + "}";
    }

    @Override
    public int hashCode() {
        return Long.hashCode(commitHash) + parameterName.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ParameterRevert)) {
            return false;
        }

        ParameterRevert other = (ParameterRevert) obj;
        return commitHash == other.commitHash
                && parameterName.equals(other.parameterName);
    }

    @Override
    public void acceptVisitor(ChangeVisitor changeVisitor) {
        changeVisitor.visit(this);
    }
}
