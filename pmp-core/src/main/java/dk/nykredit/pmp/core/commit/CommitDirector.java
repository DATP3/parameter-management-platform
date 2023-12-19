package dk.nykredit.pmp.core.commit;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import dk.nykredit.pmp.core.audit_log.AuditLog;
import dk.nykredit.pmp.core.commit.exception.CommitException;
import dk.nykredit.pmp.core.service.ParameterService;
import lombok.Getter;

@Getter
public class CommitDirector {
    @Inject
    private ParameterService parameterService;

    @Inject
    private AuditLog auditLog;

    @Inject
    private EntityManager entityManager;

    /**
     * Applies a commit and logs the commit in the audit log
     * 
     * @param commit to be applied
     * @throws CommitException If the commit cannot be applied an exception will be
     *                         thrown indicating the error
     */
    public void apply(Commit commit) throws CommitException {
        // If applying the commit fails, a `CommitException` will be thrown,
        // and `logCommit` will not be called
        System.out.println("Applying commit\n\n\n");
        System.out.println(commit);
        System.out.println("\n\n\n");
        commit.apply(this);
        auditLog.logCommit(commit);

    }
}
