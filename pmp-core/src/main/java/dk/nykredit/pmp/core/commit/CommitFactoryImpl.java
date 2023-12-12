package dk.nykredit.pmp.core.commit;

import javax.inject.Inject;

import dk.nykredit.pmp.core.audit_log.AuditLog;
import dk.nykredit.pmp.core.remote.json.raw_types.RawChange;
import dk.nykredit.pmp.core.remote.json.raw_types.RawCommit;
import dk.nykredit.pmp.core.util.ChangeValidator;
import dk.nykredit.pmp.core.util.ChangeValidatorFactory;

public class CommitFactoryImpl implements CommitFactory {

    @Inject
    private ChangeFactory changeFactory;

    @Inject
    private ChangeValidatorFactory changeValidatorFactory;

    @Inject
    AuditLog auditLog;

    // Recieve rawCommit
    // Create commit object
    // calculate hash of commit
    // start visitor on each change in the commit
    // discard all changes that failed the visitors validation
    // return commit with only valid changes

    @Override
    public Commit createCommit(RawCommit rawCommit) {
        Commit commit = new Commit();
        commit.setCommitHash(calculateHash(rawCommit));
        commit.setPushDate(rawCommit.getPushDate());
        commit.setUser(rawCommit.getUser());
        commit.setMessage(rawCommit.getMessage());
        commit.setAffectedServices(rawCommit.getAffectedServices());

        ChangeValidator changeValidator = changeValidatorFactory.createChangeValidator();

        for (RawChange change : rawCommit.getChanges()) {
            Change convertedChange = changeFactory.createChange(change);
            convertedChange.acceptVisitor(changeValidator);
        }

        commit.setChanges(changeValidator.getValidatedChanges());
        return commit;
    }

    private int calculateHash(RawCommit rawCommit) {
        return rawCommit.getPushDate().hashCode()
                + rawCommit.getUser().hashCode()
                + rawCommit.getMessage().hashCode()
                + rawCommit.getChanges().stream().mapToInt(RawChange::hashCode).sum();
    }
}
