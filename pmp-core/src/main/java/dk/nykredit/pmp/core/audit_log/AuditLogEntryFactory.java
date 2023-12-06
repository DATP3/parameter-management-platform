package dk.nykredit.pmp.core.audit_log;

import dk.nykredit.pmp.core.commit.Change;
import dk.nykredit.pmp.core.commit.Commit;
import dk.nykredit.pmp.core.commit.CommitRevert;
import dk.nykredit.pmp.core.commit.ParameterChange;
import dk.nykredit.pmp.core.commit.ParameterRevert;

import java.util.ArrayList;
import java.util.List;

public class AuditLogEntryFactory {

    public AuditLogEntry createAuditLogEntry(Commit commit) {
        AuditLogEntry entry = new AuditLogEntry();

        entry.setCommitId(commit.getCommitHash());
        entry.setPushDate(commit.getPushDate());
        entry.setUser(commit.getUser());
        entry.setMessage(commit.getMessage());

        List<ChangeEntity> changesEntities = new ArrayList<>();
        for (Change change : commit.getAppliedChanges()) {
            if (change instanceof ParameterChange)
                changesEntities.add(new ParameterChangeEntityFactory(entry).createChangeEntity(change));

            if (change instanceof ParameterRevert)
                changesEntities.add(new ParameterRevertEntityFactory(entry).createChangeEntity(change));

            if (change instanceof CommitRevert)
                changesEntities.add(new CommitRevertChangeEntityFactory(entry).createChangeEntity(change));
        }

        entry.setChanges(changesEntities);

        return entry;
    }

}
