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
        AuditLogEntry entry = auditLogEntryFactory.createAuditLogEntry(commit);
        entityManager.getTransaction().begin();
        try {
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
            try {
                auditLogEntry = getLatestCommitToParameter(name, offset);
            } catch (IndexOutOfBoundsException e) {
                return null;
            }

            ChangeEntity lastChange = null;
            for (ChangeEntity changeEntity : auditLogEntry.getChangeEntities()) {
                if (changeEntity.getParameterName().equals(name)) {
                    lastChange = changeEntity;
                    break;
                }
            }

            if (lastChange == null) {
                return null;
            }

            if (lastChange.getChangeType() != ChangeType.PARAMETER_CHANGE) {
                revertedRefs.add(lastChange.getCommitRevertRef());
                offset++;
                continue;
            }

            if (!revertedRefs.contains(auditLogEntry.getCommitId())) {
                return auditLogEntry;
            }

            offset++;
        }
    }

}
