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
        parameterService.persistParameter("test2", 10);
        parameterService.getRepository().startTransaction();
    }

    @AfterEach
    public void after() {
        ParameterService parameterService = commitDirector.getParameterService();
        parameterService.getRepository().endTransaction();
        container.shutdown();
    }

    @Test
    public void testRevertParameterChange() {

        Commit appliedCommit = new Commit();
        appliedCommit.setUser("author");
        appliedCommit.setMessage("test commit");
        appliedCommit.setPushDate(LocalDateTime.now());
        appliedCommit.setAffectedServices(List.of("service1"));

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

        commitDirector.apply(appliedCommit);

        assertEquals("data2", commitDirector.getParameterService().findParameterByName("test1"));
        assertEquals(5, commitDirector.getParameterService().<Integer>findParameterByName("test2"));

        Commit commit2 = new Commit();
        commit2.setUser("author");
        commit2.setMessage("revert param test2");
        commit2.setPushDate(LocalDateTime.now());
        commit2.setAffectedServices(List.of("service1"));

        Change paramRevert = new ParameterRevert("test2", appliedCommit.getCommitHash());
        commit2.setChanges(List.of(paramRevert));

        commitDirector.apply(commit2);

        assertEquals("data2", commitDirector.getParameterService().findParameterByName("test1"));
        assertEquals(10, commitDirector.getParameterService().<Integer>findParameterByName("test2"));
    }
}