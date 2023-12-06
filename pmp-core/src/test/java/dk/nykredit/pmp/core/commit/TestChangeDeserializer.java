package dk.nykredit.pmp.core.commit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.util.List;

import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import org.junit.jupiter.api.AfterEach;

import dk.nykredit.pmp.core.audit_log.ChangeType;
import dk.nykredit.pmp.core.remote.json.ObjectMapperFactory;
import dk.nykredit.pmp.core.remote.json.RevertAdapter;
import dk.nykredit.pmp.core.service.ParameterService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@TestInstance(Lifecycle.PER_CLASS)
public class TestChangeDeserializer {

    ObjectMapper mapper;
    private WeldContainer container;
    private CommitDirector commitDirector;

    @BeforeEach
    public void setupMapper() {
        Weld weld = new Weld();
        container = weld.initialize();
        commitDirector = container.select(CommitDirector.class).get();
        ParameterService parameterService = commitDirector.getParameterService();
        parameterService.persistParameter("test1", "data1");
        parameterService.persistParameter("test2", 5);

        mapper = container.select(ObjectMapperFactory.class).get().getObjectMapper();
    }

    @AfterEach
    public void after() {
        container.shutdown();
    }

    @Test
    public void testDeserializeParameterChange() throws JsonProcessingException {
        ParameterChange expectedChange = new ParameterChange();
        expectedChange.setName("test");
        expectedChange.setNewValue("0");
        expectedChange.setOldValue("1");
        expectedChange.setType("integer");

        String json = mapper.writeValueAsString(expectedChange);
        Change change = mapper.readValue(json, Change.class);
        assertEquals(expectedChange, change);
    }

    @Test
    public void testDeserializeParameterRevert() throws JsonProcessingException {

        ParameterChange paramChange = new ParameterChange("test1", "String", "data1", "data2");
        Commit commit = new Commit();
        commit.setPushDate(LocalDateTime.now());
        commit.setUser("author");
        commit.setMessage("test");
        commit.setChanges(List.of(paramChange));

        commitDirector.apply(commit);

        RevertAdapter revertAdapter = new RevertAdapter();

        revertAdapter.setParameterName("test1");
        revertAdapter.setCommitReference(Long.toHexString(commit.getCommitHash()));
        revertAdapter.setRevertType("parameter");

        String json = mapper.writeValueAsString(revertAdapter);
        Change revert = mapper.readValue(json, Change.class);

        Change expectedRevert = RevertFactory.createChange(paramChange, commit.getCommitHash(),
                ChangeType.PARAMETER_REVERT);

        assertEquals(expectedRevert, revert);
    }

    @Test
    public void testDeserializeCommitRevert() throws JsonProcessingException {
        ParameterChange paramChange = new ParameterChange("test1", "String", "data1", "data2");
        Commit commit = new Commit();
        commit.setPushDate(LocalDateTime.now());
        commit.setUser("author");
        commit.setMessage("test");
        commit.setChanges(List.of(paramChange));

        commitDirector.apply(commit);

        RevertAdapter revertAdapter = new RevertAdapter();

        revertAdapter.setParameterName("test1");
        revertAdapter.setCommitReference(Long.toHexString(commit.getCommitHash()));
        revertAdapter.setRevertType("commit");

        String json = mapper.writeValueAsString(revertAdapter);
        Change revert = mapper.readValue(json, Change.class);

        CommitRevert expectedRevert = new CommitRevert();
        expectedRevert.setCommitHash(commit.getCommitHash());
        expectedRevert.setRevertType(ChangeType.COMMIT_REVERT);

        assertEquals(expectedRevert, revert);
    }
}
