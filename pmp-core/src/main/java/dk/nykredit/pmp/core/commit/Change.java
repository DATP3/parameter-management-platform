package dk.nykredit.pmp.core.commit;

import java.util.List;

import dk.nykredit.pmp.core.audit_log.ChangeEntity;
import dk.nykredit.pmp.core.commit.exception.CommitException;

public interface Change {
    List<ChangeEntity> apply(CommitDirector commitDirector) throws CommitException;

    void undo(CommitDirector commitDirector);
}
