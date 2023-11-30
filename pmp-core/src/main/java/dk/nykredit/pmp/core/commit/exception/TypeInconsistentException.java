package dk.nykredit.pmp.core.commit.exception;

/**
 * TypeInconsistentException
 */
public class TypeInconsistentException extends CommitException {

    public TypeInconsistentException(String message, Exception e) {
        super(message, e);
    }

    public TypeInconsistentException(String message) {
        super(message);
    }
}
