package dk.nykredit.pmp.core.audit_log;

import java.util.List;

import dk.nykredit.pmp.core.commit.Commit;

public interface AuditLog {
    /**
     * Persists/logs the commit and all its appliedChanges in the audit log.
     * 
     * @param commit the commit to log
     */
    void logCommit(Commit commit);

    /**
     * Finds the commit that had the n'th latest change to a parameter where n is
     * denoted by the offset.
     * 
     * @param name   The name of the parameter
     * @param offset The index of which element in the list after the query to
     *               return. Each increment of the offset gives the next latest
     *               entry, where 0 gets the latest.
     * @return The n'th latest commit to the parameter given the offset
     */
    AuditLogEntry getLatestCommitToParameter(String name, int offset);

    /**
     * Finds the commit that had the latest change to a parameter.
     * 
     * @param name The name of the parameter.
     * @return The latest commit that effected the parameter.
     */
    AuditLogEntry getLatestCommitToParameter(String name);

    /**
     * Finds the commit that had the latest change to a parameter, where the change
     * was not a revert.
     * 
     * @param name The name of the parameter.
     * @return The latest commit that effected the parameter where the change was
     *         not a revert.
     */
    AuditLogEntry getLatestNotRevertedChangeToParameter(String name);

    /**
     * Returns the audit log entry with the given commit hash.
     * 
     * @param commitHash the commit hash of the entry to return
     * @return the audit log entry with the given commit hash
     */
    AuditLogEntry getAuditLogEntry(long commitHash);

    /**
     * Returns a list of all audit log entries.
     * 
     * @return a list of all audit log entries
     */
    List<AuditLogEntry> getEntries();
}
