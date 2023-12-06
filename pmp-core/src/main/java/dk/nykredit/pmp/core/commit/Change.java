package dk.nykredit.pmp.core.commit;

import java.util.List;

import dk.nykredit.pmp.core.audit_log.ChangeType;
import dk.nykredit.pmp.core.commit.exception.CommitException;

public interface Change {
    List<PersistableChange> apply(CommitDirector commitDirector) throws CommitException;

    void undo(CommitDirector commitDirector);

    ChangeType getChangeType();
}
