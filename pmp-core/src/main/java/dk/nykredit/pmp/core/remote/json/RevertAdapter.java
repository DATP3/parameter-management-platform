package dk.nykredit.pmp.core.remote.json;

import java.io.IOException;

import javax.inject.Inject;

import com.fasterxml.jackson.annotation.JsonIgnore;

import dk.nykredit.pmp.core.audit_log.AuditLog;
import dk.nykredit.pmp.core.audit_log.ChangeType;
import dk.nykredit.pmp.core.commit.Change;
import dk.nykredit.pmp.core.commit.Commit;
import dk.nykredit.pmp.core.commit.CommitRevert;
import dk.nykredit.pmp.core.commit.ParameterChange;
import dk.nykredit.pmp.core.commit.RevertFactory;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RevertAdapter {
    private String parameterName;
    private String revertType;
    private String commitReference;

    @JsonIgnore
    @Inject
    AuditLog auditLog;

    public Change toRevert() throws IOException {
        long commitHash = Long.parseUnsignedLong(commitReference, 16);

        if (revertType.equals("parameter")) {
            Commit commit = auditLog.getCommit(commitHash);
            for (Change change : commit.getChanges()) {
                if (change instanceof ParameterChange) {
                    ParameterChange parameterChange = (ParameterChange) change;
                    if (parameterChange.getName().equals(parameterName)) {
                        return RevertFactory.createChange(parameterChange, commitHash, ChangeType.PARAMETER_REVERT);

                    }
                }
            }
        }

        if (revertType.equals("commit") || revertType.equals("service")) {
            ChangeType reveType = revertType.equals("commit") ? ChangeType.COMMIT_REVERT
                    : ChangeType.SERVICE_COMMIT_REVERT;

            CommitRevert commitRevert = new CommitRevert();
            commitRevert.setCommitHash(commitHash);
            commitRevert.setRevertType(reveType);
            return commitRevert;
        }

        throw new IllegalArgumentException("Invalid revert type: " + revertType);
    }
}