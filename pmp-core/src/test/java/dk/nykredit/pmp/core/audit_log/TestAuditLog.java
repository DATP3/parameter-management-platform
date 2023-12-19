package dk.nykredit.pmp.core.audit_log;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dk.nykredit.pmp.core.commit.Change;
import dk.nykredit.pmp.core.commit.Commit;
import dk.nykredit.pmp.core.commit.CommitDirector;
import dk.nykredit.pmp.core.commit.CommitRevert;
import dk.nykredit.pmp.core.commit.ParameterChange;
import dk.nykredit.pmp.core.commit.Service;
import dk.nykredit.pmp.core.database.setup.H2StartDatabase;
import dk.nykredit.pmp.core.service.ParameterService;

public class TestAuditLog extends H2StartDatabase {

    private WeldContainer container;
    private AuditLog auditLog;
    private CommitDirector commitDirector;

    /**
     * Setup the test environment before each test.
     * Weld container is initialized and a commit director and audit log is created
     * in a dependency injection context.
     * Get the parameter service and persist some parameters.
     */
    @BeforeEach
    public void before() {
        Weld weld = new Weld();
        container = weld.initialize();
        commitDirector = container.select(CommitDirector.class).get();
        ParameterService parameterService = commitDirector.getParameterService();
        parameterService.persistParameter("test1", "data1");
        parameterService.persistParameter("test2", 5);

        auditLog = container.select(AuditLog.class).get();
    }

    /**
     * Reset test environment.
     */
    @AfterEach
    public void after() {
        container.shutdown();
    }

    /**
     * Test that the correct commit is saved in the audit log.
     */
    @Test
    void testLogCommit() {

        // Setup commit.
        Commit commit = new Commit();
        commit.setUser("author");
        commit.setMessage("test commit");
        commit.setPushDate(LocalDateTime.now());
        commit.setAffectedServices(List.of("service1"));
        // Setup service.
        Service service = new Service("service1", "service1address", "prod");

        // Setup changes.
        Change c1 = new ParameterChange("test1", "String", "data1", "data2", service, "id1");
        Change c2 = new ParameterChange("test2", "Integer", "5", "10", service, "id2");

        // Add changes to commit.
        List<Change> changes = new ArrayList<>();
        changes.add(c1);
        changes.add(c2);
        commit.setChanges(changes);

        // apply commit.
        commitDirector.apply(commit);

        // Get commit from audit log based on hash.
        AuditLogEntry commitEntry2 = auditLog.getAuditLogEntry(commit.getCommitHash());
        // Create audit log entry from commit.
        AuditLogEntry commitEntry = new AuditLogEntryFactory().createAuditLogEntry(commit);

        // Assert that the two entries are equal.
        assertEquals(commitEntry, commitEntry2);
    }

    /**
     * Test that commit revert is logged correctly.
     */
    @Test
    void testLogCommitRevert() {
        // Setup database.
        ParameterService parameterService = commitDirector.getParameterService();
        parameterService.getRepository().startTransaction();

        // Setup commit.
        Commit commit = new Commit();
        commit.setUser("author");
        commit.setMessage("test commit");
        commit.setPushDate(LocalDateTime.now());
        commit.setAffectedServices(List.of("service1"));
        // Setup service.
        Service service = new Service("service1", "service1address", "prod");

        // Setup changes.
        Change c1 = new ParameterChange("test1", "String", "data1", "data2", service, "id1");
        Change c2 = new ParameterChange("test2", "Integer", "5", "10", service, "id2");

        // Add changes to commit.
        List<Change> changes = new ArrayList<>();
        changes.add(c1);
        changes.add(c2);
        commit.setChanges(changes);

        // apply commit.
        commitDirector.apply(commit);

        // Get applied commit to revert.
        CommitRevert commitRevert = new CommitRevert();
        commitRevert.setCommitHash(commit.getCommitHash());

        // Add commit revert to be reverted.
        List<Change> changes2 = new ArrayList<>();
        changes2.add(commitRevert);

        // Setup revert commit.
        Commit commit2 = new Commit();
        commit2.setChanges(changes2);
        commit2.setPushDate(LocalDateTime.now());
        commit2.setUser("user 1");
        commit2.setMessage("revert commit 1");
        commit2.setAffectedServices(List.of("service1"));

        // Apply revert commit.
        commitDirector.apply(commit2);

        // Get reverted commit from audit log.
        AuditLogEntry commitEntry2 = auditLog.getAuditLogEntry(commit2.getCommitHash());

        // Assert that the reverted commit is logged correctly.
        assertEquals("data1", commitEntry2.getChanges().get(0).getNewValue());

        // Close database.
        parameterService.getRepository().endTransaction();
    }
}
