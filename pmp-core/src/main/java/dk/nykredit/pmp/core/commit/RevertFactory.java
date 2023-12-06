package dk.nykredit.pmp.core.commit;

import dk.nykredit.pmp.core.audit_log.ChangeEntity;
import dk.nykredit.pmp.core.audit_log.ChangeType;

public class RevertFactory {
    public static Change createChange(ChangeEntity originalChangeEntity, ChangeType reverType) {
        ParameterRevert revert = new ParameterRevert();
        revert.setCommitHash(originalChangeEntity.getCommitRevertRef());
        revert.setParameterName(originalChangeEntity.getParameterName());

        return revert;
    }

    public static Change createChange(ParameterChange originalChange, long commitHash,
            ChangeType revertType) {
        ParameterRevert revert = new ParameterRevert();
        revert.setCommitHash(commitHash);
        revert.setParameterName(originalChange.getName());

        return revert;
    }
}
