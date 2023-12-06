package dk.nykredit.pmp.core.commit;

import java.util.ArrayList;
import java.util.List;

import dk.nykredit.pmp.core.audit_log.AuditLog;
import dk.nykredit.pmp.core.audit_log.AuditLogEntry;
import dk.nykredit.pmp.core.audit_log.ChangeEntity;
import dk.nykredit.pmp.core.audit_log.ChangeEntityFactory;
import dk.nykredit.pmp.core.audit_log.ChangeType;
import dk.nykredit.pmp.core.commit.exception.CommitException;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParameterRevert implements Change {
    private long commitHash;
    private String parameterName;

    public ParameterRevert() {
    }

    public ParameterRevert(String parameterName, long commitHash) {
        this.commitHash = commitHash;
        this.parameterName = parameterName;
    }

    @Override
    public List<Change> apply(CommitDirector commitDirector) throws CommitException {
        if (commitHash == 0) {
            throw new IllegalArgumentException("rommitHash cannot be 0 when applying revert");
        }

        Change parameterChange = getRefrencedChange(commitDirector.getAuditLog());
        parameterChange.undo(commitDirector);
        return List.of(this);
    }

    public Change getRefrencedChange(AuditLog auditLog) {
        AuditLogEntry auditLogEntry = auditLog.getAuditLogEntry(commitHash);
        List<ChangeEntity> changeEntities = auditLogEntry.getChangeEntities();

        for (ChangeEntity changeEntity : changeEntities) {
            AuditLogEntry latestChange = auditLog.getLatestCommitToParameter(changeEntity.getParameterName());
            if (latestChange == null || latestChange.getCommitId() != commitHash) {
                continue;
            }

            if (parameterName == changeEntity.getParameterName()) {
                return changeEntity.toChange();
            }
        }

        throw new IllegalArgumentException("No parameter change found with parameter name: " + parameterName);
    }

    @Override
    public void undo(CommitDirector commitDirector) {
        Change parameterChange = getRefrencedChange(commitDirector.getAuditLog());
        parameterChange.apply(commitDirector);
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
        if (obj == null || !(obj instanceof ParameterRevert)) {
            return false;
        }

        ParameterRevert other = (ParameterRevert) obj;
        return commitHash == other.commitHash
                && parameterName.equals(other.parameterName);
    }
}
