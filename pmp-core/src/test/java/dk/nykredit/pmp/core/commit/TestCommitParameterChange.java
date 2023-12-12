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

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
public class TestCommitParameterChange extends H2StartDatabase {

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
    public void testApplyStringParameterChange() {
        ParameterService parameterService = commitDirector.getParameterService();

        Commit commit = new Commit();
        commit.setUser("author");
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
        assertDoesNotThrow(() -> commit.apply(commitDirector));

        assertEquals("data2", parameterService.findParameterByName("test1"));
    }

    @Test
    public void testApplyIntegerParameterChange() {
        ParameterService parameterService = commitDirector.getParameterService();

        Commit commit = new Commit();
        commit.setUser("author");
        commit.setPushDate(LocalDateTime.now());
        commit.setAffectedServices(List.of("service1"));

        List<Change> changes = new ArrayList<>();
        ParameterChange change = new ParameterChange();
        change.setName("test2");
        change.setNewValue("6");
        change.setOldValue("5");
        change.setType("Integer");
        changes.add(change);

        commit.setChanges(changes);
        assertDoesNotThrow(() -> commit.apply(commitDirector));

        assertEquals((Integer) 6, parameterService.findParameterByName("test2"));
    }

    @Test
    public void testApplyConsecutiveIntegerParameterChange() {
        ParameterService parameterService = commitDirector.getParameterService();

        Commit commit = new Commit();
        commit.setUser("author");
        commit.setPushDate(LocalDateTime.now());
        commit.setAffectedServices(List.of("service1"));

        List<Change> changes = new ArrayList<>();
        ParameterChange change = new ParameterChange();
        change.setName("test2");
        change.setNewValue("6");
        change.setOldValue("5");
        change.setType("Integer");
        changes.add(change);

        commit.setChanges(changes);
        assertDoesNotThrow(() -> commit.apply(commitDirector));

        assertEquals((Integer) 6, parameterService.findParameterByName("test2"));

        ParameterService parameterService2 = commitDirector.getParameterService();

        Commit commit2 = new Commit();
        commit2.setUser("author");
        commit2.setPushDate(LocalDateTime.now());
        commit2.setAffectedServices(List.of("service1"));

        List<Change> changes2 = new ArrayList<>();
        ParameterChange change2 = new ParameterChange();
        change2.setName("test2");
        change2.setNewValue("7");
        change2.setOldValue("6");
        change2.setType("Integer");
        changes2.add(change2);

        commit2.setChanges(changes2);
        assertDoesNotThrow(() -> commit2.apply(commitDirector));

        assertEquals((Integer) 7, parameterService2.findParameterByName("test2"));
    }

    @Test
    public void testUndoMismatchedType() {
        ParameterService parameterService = commitDirector.getParameterService();

        Commit commit = new Commit();
        commit.setUser("author");
        commit.setPushDate(LocalDateTime.now());

        List<Change> changes = new ArrayList<>();
        ParameterChange change = new ParameterChange();
        change.setName("test2");
        change.setNewValue("6");
        change.setOldValue("5");
        change.setType("String");
        changes.add(change);

        commit.setChanges(changes);
        assertThrows(TypeInconsistentException.class, () -> commit.apply(commitDirector));

        assertEquals((Integer) 5, parameterService.findParameterByName("test2"));
    }

    @Test
    public void testUndoMismatchedOldValue() {
        ParameterService parameterService = commitDirector.getParameterService();

        Commit commit = new Commit();
        commit.setUser("author");
        commit.setPushDate(LocalDateTime.now());

        List<Change> changes = new ArrayList<>();
        ParameterChange change = new ParameterChange();
        change.setName("test2");
        change.setNewValue("6");
        change.setOldValue("4");
        change.setType("Integer");
        changes.add(change);

        commit.setChanges(changes);
        assertThrows(OldValueInconsistentException.class, () -> commit.apply(commitDirector));

        assertEquals((Integer) 5, parameterService.findParameterByName("test2"));
    }

    @Test
    public void testUndoMultipleValues() {
        ParameterService parameterService = commitDirector.getParameterService();

        Commit commit = new Commit();
        commit.setUser("author");
        commit.setPushDate(LocalDateTime.now());
        commit.setAffectedServices(List.of("service1"));

        List<Change> changes = new ArrayList<>();
        ParameterChange change1 = new ParameterChange();
        change1.setName("test1");
        change1.setNewValue("data2");
        change1.setOldValue("data1");
        change1.setType("String");
        changes.add(change1);

        ParameterChange change2 = new ParameterChange();
        change2.setName("test2");
        change2.setNewValue("6");
        change2.setOldValue("4");
        change2.setType("Integer");
        changes.add(change2);

        commit.setChanges(changes);
        assertThrows(OldValueInconsistentException.class, () -> commit.apply(commitDirector));

        assertEquals((Integer) 5, parameterService.findParameterByName("test2"));
        assertEquals("data1", parameterService.findParameterByName("test1"));
    }
}
