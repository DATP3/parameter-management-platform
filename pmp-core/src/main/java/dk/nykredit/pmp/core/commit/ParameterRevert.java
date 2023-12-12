package dk.nykredit.pmp.core.commit;

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

    @Override
    public List<ChangeEntity> apply(CommitDirector commitDirector) throws CommitException {
        if (commitHash == 0) {
            throw new IllegalArgumentException("commitHash cannot be 0 when applying revert");
        }

        AuditLog auditLog = commitDirector.getAuditLog();
        AuditLogEntry auditLogEntry = auditLog.getAuditLogEntry(commitHash);
        List<ChangeEntity> changeEntities = auditLogEntry.getChangeEntities();

        for (ChangeEntity changeEntity : changeEntities) {
            if (parameterName.equals(changeEntity.getParameterName())) {
                Object oldValueTyped = commitDirector.getParameterService().getTypeParsers()
                        .parse(changeEntity.getOldValue(), changeEntity.getParameterType());

                commitDirector.getParameterService().updateParameter(changeEntity.getParameterName(),
                        oldValueTyped);

                ChangeEntity resultingChangeEntity = new RevertPartChangeEntityFactory()
                        .createChangeEntity(changeEntity, ChangeType.PARAMETER_REVERT, commitHash);

                return List.of(resultingChangeEntity);
            }
        }

        throw new IllegalArgumentException(
                "No parameter change found with parameter name: " + parameterName + " in commit: "
                        + Long.toHexString(commitHash) + ".");
    }

    @Override
    public void undo(CommitDirector commitDirector) {
        AuditLog auditLog = commitDirector.getAuditLog();
        AuditLogEntry auditLogEntry = auditLog.getAuditLogEntry(commitHash);
        List<ChangeEntity> changeEntities = auditLogEntry.getChangeEntities();

        for (ChangeEntity changeEntity : changeEntities) {
            if (parameterName == changeEntity.getParameterName()) {
                Object newValueTyped = commitDirector.getParameterService().getTypeParsers()
                        .parse(changeEntity.getNewValue(), changeEntity.getParameterType());

                commitDirector.getParameterService().updateParameter(changeEntity.getParameterName(),
                        newValueTyped);
            }
        }
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
