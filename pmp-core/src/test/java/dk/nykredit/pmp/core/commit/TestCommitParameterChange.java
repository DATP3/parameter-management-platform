package dk.nykredit.pmp.core.commit;

import dk.nykredit.pmp.core.commit.exception.OldValueInconsistentException;
import dk.nykredit.pmp.core.commit.exception.TypeInconsistentException;
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

import static org.junit.jupiter.api.Assertions.*;

public class TestCommitParameterChange extends H2StartDatabase {

    private WeldContainer container;
    private CommitDirector commitDirector;

    /**
     * Setup the test environment before each test.
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
     * Test that applying a parameter change of type String is applied correctly.
     */
    @Test
    public void testApplyStringParameterChange() {
        // Get parameter service from commit director
        ParameterService parameterService = commitDirector.getParameterService();

        // Setup commit.
        Commit commit = new Commit();
        commit.setUser("author");
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

        // Set changes on commit and apply commit.
        commit.setChanges(changes);
        // Check that no exception is thrown.
        assertDoesNotThrow(() -> commit.apply(commitDirector));

        // Check that the parameter change is applied correctly.
        assertEquals("data2", parameterService.findParameterByName("test1"));
    }

    /**
     * Test that applying a parameter change of type Integer is applied correctly.
     */
    @Test
    public void testApplyIntegerParameterChange() {
        // Get parameter service from commit director.
        ParameterService parameterService = commitDirector.getParameterService();

        // Setup commit.
        Commit commit = new Commit();
        commit.setUser("author");
        commit.setPushDate(LocalDateTime.now());
        commit.setAffectedServices(List.of("service1"));

        // Setup parameter change.
        List<Change> changes = new ArrayList<>();
        ParameterChange change = new ParameterChange();
        change.setName("test2");
        change.setNewValue("6");
        change.setOldValue("5");
        change.setType("Integer");
        changes.add(change);

        // Set changes on commit and apply commit.
        commit.setChanges(changes);
        // Check that no exception is thrown.
        assertDoesNotThrow(() -> commit.apply(commitDirector));

        // Check that the parameter change is applied correctly.
        assertEquals((Integer) 6, parameterService.findParameterByName("test2"));
    }

    /**
     * Test that applying consecutive parameter changes of type Integer is working
     * as expected.
     */
    @Test
    public void testApplyConsecutiveIntegerParameterChange() {
        // Get parameter service from commit director.
        ParameterService parameterService = commitDirector.getParameterService();

        // Setup commit.
        Commit commit = new Commit();
        commit.setUser("author");
        commit.setPushDate(LocalDateTime.now());
        commit.setAffectedServices(List.of("service1"));

        // Setup parameter change.
        List<Change> changes = new ArrayList<>();
        ParameterChange change = new ParameterChange();
        change.setName("test2");
        change.setNewValue("6");
        change.setOldValue("5");
        change.setType("Integer");
        changes.add(change);

        // Set changes on commit and apply commit.
        commit.setChanges(changes);
        assertDoesNotThrow(() -> commit.apply(commitDirector));

        // Check that the parameter change is applied correctly.
        assertEquals((Integer) 6, parameterService.findParameterByName("test2"));

        // Setup second commit.
        Commit commit2 = new Commit();
        commit2.setUser("author");
        commit2.setPushDate(LocalDateTime.now());
        commit2.setAffectedServices(List.of("service1"));

        // Setup second parameter change.
        List<Change> changes2 = new ArrayList<>();
        ParameterChange change2 = new ParameterChange();
        change2.setName("test2");
        change2.setNewValue("7");
        change2.setOldValue("6");
        change2.setType("Integer");
        changes2.add(change2);

        // Set changes on second commit and apply commit.
        commit2.setChanges(changes2);
        // Check that no exception is thrown.
        assertDoesNotThrow(() -> commit2.apply(commitDirector));

        // Check that the second parameter change is applied correctly.
        assertEquals((Integer) 7, parameterService.findParameterByName("test2"));
    }

    /**
     * Test that mismatched type is undone and not applied.
     */
    @Test
    public void testUndoMismatchedType() {
        // get parameter service from commit director.
        ParameterService parameterService = commitDirector.getParameterService();

        // Setup commit.
        Commit commit = new Commit();
        commit.setUser("author");
        commit.setPushDate(LocalDateTime.now());

        // Setup parameter change.
        List<Change> changes = new ArrayList<>();
        ParameterChange change = new ParameterChange();
        change.setName("test2");
        change.setNewValue("6");
        change.setOldValue("5");
        change.setType("String");
        changes.add(change);

        // Set changes on commit and apply commit.
        commit.setChanges(changes);
        // Check that inconsistent type exception is thrown.
        assertThrows(TypeInconsistentException.class, () -> commit.apply(commitDirector));

        // Check that the parameter change is not applied.
        assertEquals((Integer) 5, parameterService.findParameterByName("test2"));
    }

    /**
     * Test that mismatched old value is undone and not applied.
     */
    @Test
    public void testUndoMismatchedOldValue() {
        // Get parameter service from commit director.
        ParameterService parameterService = commitDirector.getParameterService();

        // Setup commit.
        Commit commit = new Commit();
        commit.setUser("author");
        commit.setPushDate(LocalDateTime.now());

        // Setup parameter change.
        List<Change> changes = new ArrayList<>();
        ParameterChange change = new ParameterChange();
        change.setName("test2");
        change.setNewValue("6");
        change.setOldValue("4");
        change.setType("Integer");
        changes.add(change);

        // Set changes on commit and apply commit.
        commit.setChanges(changes);
        // Check that inconsistent old value exception is thrown.
        assertThrows(OldValueInconsistentException.class, () -> commit.apply(commitDirector));

        // Check that the parameter change is not applied.
        assertEquals((Integer) 5, parameterService.findParameterByName("test2"));
    }

    /**
     * Test that undoing multiple values is working as expected if an error is
     * thrown.
     */
    @Test
    public void testUndoMultipleValues() {
        // Get parameter service from commit director.
        ParameterService parameterService = commitDirector.getParameterService();

        // Setup commit.
        Commit commit = new Commit();
        commit.setUser("author");
        commit.setPushDate(LocalDateTime.now());
        commit.setAffectedServices(List.of("service1"));

        // Setup parameter change.
        List<Change> changes = new ArrayList<>();
        ParameterChange change1 = new ParameterChange();
        change1.setName("test1");
        change1.setNewValue("data2");
        change1.setOldValue("data1");
        change1.setType("String");
        changes.add(change1);

        // Setup second parameter change with mismatched old value.
        ParameterChange change2 = new ParameterChange();
        change2.setName("test2");
        change2.setNewValue("6");
        change2.setOldValue("4");
        change2.setType("Integer");
        changes.add(change2);

        // Set changes on commit and apply commit.
        commit.setChanges(changes);
        // Check that inconsistent old value exception is thrown.
        assertThrows(OldValueInconsistentException.class, () -> commit.apply(commitDirector));

        // Check that the parameter changes are not applied.
        assertEquals((Integer) 5, parameterService.findParameterByName("test2"));
        assertEquals("data1", parameterService.findParameterByName("test1"));
    }
}
