package dk.nykredit.pmp.core.audit_log;

import java.util.List;

import dk.nykredit.pmp.core.commit.Commit;

public interface AuditLog {
    void logCommit(Commit commit);

    AuditLogEntry getLatestCommitToParameter(String name, int offset);

    AuditLogEntry getLatestCommitToParameter(String name);

    AuditLogEntry getLatestNotRevertedChangeToParameter(String name);

    AuditLogEntry getAuditLogEntry(long commitHash);

    List<AuditLogEntry> getEntries();
}
