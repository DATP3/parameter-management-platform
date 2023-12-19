package dk.nykredit.pmp.core.audit_log;

import dk.nykredit.pmp.core.commit.Commit;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
@Transactional
public class AuditLogImpl implements AuditLog {

    @Inject
    private EntityManager entityManager;

    @Inject
    private AuditLogEntryFactory auditLogEntryFactory;

    @Override
    public void logCommit(Commit commit) {
        // Creates the audit log entry from the commit using the factory
        AuditLogEntry entry = auditLogEntryFactory.createAuditLogEntry(commit);

        // Begins transaction to make sure database works as expected
        entityManager.getTransaction().begin();
        try {
            // Saves the entry as well as all its changes
            entityManager.persist(entry);
            for (ChangeEntity change : entry.getChangeEntities()) {
                entityManager.persist(change);
            }
        } finally {
            entityManager.getTransaction().commit();
        }
    }

    @Override
    public AuditLogEntry getAuditLogEntry(long commitHash) {
        return entityManager.find(AuditLogEntry.class, commitHash);
    }

    @Override
    public List<AuditLogEntry> getEntries() {
        return entityManager.createQuery("SELECT e FROM AuditLogEntry e", AuditLogEntry.class)
                .getResultList();
    }

    @Override
    public AuditLogEntry getLatestCommitToParameter(String name, int offset) throws IndexOutOfBoundsException {
        // List<AuditLogEntry> entries = getLatestCommitsToParameter(name, 1);
        List<AuditLogEntry> auditLogEntries = entityManager.createQuery(
                "SELECT e FROM AuditLogEntry e, ChangeEntity p WHERE p.parameterName = :name AND p.commit.commitId = e.commitId ORDER BY e.pushDate DESC",
                AuditLogEntry.class)
                .setParameter("name", name)
                .getResultList();

        return auditLogEntries.get(offset);
    }

    @Override
    public AuditLogEntry getLatestCommitToParameter(String name) {
        // List<AuditLogEntry> entries = getLatestCommitsToParameter(name, 1);
        List<AuditLogEntry> auditLogEntries = entityManager.createQuery(
                "SELECT e FROM AuditLogEntry e, ChangeEntity p WHERE p.parameterName = :name AND p.commit.commitId = e.commitId ORDER BY e.pushDate DESC",
                AuditLogEntry.class)
                .setParameter("name", name)
                .getResultList();

        return auditLogEntries.get(0);
    }

    public AuditLogEntry getLatestNotRevertedChangeToParameter(String name) {
        int offset = 0;

        List<Long> revertedRefs = new ArrayList<>();

        while (true) {
            AuditLogEntry auditLogEntry = null;

            // Tries to find the latest commit to the parameter. If there is no commits that
            // effected the parameter, null is returned.
            try {
                auditLogEntry = getLatestCommitToParameter(name, offset);
            } catch (IndexOutOfBoundsException e) {
                return null;
            }

            // finds the latest change to the parameter from the auditLogEntry.
            ChangeEntity lastChange = null;
            for (ChangeEntity changeEntity : auditLogEntry.getChangeEntities()) {
                if (changeEntity.getParameterName().equals(name)) {
                    lastChange = changeEntity;
                    break;
                }
            }

            // If there is no change to the parameter, null is returned.
            if (lastChange == null) {
                return null;
            }

            // If the latest change was not a parameter change, the offset is incremented.
            // This makes the next iteration of the while loop look for the next latest
            // change.
            if (lastChange.getChangeType() != ChangeType.PARAMETER_CHANGE) {
                revertedRefs.add(lastChange.getCommitRevertRef());
                offset++;
                continue;
            }

            // If the latest change was a parameter change form a commit not preveusly
            // looked at, the auditLogEntry is returned.
            if (!revertedRefs.contains(auditLogEntry.getCommitId())) {
                return auditLogEntry;
            }

            offset++;
        }
    }
}
