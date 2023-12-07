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
    public void testRevertParameterChange() {
        ParameterService parameterService = commitDirector.getParameterService();
        parameterService.getRepository().startTransaction();

        Commit commit = new Commit();
        commit.setUser("author");
        commit.setMessage("test commit");
        commit.setPushDate(LocalDateTime.now());
        commit.setAffectedServices(List.of("service1"));

        Change c1 = new ParameterChange("test1", "String", "data1", "data2");
        Change c2 = new ParameterChange("test2", "Integer", "5", "10");

        List<Change> changes = new ArrayList<>();
        changes.add(c1);
        changes.add(c2);

        commit.setChanges(changes);

        commitDirector.apply(commit);

        assertEquals("data2", commitDirector.getParameterService().findParameterByName("test1"));
        assertEquals(10, commitDirector.getParameterService().<Integer>findParameterByName("test2"));

        Commit commit2 = new Commit();
        commit2.setUser("author");
        commit2.setMessage("revert param test1");
        commit2.setPushDate(LocalDateTime.now());
        commit2.setAffectedServices(List.of("service1"));

        Change paramRevert = new ParameterRevert("test1", commit.getCommitHash());
        commit2.setChanges(List.of(paramRevert));

        commitDirector.apply(commit2);

        assertEquals("data1", commitDirector.getParameterService().findParameterByName("test1"));
        assertEquals(10, commitDirector.getParameterService().<Integer>findParameterByName("test2"));

        parameterService.getRepository().endTransaction();
    }
}