package dk.nykredit.pmp.core.audit_log;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dk.nykredit.pmp.core.commit.ParameterChange;
import dk.nykredit.pmp.core.commit.ParameterRevert;
import dk.nykredit.pmp.core.remote.json.ObjectMapperFactoryImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestAuditLogSerializer {
    ObjectMapper mapper;

    @BeforeAll
    public void setupMapper() {
        mapper = new ObjectMapperFactoryImpl().getObjectMapper();
    }

    @Test
    public void testSerializeParameterChangeEntity() {
        ChangeEntity entity = new ChangeEntity();
        entity.setChangeType(ChangeType.PARAMETER_CHANGE);
        entity.setParameterName("test1");
        entity.setParameterType("String");
        entity.setOldValue("first");
        entity.setNewValue("second");

        try (JsonParser parser = mapper.createParser(mapper.writeValueAsString(entity))) {
            ObjectCodec codec = parser.getCodec();
            JsonNode node = codec.readTree(parser);

            assertEquals("test1", node.get("name").asText());
            assertEquals("first", node.get("oldValue").asText());
            assertEquals("second", node.get("newValue").asText());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testSerializeParameterChangeAuditLogEntry() {
        ParameterChange change = new ParameterChange();
        change.setName("test1");
        change.setOldValue("first");
        change.setNewValue("second");

        LocalDateTime pushDate = LocalDateTime.of(2023, 11, 28, 9, 15, 12, (int) 2.93e8);

		AuditLogEntry entry = new AuditLogEntry();
		entry.setUser("test user");
		entry.setMessage("test commit");
		entry.setPushDate(pushDate);
		entry.setCommitId(123);
		entry.setAffectedServices("service1");

        ChangeEntity entity = new ParameterChangeEntityFactory().createChangeEntity(change);
        entry.setChanges(List.of(entity));

        String json;
        try {
            json = mapper.writeValueAsString(entry);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        try (JsonParser parser = mapper.createParser(json)) {
            ObjectCodec codec = parser.getCodec();
            JsonNode node = codec.readTree(parser);

            assertEquals("test user", node.get("user").asText());
            assertEquals("test commit", node.get("message").asText());
            assertEquals("2023-11-28T09:15:12.293", node.get("pushDate").asText());
            assertEquals(Long.toHexString(123), node.get("hash").asText());

			assertTrue(node.has("affectedServices"));
			assertEquals("service1", node.get("affectedServices").elements().next().asText());

			assertTrue(node.has("changes"));
			JsonNode changes = node.get("changes");
			assertTrue(changes.has("parameterChanges"));
			assertTrue(changes.has("reverts"));

            assertFalse(changes.get("reverts").elements().hasNext());

            JsonNode paramChanges = changes.get("parameterChanges");

            assertTrue(paramChanges.elements().hasNext());
            JsonNode c = paramChanges.elements().next();

            assertEquals("test1", c.get("name").asText());
            assertEquals("first", c.get("oldValue").asText());
            assertEquals("second", c.get("newValue").asText());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testSerializeCommitRevertAuditLogEntry() {
        ParameterChange change = new ParameterChange();
        change.setName("test1");
        change.setOldValue("first");
        change.setNewValue("second");

        LocalDateTime pushDate = LocalDateTime.of(2023, 11, 28, 9, 15, 12, (int) 2.93e8);

		AuditLogEntry entry = new AuditLogEntry();
		entry.setUser("test user");
		entry.setMessage("test commit");
		entry.setPushDate(pushDate);
		entry.setCommitId(123);
		entry.setAffectedServices("service1");

        ChangeEntity changeEntity = new ParameterChangeEntityFactory().createChangeEntity(change);
        entry.setChanges(List.of(changeEntity));

		AuditLogEntry entry2 = new AuditLogEntry();
		entry2.setUser("test user2");
		entry2.setMessage("revert test commit");
		entry2.setPushDate(pushDate.plusHours(2));
		entry2.setCommitId(456);
		entry2.setAffectedServices("service1");

        ParameterRevert parameterRevert = new ParameterRevert();
        parameterRevert.setCommitHash(123);

        ChangeEntity entity2 = new RevertPartChangeEntityFactory().createChangeEntity(changeEntity,
                ChangeType.COMMIT_REVERT, 123);

        entry2.setChanges(List.of(entity2));

        String json;
        try {
            json = mapper.writeValueAsString(entry2);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        try (JsonParser parser = mapper.createParser(json)) {
            ObjectCodec codec = parser.getCodec();
            JsonNode node = codec.readTree(parser);

            assertEquals("test user2", node.get("user").asText());
            assertEquals("revert test commit", node.get("message").asText());
            assertEquals("2023-11-28T11:15:12.293", node.get("pushDate").asText());
            assertEquals(Long.toHexString(456), node.get("hash").asText());

            assertTrue(node.has("changes"));
            JsonNode changes = node.get("changes");
            assertTrue(changes.has("parameterChanges"));
            assertTrue(changes.has("reverts"));

			assertTrue(node.has("affectedServices"));
			assertEquals("service1", node.get("affectedServices").elements().next().asText());

			assertTrue(node.has("changes"));
			JsonNode changes = node.get("changes");
			assertTrue(changes.has("parameterChanges"));
			assertTrue(changes.has("reverts"));
            assertFalse(changes.get("parameterChanges").elements().hasNext());

            JsonNode reverts = changes.get("reverts");

            assertTrue(reverts.elements().hasNext());
            JsonNode revertNode = reverts.elements().next();

            assertEquals(Long.toHexString(123), revertNode.get("referenceHash").asText());
            assertEquals(String.valueOf(ChangeType.COMMIT_REVERT), revertNode.get("revertType").asText());

            assertTrue(revertNode.has("parameterChanges"));
            assertTrue(revertNode.get("parameterChanges").elements().hasNext());

            JsonNode c = revertNode.get("parameterChanges").elements().next();

            assertEquals("test1", c.get("name").asText());
            assertEquals("second", c.get("oldValue").asText());
            assertEquals("first", c.get("newValue").asText());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
