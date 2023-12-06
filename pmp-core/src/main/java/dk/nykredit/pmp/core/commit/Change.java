package dk.nykredit.pmp.core.commit;

import java.util.List;
import dk.nykredit.pmp.core.commit.exception.CommitException;

public interface Change {
    List<Change> apply(CommitDirector commitDirector) throws CommitException;

    void undo(CommitDirector commitDirector);
}
