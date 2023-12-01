package dk.nykredit.pmp.core.audit_log;

import dk.nykredit.pmp.core.commit.Commit;

import java.util.List;
import java.util.stream.Collectors;

public class AuditLogEntryFactory {

    public AuditLogEntry createAuditLogEntry(Commit commit) {
        ChangeEntityFactory changeEntityFactory = new ChangeEntityFactory();

        AuditLogEntry entry = new AuditLogEntry();

        entry.setCommitId(commit.getCommitHash());
        entry.setPushDate(commit.getPushDate());
        entry.setUser(commit.getUser());
        entry.setMessage(commit.getMessage());

        List<ChangeEntity> changes = commit.getChanges().stream()
                .map((change) -> changeEntityFactory.createChangeEntity(change, entry))
                .collect(Collectors.toList());

        entry.setChanges(changes);

        return entry;
    }

}
