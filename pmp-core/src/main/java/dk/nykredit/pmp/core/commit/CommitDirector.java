package dk.nykredit.pmp.core.commit;

import javax.inject.Inject;

import dk.nykredit.pmp.core.audit_log.AuditLog;
import dk.nykredit.pmp.core.commit.exception.CommitException;
import dk.nykredit.pmp.core.service.ParameterService;

public class CommitDirector {
	@Inject
	private ParameterService parameterService;

	@Inject
	private AuditLog auditLog;

	public void apply(Commit commit) throws CommitException {
		// If applying the commit fails, a `CommitException` will be thrown,
		// and `logCommit` will not be called
		commit.apply(parameterService);
		auditLog.logCommit(commit);
	}
}
