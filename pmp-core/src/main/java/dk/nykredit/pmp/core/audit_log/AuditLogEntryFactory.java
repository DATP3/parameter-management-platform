package dk.nykredit.pmp.core.audit_log;

import dk.nykredit.pmp.core.commit.Commit;

public class AuditLogEntryFactory {
    public AuditLogEntry createAuditLogEntry(Commit commit) {
        AuditLogEntry entry = new AuditLogEntry();

        // Sets the coresponding fields in the AuditLogEntry object
        entry.setCommitId(commit.getCommitHash());
        entry.setPushDate(commit.getPushDate());
        entry.setUser(commit.getUser());
        entry.setMessage(commit.getMessage());
        entry.setAffectedServices(String.join(",", commit.getAffectedServices()));

        // Sets the commit of each ChangeEntiry in the list of applied changes to the
        // AuditLogEntry. This is to link the change with the commit it is from when
        // stored.
        for (ChangeEntity changeEntity : commit.getAppliedChanges()) {
            changeEntity.setCommit(entry);
        }

        // Adds the applied changes of the commit to the ChangeEntity list of the
        // AuditLogEntry
        entry.setChanges(commit.getAppliedChanges());

        return entry;
    }
}
