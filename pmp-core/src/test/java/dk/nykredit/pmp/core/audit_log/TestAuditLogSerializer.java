package dk.nykredit.pmp.core.audit_log;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dk.nykredit.pmp.core.commit.ParameterChange;
import dk.nykredit.pmp.core.remote.json.ObjectMapperFactoryImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// Create class once and use for all methods.
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestAuditLogSerializer {
    ObjectMapper mapper;

    /**
     * Setup the test environment before each test.
     * ObjectMapper for json serialization is initialized in dependency injection
     * context.
     */
    @BeforeAll
    public void setupMapper() {
        mapper = new ObjectMapperFactoryImpl().getObjectMapper();
    }

    /**
     * Test that parameter change entity is serialized correctly.
     */
    @Test
    public void testSerializeParameterChangeEntity() {
        // Setup change entity
        ChangeEntity entity = new ChangeEntity();
        entity.setChangeType(ChangeType.PARAMETER_CHANGE);
        entity.setParameterName("test1");
        entity.setParameterType("String");
        entity.setOldValue("first");
        entity.setNewValue("second");

        /**
         * Serialize change entity to json and check that the correct values are
         * present.
         * Throw exception if serialization fails.
         */
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

    /**
     * Test that checks if the json serialization of an audit log entry containing
     * a parameter change is correct.
     */
    @Test
    public void testSerializeParameterChangeAuditLogEntry() {
        // Setup parameter change.
        ParameterChange change = new ParameterChange();
        change.setName("test1");
        change.setOldValue("first");
        change.setNewValue("second");

        // Setup audit log entry.
        LocalDateTime pushDate = LocalDateTime.of(2023, 11, 28, 9, 15, 12, (int) 2.93e8);
        AuditLogEntry entry = new AuditLogEntry();
        entry.setUser("test user");
        entry.setMessage("test commit");
        entry.setPushDate(pushDate);
        entry.setCommitId(123);
        entry.setAffectedServices("service1");

        // Setup change entity.
        ChangeEntity entity = new ParameterChangeEntityFactory().createChangeEntity(change);
        entry.setChanges(List.of(entity));

        /**
         * Try to serialize audit log entry to json.
         * Throw exception if serialization fails (should not happen).
         */
        String json;
        try {
            json = mapper.writeValueAsString(entry);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        /**
         * Deserialize json and check that the correct values are present.
         * Throw exception if deserialization fails (should not happen).
         */
        try (JsonParser parser = mapper.createParser(json)) {
            // Setup codec and json reader.
            ObjectCodec codec = parser.getCodec();
            JsonNode node = codec.readTree(parser);

            // Check that the correct user, date, and commit hash are present.
            assertEquals("test user", node.get("user").asText());
            assertEquals("test commit", node.get("message").asText());
            assertEquals("2023-11-28T09:15:12.293", node.get("pushDate").asText());
            assertEquals(Long.toHexString(123), node.get("hash").asText());

            // Check that the correct affected services are present.
            assertTrue(node.has("affectedServices"));
            assertEquals("service1", node.get("affectedServices").elements().next().asText());

            // Check that fields for changes and reverts are present.
            assertTrue(node.has("changes"));
            JsonNode changes = node.get("changes");
            assertTrue(changes.has("parameterChanges"));
            assertTrue(changes.has("reverts"));

            // Check that the reverts field is empty.
            assertFalse(changes.get("reverts").elements().hasNext());

            // Check that the parameter changes field is not empty.
            JsonNode paramChanges = changes.get("parameterChanges");
            assertTrue(paramChanges.elements().hasNext());

            // Check that the correct values are present in the parameter change.
            JsonNode c = paramChanges.elements().next();
            assertEquals("test1", c.get("name").asText());
            assertEquals("first", c.get("oldValue").asText());
            assertEquals("second", c.get("newValue").asText());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Test that checks if the json serialization of an audit log entry containing
     * a commit revert is correct.
     */
    @Test
    public void testSerializeCommitRevertAuditLogEntry() {
        // Setup parameter change.
        ParameterChange change = new ParameterChange();
        change.setName("test1");
        change.setOldValue("first");
        change.setNewValue("second");

        // Setup audit log entry.
        LocalDateTime pushDate = LocalDateTime.of(2023, 11, 28, 9, 15, 12, (int) 2.93e8);
        AuditLogEntry entry = new AuditLogEntry();
        entry.setUser("test user");
        entry.setMessage("test commit");
        entry.setPushDate(pushDate);
        entry.setCommitId(123);
        entry.setAffectedServices("service1");

        // Setup change entity.
        ChangeEntity changeEntity = new ParameterChangeEntityFactory().createChangeEntity(change);
        entry.setChanges(List.of(changeEntity));

        // Setup revert of commit.
        AuditLogEntry entry2 = new AuditLogEntry();
        entry2.setUser("test user2");
        entry2.setMessage("revert test commit");
        entry2.setPushDate(pushDate.plusHours(2));
        entry2.setCommitId(456);
        entry2.setAffectedServices("service1");

        // Setup change entity for revert.
        ChangeEntity entity2 = new RevertPartChangeEntityFactory().createChangeEntity(changeEntity,
                ChangeType.COMMIT_REVERT, 123);
        entry2.setChanges(List.of(entity2));

        /**
         * Serialize audit log entry to json.
         * Try-with-resources to automatically close the parser.
         */
        String json;
        try {
            json = mapper.writeValueAsString(entry2);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        /**
         * Deserialize json and check that the correct values are present.
         * Try-with-resources to automatically close the parser.
         */
        try (JsonParser parser = mapper.createParser(json)) {
            // Setup codec and json reader.
            ObjectCodec codec = parser.getCodec();
            JsonNode node = codec.readTree(parser);

            // Check that the correct user, date, and commit hash are present.
            assertEquals("test user2", node.get("user").asText());
            assertEquals("revert test commit", node.get("message").asText());
            assertEquals("2023-11-28T11:15:12.293", node.get("pushDate").asText());
            assertEquals(Long.toHexString(456), node.get("hash").asText());

            // Check that fields for changes and reverts are present.
            assertTrue(node.has("changes"));
            JsonNode changes = node.get("changes");
            assertTrue(changes.has("parameterChanges"));
            assertTrue(changes.has("reverts"));

            // Check that the parameter changes field is empty.
            assertFalse(changes.get("parameterChanges").elements().hasNext());
            // Check that the correct affected services are present.
            assertTrue(node.has("affectedServices"));
            assertEquals("service1", node.get("affectedServices").elements().next().asText());

            // Chekc that the reverts field is not empty.
            JsonNode reverts = changes.get("reverts");
            assertTrue(reverts.elements().hasNext());

            // Check that the reference ond type of the revert is correct.
            JsonNode revertNode = reverts.elements().next();
            assertEquals(Long.toHexString(123), revertNode.get("referenceHash").asText());
            assertEquals(String.valueOf(ChangeType.COMMIT_REVERT), revertNode.get("revertType").asText());

            // Check that the revert has a non-empty parameter changes field.
            assertTrue(revertNode.has("parameterChanges"));
            assertTrue(revertNode.get("parameterChanges").elements().hasNext());

            // Check that the correct values are present in the parameter change.
            JsonNode c = revertNode.get("parameterChanges").elements().next();
            assertEquals("test1", c.get("name").asText());
            assertEquals("second", c.get("oldValue").asText());
            assertEquals("first", c.get("newValue").asText());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
