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

    @BeforeEach
    public void before() {
        Weld weld = new Weld();
        container = weld.initialize();
        commitDirector = container.select(CommitDirector.class).get();

        ParameterService parameterService = commitDirector.getParameterService();
        parameterService.persistParameter("test1", "data1");
        parameterService.persistParameter("test2", 5);
    }

    @AfterEach
    public void after() {
        container.shutdown();
    }

    @Test
    void testCommitRevertOnLatestCommit() {
        // This throws a bunch of exceptions to the console but the test seems to be
        // working fine
        ParameterService parameterService = commitDirector.getParameterService();

        parameterService.getRepository().startTransaction();

        Commit commit = new Commit();
        commit.setUser("author");
        commit.setMessage("test commit");
        commit.setPushDate(LocalDateTime.now());
        commit.setAffectedServices(List.of("service1"));

        List<Change> changes = new ArrayList<>();
        ParameterChange change = new ParameterChange();
        change.setName("test1");
        change.setNewValue("data2");
        change.setOldValue("data1");
        change.setType("String");
        changes.add(change);

        commit.setChanges(changes);

        commitDirector.apply(commit);

        CommitRevert commitRevert = new CommitRevert();
        commitRevert.setCommitHash(commit.getCommitHash());

        Commit commit2 = new Commit();
        commit2.setPushDate(LocalDateTime.now());
        commit2.setUser("test1");
        commit2.setMessage("revert commit 1");
        commit2.setAffectedServices(List.of("service1"));

        List<Change> changes2 = new ArrayList<>();
        changes2.add(commitRevert);

        commit2.setChanges(changes2);
        commitDirector.apply(commit2);

        parameterService.getRepository().endTransaction();

        assertEquals("data1", parameterService.findParameterByName("test1"));
        assertEquals(5, parameterService.<Integer>findParameterByName("test2"));
    }

    @Test
    void testDontRevertParametersWithNewerChanges() {
        // This throws a bunch of exceptions to the console but the test seems to be
        // working fine
        ParameterService parameterService = commitDirector.getParameterService();

        parameterService.getRepository().startTransaction();

        Commit commit = new Commit();
        commit.setUser("author");
        commit.setMessage("test commit asd");
        commit.setPushDate(LocalDateTime.now());
        commit.setAffectedServices(List.of("service1"));

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

        commitDirector.apply(commit);

        Commit commit2 = new Commit();
        commit2.setUser("author");
        commit2.setMessage("test commit 2");
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

            commit2.setChanges(changes);
        }

        commitDirector.apply(commit2);

        CommitRevert commitRevert = new CommitRevert();
        commitRevert.setCommitHash(commit.getCommitHash());

        Commit revertCommit = new Commit();
        revertCommit.setPushDate(LocalDateTime.now());
        revertCommit.setUser("test1");
        revertCommit.setMessage("revert commit 1");
        revertCommit.setAffectedServices(List.of("service1"));

        List<Change> changes2 = new ArrayList<>();
        changes2.add(commitRevert);

        revertCommit.setChanges(changes2);
        commitDirector.apply(revertCommit);

        parameterService.getRepository().endTransaction();

        assertEquals("data3", parameterService.findParameterByName("test1"));
        assertEquals(5, parameterService.<Integer>findParameterByName("test2"));
    }

    @Test
    void testRevertCommitRevert() {

        ParameterService parameterService = commitDirector.getParameterService();
        parameterService.getRepository().startTransaction();

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

        commitDirector.apply(commit1);

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

        commitDirector.apply(commit2);

        assertNotEquals(expectedValueAfterTest, parameterService.findParameterByName("test1"));

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

        commitDirector.apply(commit3);

        assertEquals(expectedValueAfterTest, parameterService.findParameterByName("test1"));

        parameterService.getRepository().endTransaction();
    }

    @Test
    public void testDontRevertParametersWithNewerReverts() {
        // This throws a bunch of exceptions to the console but the test seems to be
        // working fine
        ParameterService parameterService = commitDirector.getParameterService();

        parameterService.getRepository().startTransaction();

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

        commitDirector.apply(commit2);

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

        commitDirector.apply(commit3);

        String test1 = parameterService.findParameterByName("test1");
        Integer test2 = parameterService.findParameterByName("test2");

        assertEquals("data1", test1);
        assertEquals(5, test2);

        AuditLogEntry entry3 = commitDirector.getAuditLog().getAuditLogEntry(commit3.getCommitHash());

        assertEquals(entry3.getChanges().size(), 1);
        assertEquals(entry3.getChanges().get(0).getParameterName(), "test1");

        parameterService.getRepository().endTransaction();
    }

    @Test
    public void testAllowChainReverts() {
        // This throws a bunch of exceptions to the console but the test seems to be
        // working fine
        ParameterService parameterService = commitDirector.getParameterService();

        parameterService.getRepository().startTransaction();

        // Make first parameter change to both parameters
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

        // Make second parameter change to both parameters
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

        // Assert change stuck
        String test1 = parameterService.findParameterByName("test1");
        Integer test2 = parameterService.findParameterByName("test2");

        assertEquals("data3", test1);
        assertEquals(15, test2);

        // Revert second commit
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

        // Assert first revert applied
        test1 = parameterService.findParameterByName("test1");
        test2 = parameterService.findParameterByName("test2");

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

        commitDirector.apply(commit4);

        test1 = parameterService.findParameterByName("test1");
        test2 = parameterService.findParameterByName("test2");

        assertEquals("data1", test1);
        assertEquals(5, test2);

        // AuditLogEntry entry3 =
        // commitDirector.getAuditLog().getAuditLogEntry(commit4.getCommitHash());

        // assertEquals(entry3.getChanges().size(), 1);
        // assertEquals(entry3.getChanges().get(0).getParameterName(), "test1");

        parameterService.getRepository().endTransaction();
    }

    @Test
    public void testAllowLongerChainReverts() {
        // This throws a bunch of exceptions to the console but the test seems to be
        // working fine
        ParameterService parameterService = commitDirector.getParameterService();

        parameterService.getRepository().startTransaction();

        // Make first parameter change to both parameters
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

        // Make second parameter change to both parameters
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

        // Make third parameter change to both parameters
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

        commitDirector.apply(commit22);

        // Assert change stuck
        String test1 = parameterService.findParameterByName("test1");
        Integer test2 = parameterService.findParameterByName("test2");

        assertEquals("data4", test1);
        assertEquals(20, test2);

        // Revert second commit
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

        commitDirector.apply(commit32);

        // Assert first revert applied
        test1 = parameterService.findParameterByName("test1");
        test2 = parameterService.findParameterByName("test2");

        assertEquals("data3", test1);
        assertEquals(15, test2);

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

        // Assert first revert applied
        test1 = parameterService.findParameterByName("test1");
        test2 = parameterService.findParameterByName("test2");

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

        commitDirector.apply(commit4);

        test1 = parameterService.findParameterByName("test1");
        test2 = parameterService.findParameterByName("test2");

        assertEquals("data1", test1);
        assertEquals(5, test2);

        // AuditLogEntry entry3 =
        // commitDirector.getAuditLog().getAuditLogEntry(commit4.getCommitHash());

        // assertEquals(entry3.getChanges().size(), 1);
        // assertEquals(entry3.getChanges().get(0).getParameterName(), "test1");

        parameterService.getRepository().endTransaction();
    }
}
