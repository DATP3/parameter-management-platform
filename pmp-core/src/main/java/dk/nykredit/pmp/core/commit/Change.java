package dk.nykredit.pmp.core.commit;

import java.util.List;

import dk.nykredit.pmp.core.audit_log.ChangeEntity;
import dk.nykredit.pmp.core.commit.exception.CommitException;
import dk.nykredit.pmp.core.util.ChangeVisitor;

public interface Change {
    /**
     * @param commitDirector The commit director is used to get the parameter
     *                       service so it can be used to change parameters, as well
     *                       as the audit log to find the change to be reverted in
     *                       case the change is a revert.
     * @return A list of change entities representing the changes made to parameters
     *         by the change.
     */
    List<ChangeEntity> apply(CommitDirector commitDirector) throws CommitException;

    void acceptVisitor(ChangeVisitor changeVisitor);
}
