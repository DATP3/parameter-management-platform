package dk.nykredit.pmp.core.commit;

import dk.nykredit.pmp.core.commit.exception.CommitException;

public interface Change {
    void apply(CommitDirector commitDirector) throws CommitException;

    void undo(CommitDirector commitDirector);

    String getPmpRoot(); 
    String getType();
}
