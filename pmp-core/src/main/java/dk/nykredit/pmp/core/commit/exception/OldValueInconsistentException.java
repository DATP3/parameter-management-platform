package dk.nykredit.pmp.core.commit.exception;

/**
 * OldValueInconsistentException
 */
public class OldValueInconsistentException extends CommitException {

    public OldValueInconsistentException(String message, Exception e) {
        super(message, e);
    }

    public OldValueInconsistentException(String message) {
        super(message);
    }
}
