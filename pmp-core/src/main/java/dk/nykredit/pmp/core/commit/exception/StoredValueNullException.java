package dk.nykredit.pmp.core.commit.exception;

/**
 * StoredValueNullException
 */
public class StoredValueNullException extends CommitException {

    public StoredValueNullException(String message, Exception e) {
        super(message, e);
    }

    public StoredValueNullException(String message) {
        super(message);
    }
}
