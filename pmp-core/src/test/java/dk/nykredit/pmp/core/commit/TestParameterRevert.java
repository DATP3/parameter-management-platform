package dk.nykredit.pmp.core.commit;

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

public class TestParameterRevert extends H2StartDatabase {
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
        parameterService.persistParameter("test2", 10);

        // Starting a transaction in test clears the database, so we manually start one
        // at the beginning of the test, to avoid starting a new one every time we
        // persist a change.
        parameterService.getRepository().startTransaction();
    }

    /**
     * Reset test environment.
     */
    @AfterEach
    public void after() {
        ParameterService parameterService = commitDirector.getParameterService();
        parameterService.getRepository().endTransaction();
        container.shutdown();
    }

    /**
     * Test that reverting a parameter change is applied correctly.
     */
    @Test
    public void testRevertParameterChange() {

        // Setup commit.
        Commit appliedCommit = new Commit();
        appliedCommit.setUser("author");
        appliedCommit.setMessage("test commit");
        appliedCommit.setPushDate(LocalDateTime.now());
        appliedCommit.setAffectedServices(List.of("service1"));

        // Setup parameter changes.
        Change c1 = new ParameterChange(
                "test1",
                "String",
                "data1",
                "data2",
                new Service(
                        "service1",
                        "service1Address",
                        "service1Environment"),
                "id1");
        Change c2 = new ParameterChange(
                "test2",
                "Integer",
                "10",
                "5",
                new Service(
                        "service1",
                        "service1Address",
                        "service1Environment"),
                "id2");
        List<Change> changes = new ArrayList<>();
        changes.add(c1);
        changes.add(c2);

        appliedCommit.setChanges(changes);

        // Apply commit consisting of parameter changes.
        commitDirector.apply(appliedCommit);

        // Assert that the parameter changes are applied correctly.
        assertEquals("data2", commitDirector.getParameterService().findParameterByName("test1"));
        assertEquals(5, commitDirector.getParameterService().<Integer>findParameterByName("test2"));

        // Setup revert parameter.
        Commit commit2 = new Commit();
        commit2.setUser("author");
        commit2.setMessage("revert param test2");
        commit2.setPushDate(LocalDateTime.now());
        commit2.setAffectedServices(List.of("service1"));

        Change paramRevert = new ParameterRevert("test2", appliedCommit.getCommitHash());
        commit2.setChanges(List.of(paramRevert));

        // Apply revert parameter change.
        commitDirector.apply(commit2);

        // Assert that the test2 parameter has been reverted correctly and that test1 is
        // unchanged since the commit.
        assertEquals("data2", commitDirector.getParameterService().findParameterByName("test1"));
        assertEquals(10, commitDirector.getParameterService().<Integer>findParameterByName("test2"));
    }
}