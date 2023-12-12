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
    public void testDeserializeRawParameterChange() throws Exception {
        RawParameterChange expectedChange = new RawParameterChange();
        expectedChange.setName("test");
        expectedChange.setNewValue("0");
        expectedChange.setOldValue("1");
        expectedChange.setType("integer");
        expectedChange.setId("id");
        expectedChange.setService(new Service("testServiceName", "testServiceAddress", "testServiceEnvironment"));

        String json = mapper.writeValueAsString(expectedChange);
        RawChange change = mapper.readValue(json, RawParameterChange.class);

        assertEquals(expectedChange, change);
    }

    @Test
    public void testDeserializeRawParameterRevert() throws JsonProcessingException {

        RawParameterRevert expectedRevert = new RawParameterRevert();
        expectedRevert.setParameterName("test1");
        expectedRevert.setCommitHash("10AC");
        expectedRevert.setRevertType("parameter");
        expectedRevert.setService(new Service("testServiceName", "testServiceAddress", "testServiceEnvironment"));

        String json = "{\"parameterName\":\"test1\",\"commitReference\":\"10AC\",\"revertType\":\"parameter\",\"service\":{\"name\":\"testServiceName\",\"address\":\"testServiceAddress\",\"environment\":\"testServiceEnvironment\"}}";
        RawParameterRevert revert = mapper.readValue(json, RawParameterRevert.class);

        assertEquals(expectedRevert, revert);
    }

    @Test
    public void testDeserializeRawCommitRevert() throws JsonProcessingException {

        String json = "{\"commitReference\":\"10\"}";

        RawCommitRevert expectedRevert = new RawCommitRevert();
        expectedRevert.setCommitHash("10");

        RawCommitRevert revert = mapper.readValue(json, RawCommitRevert.class);

        assertEquals(expectedRevert, revert);
    }

    @Test
    public void testDeserializeRawCommit() throws JsonProcessingException {
        String json = "{\"user\":\"author\",\"message\":\"test commit\",\"pushDate\":\"2020-05-05T12:00:00\",\"affectedServices\":[\"service1\"],\"changes\":[{\"name\":\"test1\",\"newValue\":\"data2\",\"value\":\"data1\",\"type\":\"String\",\"id\":\"id1\",\"service\":{\"name\":\"service1\",\"address\":\"service1Address\",\"environment\":\"service1Environment\"}},{\"name\":\"test2\",\"newValue\":\"6\",\"value\":\"5\",\"type\":\"Integer\",\"id\":\"id2\",\"service\":{\"name\":\"service1\",\"address\":\"service1Address\",\"environment\":\"service1Environment\"}}]}";
        List<String> expectedAffectedServices = new ArrayList<String>();
        expectedAffectedServices.add("service1");
        List<RawChange> expectedChanges = new ArrayList<RawChange>();

        RawCommit expectedCommit = new RawCommit();
        expectedCommit.setUser("author");
        expectedCommit.setMessage("test commit");
        // expectedCommit.setPushDate("2020-05-05T12:00:00");
        expectedCommit.setPushDate(LocalDateTime.parse("2020-05-05T12:00:00"));
        expectedCommit.setAffectedServices(expectedAffectedServices);
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
        expectedCommit.setChanges(expectedChanges);

        RawCommit commit = mapper.readValue(json, RawCommit.class);

        assertEquals(expectedCommit, commit);
    }

}
