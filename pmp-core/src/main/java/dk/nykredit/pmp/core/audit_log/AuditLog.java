package dk.nykredit.pmp.core.audit_log;

import java.util.List;

import dk.nykredit.pmp.core.commit.Commit;

public interface AuditLog {
    void logCommit(Commit commit);

    Commit getCommit(long commitHash) throws IllegalArgumentException;

    List<Commit> getCommits();

    AuditLogEntry getLatestCommitToParameter(String name);

    AuditLogEntry getAuditLogEntry(long commitHash);

    List<AuditLogEntry> getEntries();
}
