package dk.nykredit.pmp.core.commit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import org.junit.jupiter.api.AfterEach;

import dk.nykredit.pmp.core.remote.json.ObjectMapperFactory;
import dk.nykredit.pmp.core.remote.json.raw_types.RawChange;
import dk.nykredit.pmp.core.remote.json.raw_types.RawCommit;
import dk.nykredit.pmp.core.remote.json.raw_types.RawCommitRevert;
import dk.nykredit.pmp.core.remote.json.raw_types.RawParameterChange;
import dk.nykredit.pmp.core.remote.json.raw_types.RawParameterRevert;
import dk.nykredit.pmp.core.service.ParameterService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

// Create class once and use for all methods.
@TestInstance(Lifecycle.PER_CLASS)
public class TestChangeDeserializer {

    ObjectMapper mapper;
    private WeldContainer container;
    private CommitDirector commitDirector;

    /**
     * Setup the test environment before each test.
     * Weld container is initialized and the commit director and Object mapper for
     * json serialization is created in dependency injection context.
     * Get the parameter service and persist some parameters.
     */
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

    /**
     * Reset test environment.
     */
    @AfterEach
    public void after() {
        container.shutdown();
    }

    /**
     * Test that a raw parameter change is deserialized correctly.
     * 
     * @throws Exception
     */
    @Test
    public void testDeserializeRawParameterChange() throws Exception {
        // Setup raw parameter change.
        RawParameterChange expectedChange = new RawParameterChange();
        expectedChange.setName("test");
        expectedChange.setNewValue("0");
        expectedChange.setOldValue("1");
        expectedChange.setType("integer");
        expectedChange.setId("id");
        expectedChange.setService(new Service("testServiceName", "testServiceAddress", "testServiceEnvironment"));

        // Serialize raw parameter change to json and deserialize it back to a raw
        // parameter change.
        String json = mapper.writeValueAsString(expectedChange);
        RawChange change = mapper.readValue(json, RawParameterChange.class);

        // Check that the deserialized json string is equal to the expected raw
        // parameter change.
        assertEquals(expectedChange, change);
    }

    /**
     * Test that a raw parameter revert is deserialized correctly.
     * 
     * @throws JsonProcessingException
     */
    @Test
    public void testDeserializeRawParameterRevert() throws JsonProcessingException {

        // Setup raw parameter revert.
        RawParameterRevert expectedRevert = new RawParameterRevert();
        expectedRevert.setParameterName("test1");
        expectedRevert.setCommitHash("10AC");
        expectedRevert.setRevertType("parameter");
        expectedRevert.setService(new Service("testServiceName", "testServiceAddress", "testServiceEnvironment"));

        // Setup json string with a parameter revert corresponding to the raw parameter
        // revert.
        String json = "{\"parameterName\":\"test1\",\"commitReference\":\"10AC\",\"revertType\":\"parameter\",\"service\":{\"name\":\"testServiceName\",\"address\":\"testServiceAddress\",\"environment\":\"testServiceEnvironment\"}}";
        // Deserialize json string to raw parameter revert.
        RawParameterRevert revert = mapper.readValue(json, RawParameterRevert.class);

        // Check that the deserialized json string is equal to the expected raw
        // parameter revert.
        assertEquals(expectedRevert, revert);
    }

    /**
     * Test that a raw commit revert is deserialized correctly.
     * 
     * @throws JsonProcessingException
     */
    @Test
    public void testDeserializeRawCommitRevert() throws JsonProcessingException {

        // Setup json string with a commit revert corresponding to the raw commit revert
        String json = "{\"commitReference\":\"10\"}";

        // Setup raw commit revert.
        RawCommitRevert expectedRevert = new RawCommitRevert();
        expectedRevert.setCommitHash("10");

        // Deserialize json string to raw commit revert.
        RawCommitRevert revert = mapper.readValue(json, RawCommitRevert.class);

        // Check that the deserialized json string is equal to the expected raw
        // commit revert.
        assertEquals(expectedRevert, revert);
    }

    /**
     * Test that a raw commit is deserialized correctly.
     * 
     * @throws JsonProcessingException
     */
    @Test
    public void testDeserializeRawCommit() throws JsonProcessingException {
        // Setup json string with a commit corresponding to the raw commit.
        String json = "{\"user\":\"author\",\"message\":\"test commit\",\"pushDate\":\"2020-05-05T12:00:00\",\"affectedServices\":[\"service1\"],\"changes\":[{\"name\":\"test1\",\"newValue\":\"data2\",\"value\":\"data1\",\"type\":\"String\",\"id\":\"id1\",\"service\":{\"name\":\"service1\",\"address\":\"service1Address\",\"environment\":\"service1Environment\"}},{\"name\":\"test2\",\"newValue\":\"6\",\"value\":\"5\",\"type\":\"Integer\",\"id\":\"id2\",\"service\":{\"name\":\"service1\",\"address\":\"service1Address\",\"environment\":\"service1Environment\"}}]}";
        // Create array of expected affected services.
        List<String> expectedAffectedServices = new ArrayList<String>();
        // Add affected service to expected affected services.
        expectedAffectedServices.add("service1");
        // Create array of expected changes.
        List<RawChange> expectedChanges = new ArrayList<RawChange>();

        // Setup expected raw commit.
        RawCommit expectedCommit = new RawCommit();
        expectedCommit.setUser("author");
        expectedCommit.setMessage("test commit");
        expectedCommit.setPushDate(LocalDateTime.parse("2020-05-05T12:00:00"));
        expectedCommit.setAffectedServices(expectedAffectedServices);
        // Setup and add change to expected changes array.
        RawParameterChange change1 = new RawParameterChange();
        change1.setName("test1");
        change1.setNewValue("data2");
        change1.setOldValue("data1");
        change1.setType("String");
        change1.setId("id1");
        change1.setService(new Service("service1", "service1Address", "service1Environment"));
        expectedChanges.add(change1);
        RawParameterChange change2 = new RawParameterChange();
        change2.setName("test2");
        change2.setNewValue("6");
        change2.setOldValue("5");
        change2.setType("Integer");
        change2.setId("id2");
        change2.setService(new Service("service1", "service1Address", "service1Environment"));
        expectedChanges.add(change2);
        // Add expected changes to expected commit.
        expectedCommit.setChanges(expectedChanges);

        // Deserialize json string to raw commit.
        RawCommit commit = mapper.readValue(json, RawCommit.class);

        // Check that the deserialized json string is equal to the expected raw commit.
        assertEquals(expectedCommit, commit);
    }

}
