package dk.nykredit.pmp.core.commit;

import dk.nykredit.pmp.core.audit_log.AuditLogEntry;
import dk.nykredit.pmp.core.database.setup.H2StartDatabase;
import dk.nykredit.pmp.core.service.ParameterService;
import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class TestCommitRevert extends H2StartDatabase {
    private WeldContainer container;
    private CommitDirector commitDirector;

    /**
     * Weld container is initialized and a commit director is created in dependency
     * injection context.
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
    }

    /**
     * Reset test environment.
     */
    @AfterEach
    public void after() {
        container.shutdown();
    }

    /**
     * Test that the latest commit is reverted correctly.
     */
    @Test
    void testCommitRevertOnLatestCommit() {
        // This throws a bunch of exceptions to the console but the test seems to be
        // working fine.

        // Get parameter service from commit director.
        ParameterService parameterService = commitDirector.getParameterService();

        // Starting a transaction in test clears the database, so we manually start one
        // at the beginning of the test, to avoid starting a new one every time we
        // persist a change.
        parameterService.getRepository().startTransaction();

        // Setup commit.
        Commit commit = new Commit();
        commit.setUser("author");
        commit.setMessage("test commit");
        commit.setPushDate(LocalDateTime.now());
        commit.setAffectedServices(List.of("service1"));

        // Setup parameter change.
        List<Change> changes = new ArrayList<>();
        ParameterChange change = new ParameterChange();
        change.setName("test1");
        change.setNewValue("data2");
        change.setOldValue("data1");
        change.setType("String");
        changes.add(change);

        // Add changes to commit.
        commit.setChanges(changes);

        // Apply commit.
        commitDirector.apply(commit);

        // setup revert commit.
        CommitRevert commitRevert = new CommitRevert();
        commitRevert.setCommitHash(commit.getCommitHash());

        Commit commit2 = new Commit();
        commit2.setPushDate(LocalDateTime.now());
        commit2.setUser("test1");
        commit2.setMessage("revert commit 1");
        commit2.setAffectedServices(List.of("service1"));

        // Add commit revert to changes.
        List<Change> changes2 = new ArrayList<>();
        changes2.add(commitRevert);

        // Add changes to revert commit.
        commit2.setChanges(changes2);

        // Apply revert commit.
        commitDirector.apply(commit2);

        // Stop transaction
        parameterService.getRepository().endTransaction();

        // Check that the parameter change is reverted correctly.
        assertEquals("data1", parameterService.findParameterByName("test1"));
        assertEquals(5, parameterService.<Integer>findParameterByName("test2"));
    }

    /**
     * Test that parameters that have been changed after the commit that is being
     * reverted are not reverted.
     */
    @Test
    void testDontRevertParametersWithNewerChanges() {
        // This throws a bunch of exceptions to the console but the test seems to be
        // working fine.

        // Get parameter service from commit director.
        ParameterService parameterService = commitDirector.getParameterService();

        // Starting a transaction in test clears the database, so we manually start one
        // at the beginning of the test, to avoid starting a new one every time we
        // persist a change.
        parameterService.getRepository().startTransaction();

        // Setup commit.
        Commit commit = new Commit();
        commit.setUser("author");
        commit.setMessage("test commit asd");
        commit.setPushDate(LocalDateTime.now());
        commit.setAffectedServices(List.of("service1"));

        // Setup two parameter changes and add to commit.
        {
            List<Change> changes = new ArrayList<>();
            ParameterChange change = new ParameterChange();
            change.setName("test1");
            change.setNewValue("data2");
            change.setOldValue("data1");
            change.setType("String");
            changes.add(change);

            ParameterChange change2 = new ParameterChange();
            change2.setName("test2");
            change2.setNewValue("6");
            change2.setOldValue("5");
            change2.setType("Integer");
            changes.add(change2);

            commit.setChanges(changes);
        }

        // Apply commit.
        commitDirector.apply(commit);

        // setup second commit.
        Commit commit2 = new Commit();
        commit2.setUser("author");
        commit2.setMessage("test commit 2");
        commit2.setPushDate(LocalDateTime.now());
        commit2.setAffectedServices(List.of("service1"));

        // Setup parameter change and add to second commit.
        {
            List<Change> changes = new ArrayList<>();
            ParameterChange change = new ParameterChange();
            change.setName("test1");
            change.setNewValue("data3");
            change.setOldValue("data2");
            change.setType("String");
            changes.add(change);

            commit2.setChanges(changes);
        }

        // Apply second commit.
        commitDirector.apply(commit2);

        // Setup revert of first commit.
        CommitRevert commitRevert = new CommitRevert();
        commitRevert.setCommitHash(commit.getCommitHash());

        Commit revertCommit = new Commit();
        revertCommit.setPushDate(LocalDateTime.now());
        revertCommit.setUser("test1");
        revertCommit.setMessage("revert commit 1");
        revertCommit.setAffectedServices(List.of("service1"));

        // Add commit revert to changes.
        List<Change> changes2 = new ArrayList<>();
        changes2.add(commitRevert);

        // Add changes to revert commit.
        revertCommit.setChanges(changes2);

        // Apply revert commit.
        commitDirector.apply(revertCommit);

        // Stop transaction.
        parameterService.getRepository().endTransaction();

        // Check that the parameter change is not reverted as the parameter changed in
        // the first commit has been changed again in the second commit.
        // The second parameter is still reverted as its history is not changed since
        // the firs commit.
        assertEquals("data3", parameterService.findParameterByName("test1"));
        assertEquals(5, parameterService.<Integer>findParameterByName("test2"));
    }

    /**
     * Test that reverting a commit revert gets the initial commit that was made.
     */
    @Test
    void testRevertCommitRevert() {

        // Get parameter service from commit director.
        ParameterService parameterService = commitDirector.getParameterService();

        // Starting a transaction in test clears the database, so we manually start one
        // at the beginning of the test, to avoid starting a new one every time we
        // persist a change.
        parameterService.getRepository().startTransaction();

        // Setup commit.
        Commit commit1 = new Commit();
        String expectedValueAfterTest = "data2";
        Service service = new Service("service1", "service1address", "prod");
        Change change1 = new ParameterChange("test1", "String", "data1", expectedValueAfterTest, service, "id1");
        List<Change> changes1 = new ArrayList<>();
        changes1.add(change1);
        commit1.setChanges(changes1);
        commit1.setPushDate(LocalDateTime.now());
        commit1.setUser("user 1");
        commit1.setMessage("commit 1");
        commit1.setAffectedServices(List.of("service1"));

        // Apply commit.
        commitDirector.apply(commit1);

        // Setup revert commit.
        Commit commit2 = new Commit();
        CommitRevert CommitRevert1 = new CommitRevert();
        CommitRevert1.setCommitHash(commit1.getCommitHash());
        List<Change> changesRevert1 = new ArrayList<>();
        changesRevert1.add(CommitRevert1);
        commit2.setChanges(changesRevert1);
        commit2.setPushDate(LocalDateTime.now());
        commit2.setUser("user 1");
        commit2.setMessage("revert commit 1");
        commit2.setAffectedServices(List.of("service1"));

        // Apply revert commit.
        commitDirector.apply(commit2);

        // As the commit has been reverted the parameter should be back to its original
        // value and not the value from the first commit.
        assertNotEquals(expectedValueAfterTest, parameterService.findParameterByName("test1"));

        // Setup revert of revert commit.
        Commit commit3 = new Commit();
        CommitRevert commitRevert2 = new CommitRevert();
        commitRevert2.setCommitHash(commit2.getCommitHash());
        List<Change> changesRevert2 = new ArrayList<>();
        changesRevert2.add(commitRevert2);
        commit3.setChanges(changesRevert2);
        commit3.setPushDate(LocalDateTime.now());
        commit3.setUser("user 1");
        commit3.setMessage("revert commit 2");
        commit3.setAffectedServices(List.of("service1"));

        // Apply revert of revert commit.
        commitDirector.apply(commit3);

        // As the revert of the revert commit has been applied the parameter should be
        // equal to the parameter value from the first commit.
        assertEquals(expectedValueAfterTest, parameterService.findParameterByName("test1"));

        parameterService.getRepository().endTransaction();
    }

    /**
     * 
     */
    @Test
    public void testDontRevertParametersWithNewerReverts() {
        // This throws a bunch of exceptions to the console but the test seems to be
        // working fine.
        ParameterService parameterService = commitDirector.getParameterService();

        // Starting a transaction in test clears the database, so we manually start one
        // at the beginning of the test, to avoid starting a new one every time we
        // persist a change.
        parameterService.getRepository().startTransaction();

        // Setup commit with two parameter changes.
        Commit commit1 = new Commit();
        commit1.setUser("author");
        commit1.setMessage("change both parameters");
        commit1.setPushDate(LocalDateTime.now());
        commit1.setAffectedServices(List.of("service1"));

        {
            List<Change> changes = new ArrayList<>();
            ParameterChange change = new ParameterChange();
            change.setName("test1");
            change.setNewValue("data2");
            change.setOldValue("data1");
            change.setType("String");
            changes.add(change);

            ParameterChange change2 = new ParameterChange();
            change2.setName("test2");
            change2.setNewValue("10");
            change2.setOldValue("5");
            change2.setType("Integer");
            changes.add(change2);

            commit1.setChanges(changes);
        }

        // Apply commit.
        commitDirector.apply(commit1);

        // Setup revert of one of the parameter changes.
        Commit commit2 = new Commit();
        commit2.setUser("author");
        commit2.setMessage("revert test2");
        commit2.setPushDate(LocalDateTime.now());
        commit2.setAffectedServices(List.of("service1"));

        {
            List<Change> changes = new ArrayList<>();
            ParameterRevert change = new ParameterRevert();
            change.setParameterName("test2");
            change.setCommitHash(commit1.getCommitHash());
            changes.add(change);

            commit2.setChanges(changes);
        }

        // Apply revert parameter.
        commitDirector.apply(commit2);

        // Setup revert of the initial commit.
        Commit commit3 = new Commit();
        commit3.setUser("author");
        commit3.setMessage("revert commit1");
        commit3.setPushDate(LocalDateTime.now());
        commit3.setAffectedServices(List.of("service1"));

        {
            List<Change> changes = new ArrayList<>();
            CommitRevert change = new CommitRevert();
            change.setCommitHash(commit1.getCommitHash());
            changes.add(change);

            commit3.setChanges(changes);
        }

        // Apply revert commit.
        commitDirector.apply(commit3);

        // Get the parameter values from the service.
        String test1 = parameterService.findParameterByName("test1");
        Integer test2 = parameterService.findParameterByName("test2");

        // Check that both parameters are reverted to their initial values.
        assertEquals("data1", test1);
        assertEquals(5, test2);

        // get the audit log entry for the commit revert.
        AuditLogEntry entry3 = commitDirector.getAuditLog().getAuditLogEntry(commit3.getCommitHash());

        // Check that the commit revert only has one change as the one parameter was
        // reverted before the commit revert.
        assertEquals(entry3.getChanges().size(), 1);
        assertEquals(entry3.getChanges().get(0).getParameterName(), "test1");

        // Stop transaction.
        parameterService.getRepository().endTransaction();
    }

    /**
     * Test that reverting commits one by one is possible.
     */
    @Test
    public void testAllowChainReverts() {
        // This throws a bunch of exceptions to the console but the test seems to be
        // working fine.

        // Get parameter service from commit director.
        ParameterService parameterService = commitDirector.getParameterService();

        // Starting a transaction in test clears the database, so we manually start one
        // at the beginning of the test, to avoid starting a new one every time we
        // persist a change.
        parameterService.getRepository().startTransaction();

        // Make first parameter change to both parameters.
        Commit commit1 = new Commit();
        commit1.setUser("author");
        commit1.setMessage("change both parameters");
        commit1.setPushDate(LocalDateTime.now());
        commit1.setAffectedServices(List.of("service1"));

        {
            List<Change> changes = new ArrayList<>();
            ParameterChange change = new ParameterChange();
            change.setName("test1");
            change.setNewValue("data2");
            change.setOldValue("data1");
            change.setType("String");
            changes.add(change);

            ParameterChange change2 = new ParameterChange();
            change2.setName("test2");
            change2.setNewValue("10");
            change2.setOldValue("5");
            change2.setType("Integer");
            changes.add(change2);

            commit1.setChanges(changes);
        }

        commitDirector.apply(commit1);

        // Make second parameter change to both parameters.
        Commit commit2 = new Commit();
        commit2.setUser("author");
        commit2.setMessage("change both parameters again");
        commit2.setPushDate(LocalDateTime.now());
        commit2.setAffectedServices(List.of("service1"));

        {
            List<Change> changes = new ArrayList<>();
            ParameterChange change = new ParameterChange();
            change.setName("test1");
            change.setNewValue("data3");
            change.setOldValue("data2");
            change.setType("String");
            changes.add(change);

            ParameterChange change2 = new ParameterChange();
            change2.setName("test2");
            change2.setNewValue("15");
            change2.setOldValue("10");
            change2.setType("Integer");
            changes.add(change2);

            commit2.setChanges(changes);
        }

        commitDirector.apply(commit2);

        // Assert change stuck.
        String test1 = parameterService.findParameterByName("test1");
        Integer test2 = parameterService.findParameterByName("test2");

        assertEquals("data3", test1);
        assertEquals(15, test2);

        // Revert second commit.
        Commit commit3 = new Commit();
        commit3.setUser("author");
        commit3.setMessage("revert second commit");
        commit3.setPushDate(LocalDateTime.now());
        commit3.setAffectedServices(List.of("service1"));

        {
            List<Change> changes = new ArrayList<>();
            CommitRevert change = new CommitRevert();
            change.setCommitHash(commit2.getCommitHash());
            changes.add(change);

            commit3.setChanges(changes);
        }

        commitDirector.apply(commit3);

        // Assert first revert applied.
        test1 = parameterService.findParameterByName("test1");
        test2 = parameterService.findParameterByName("test2");

        assertEquals("data2", test1);
        assertEquals(10, test2);

        // Revert first commit.
        Commit commit4 = new Commit();
        commit4.setUser("author");
        commit4.setMessage("revert first commit");
        commit4.setPushDate(LocalDateTime.now());
        commit4.setAffectedServices(List.of("service1"));

        {
            List<Change> changes = new ArrayList<>();
            CommitRevert change = new CommitRevert();
            change.setCommitHash(commit1.getCommitHash());
            changes.add(change);

            commit4.setChanges(changes);
        }

        // Apply revert of first commit.
        commitDirector.apply(commit4);

        // Get parameters from service.
        test1 = parameterService.findParameterByName("test1");
        test2 = parameterService.findParameterByName("test2");

        // Assert that parameters are back to their original values.
        assertEquals("data1", test1);
        assertEquals(5, test2);

        // Stop transaction.
        parameterService.getRepository().endTransaction();
    }

    @Test
    public void testAllowLongerChainReverts() {
        // This throws a bunch of exceptions to the console but the test seems to be
        // working fine.

        // Get parameter service from commit director.
        ParameterService parameterService = commitDirector.getParameterService();

        // Starting a transaction in test clears the database, so we manually start one
        // at the beginning of the test, to avoid starting a new one every time we
        // persist a change.
        parameterService.getRepository().startTransaction();

        // Make first parameter change to both parameters.
        Commit commit1 = new Commit();
        commit1.setUser("author");
        commit1.setMessage("change both parameters");
        commit1.setPushDate(LocalDateTime.now());
        commit1.setAffectedServices(List.of("service1"));

        {
            List<Change> changes = new ArrayList<>();
            ParameterChange change = new ParameterChange();
            change.setName("test1");
            change.setNewValue("data2");
            change.setOldValue("data1");
            change.setType("String");
            changes.add(change);

            ParameterChange change2 = new ParameterChange();
            change2.setName("test2");
            change2.setNewValue("10");
            change2.setOldValue("5");
            change2.setType("Integer");
            changes.add(change2);

            commit1.setChanges(changes);
        }

        // Apply commit.
        commitDirector.apply(commit1);

        // Make second parameter change to both parameters.
        Commit commit2 = new Commit();
        commit2.setUser("author");
        commit2.setMessage("change both parameters again");
        commit2.setPushDate(LocalDateTime.now());
        commit2.setAffectedServices(List.of("service1"));

        {
            List<Change> changes = new ArrayList<>();
            ParameterChange change = new ParameterChange();
            change.setName("test1");
            change.setNewValue("data3");
            change.setOldValue("data2");
            change.setType("String");
            changes.add(change);

            ParameterChange change2 = new ParameterChange();
            change2.setName("test2");
            change2.setNewValue("15");
            change2.setOldValue("10");
            change2.setType("Integer");
            changes.add(change2);

            commit2.setChanges(changes);
        }

        // Apply second commit.
        commitDirector.apply(commit2);

        // Make third parameter change to both parameters.
        Commit commit22 = new Commit();
        commit22.setUser("author");
        commit22.setMessage("change both parameters again");
        commit22.setPushDate(LocalDateTime.now());
        commit22.setAffectedServices(List.of("service1"));

        {
            List<Change> changes = new ArrayList<>();
            ParameterChange change = new ParameterChange();
            change.setName("test1");
            change.setNewValue("data4");
            change.setOldValue("data3");
            change.setType("String");
            changes.add(change);

            ParameterChange change2 = new ParameterChange();
            change2.setName("test2");
            change2.setNewValue("20");
            change2.setOldValue("15");
            change2.setType("Integer");
            changes.add(change2);

            commit22.setChanges(changes);
        }

        // Apply third commit.
        commitDirector.apply(commit22);

        // Assert change stuck.
        String test1 = parameterService.findParameterByName("test1");
        Integer test2 = parameterService.findParameterByName("test2");

        assertEquals("data4", test1);
        assertEquals(20, test2);

        // Revert third commit.
        Commit commit32 = new Commit();
        commit32.setUser("author");
        commit32.setMessage("revert second commit");
        commit32.setPushDate(LocalDateTime.now());
        commit32.setAffectedServices(List.of("service1"));

        {
            List<Change> changes = new ArrayList<>();
            CommitRevert change = new CommitRevert();
            change.setCommitHash(commit22.getCommitHash());
            changes.add(change);

            commit32.setChanges(changes);
        }

        // Apply revert of third commit.
        commitDirector.apply(commit32);

        // Get parameters from service.
        test1 = parameterService.findParameterByName("test1");
        test2 = parameterService.findParameterByName("test2");

        // Assert revert of third commit is applied.
        assertEquals("data3", test1);
        assertEquals(15, test2);

        // Revert second commit.
        Commit commit3 = new Commit();
        commit3.setUser("author");
        commit3.setMessage("revert second commit");
        commit3.setPushDate(LocalDateTime.now());
        commit3.setAffectedServices(List.of("service1"));

        {
            List<Change> changes = new ArrayList<>();
            CommitRevert change = new CommitRevert();
            change.setCommitHash(commit2.getCommitHash());
            changes.add(change);

            commit3.setChanges(changes);
        }

        commitDirector.apply(commit3);

        // Get parameters from service.
        test1 = parameterService.findParameterByName("test1");
        test2 = parameterService.findParameterByName("test2");

        // Assert revert of second commit is applied.
        assertEquals("data2", test1);
        assertEquals(10, test2);

        // Revert first commit
        Commit commit4 = new Commit();
        commit4.setUser("author");
        commit4.setMessage("revert first commit");
        commit4.setPushDate(LocalDateTime.now());
        commit4.setAffectedServices(List.of("service1"));

        {
            List<Change> changes = new ArrayList<>();
            CommitRevert change = new CommitRevert();
            change.setCommitHash(commit1.getCommitHash());
            changes.add(change);

            commit4.setChanges(changes);
        }

        // Apply revert of first commit.
        commitDirector.apply(commit4);

        // Get parameters from service.
        test1 = parameterService.findParameterByName("test1");
        test2 = parameterService.findParameterByName("test2");

        // Assert revert of first commit is applied.
        assertEquals("data1", test1);
        assertEquals(5, test2);

        // Stop transaction.
        parameterService.getRepository().endTransaction();
    }
}
