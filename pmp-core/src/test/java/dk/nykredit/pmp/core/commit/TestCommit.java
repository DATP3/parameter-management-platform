package dk.nykredit.pmp.core.commit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import dk.nykredit.pmp.core.commit.exception.OldValueInconsistentException;
import dk.nykredit.pmp.core.commit.exception.TypeInconsistentException;
import dk.nykredit.pmp.core.database.setup.H2StartDatabase;
import dk.nykredit.pmp.core.remote.json.ObjectMapperFactoryImpl;
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

public class TestCommit extends H2StartDatabase {

    private static final String commitJson = "{\n" +
            "\"pushDate\": \"2023-11-28T09:15:12.293Z\",\n" +
            "\"user\": \"test\",\n" +
            "\"message\": \"test commit\",\n" +
            "\"changes\": [\n" +
            "{\n" +
            "\"name\": \"test1\",\n" +
            "\"type\": \"String\",\n" +
            "\"oldValue\": \"data1\",\n" +
            "\"newValue\": \"data2\"\n" +
            "}\n" +
            "]\n" +
            "}";

    private WeldContainer container;
    private ParameterService parameterService;

    private ObjectMapper mapper;

    @BeforeEach
    public void before() {
        Weld weld = new Weld();
        container = weld.initialize();
        parameterService = container.select(ParameterService.class).get();
        parameterService.persistParameter("test1", "data1");
        parameterService.persistParameter("test2", 5);

        mapper = new ObjectMapperFactoryImpl().getObjectMapper();
    }

    @AfterEach
    public void after() {
        container.shutdown();
    }

    @Test
    public void testApplyStringParameterChange() {
        Commit commit = new Commit();
        commit.setUser("author");
        commit.setPushDate(LocalDateTime.now());

        List<Change> changes = new ArrayList<>();
        ParameterChange change = new ParameterChange();
        change.setName("test1");
        change.setNewValue("data2");
        change.setOldValue("data1");
        change.setType("String");
        changes.add(change);

        commit.setChanges(changes);
        assertDoesNotThrow(() -> commit.apply(parameterService));

        assertEquals("data2", parameterService.findParameterByName("test1"));
    }

    @Test
    public void testApplyIntegerParameterChange() {
        Commit commit = new Commit();
        commit.setUser("author");
        commit.setPushDate(LocalDateTime.now());

        List<Change> changes = new ArrayList<>();
        ParameterChange change = new ParameterChange();
        change.setName("test2");
        change.setNewValue("6");
        change.setOldValue("5");
        change.setType("Integer");
        changes.add(change);

        commit.setChanges(changes);
        assertDoesNotThrow(() -> commit.apply(parameterService));

        assertEquals((Integer) 6, parameterService.findParameterByName("test2"));
    }

    @Test
    public void testUndoMismatchedType() {
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
        assertThrows(TypeInconsistentException.class, () -> commit.apply(parameterService));

        assertEquals((Integer) 5, parameterService.findParameterByName("test2"));
    }

    @Test
    public void testUndoMismatchedOldValue() {
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
        assertThrows(OldValueInconsistentException.class, () -> commit.apply(parameterService));

        assertEquals((Integer) 5, parameterService.findParameterByName("test2"));
    }

    @Test
    public void testUndoMultipleValues() {
        Commit commit = new Commit();
        commit.setUser("author");
        commit.setPushDate(LocalDateTime.now());

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
        assertThrows(OldValueInconsistentException.class, () -> commit.apply(parameterService));

        assertEquals((Integer) 5, parameterService.findParameterByName("test2"));
        assertEquals("data1", parameterService.findParameterByName("test1"));
    }

    @Test
    public void testParseCommit() throws JsonProcessingException {
        LocalDateTime pushDate = LocalDateTime.of(2023, 11, 28, 9, 15, 12, (int) 2.93e8);

        List<Change> changes = new ArrayList<>();
        ParameterChange change = new ParameterChange();
        change.setName("test1");
        change.setType("String");
        change.setOldValue("data1");
        change.setNewValue("data2");
        changes.add(change);

        Commit commit = mapper.readValue(commitJson, Commit.class);
        assertEquals("test", commit.getUser());
        assertEquals("test commit", commit.getMessage());
        assertEquals(pushDate, commit.getPushDate());
        assertEquals(changes, commit.getChanges());
    }

}
