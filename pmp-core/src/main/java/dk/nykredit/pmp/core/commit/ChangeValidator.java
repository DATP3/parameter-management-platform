package dk.nykredit.pmp.core.commit;

public interface ChangeValidator {
    boolean validateChange(ParameterChange change);
    boolean validateChange(ParameterRevert change);
    boolean validateChange(CommitRevert change);
}

