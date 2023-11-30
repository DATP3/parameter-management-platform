package dk.nykredit.pmp.core.commit.exception;

public abstract class CommitException extends RuntimeException {

    public CommitException(String message, Exception e) {
        super(message, e);
    }

    public CommitException(String message) {
        super(message);
    }
}
