package dk.nykredit.pmp.core.audit_log;

import dk.nykredit.pmp.core.commit.Commit;

public class AuditLogEntryFactory {

    public AuditLogEntry createAuditLogEntry(Commit commit) {
        AuditLogEntry entry = new AuditLogEntry();

        entry.setCommitId(commit.getCommitHash());
        entry.setPushDate(commit.getPushDate());
        entry.setUser(commit.getUser());
        entry.setMessage(commit.getMessage());
        entry.setAffectedServices(String.join(",", commit.getAffectedServices()));

        for (ChangeEntity changeEntity : commit.getAppliedChanges()) {
            changeEntity.setCommit(entry);
        }

        entry.setChanges(commit.getAppliedChanges());

        return entry;
    }

}
