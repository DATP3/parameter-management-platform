package dk.nykredit.pmp.core.remote.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import dk.nykredit.pmp.core.audit_log.AuditLog;
import dk.nykredit.pmp.core.remote.json.raw_types.RawChange;
import dk.nykredit.pmp.core.remote.json.raw_types.RawCommitRevert;
import dk.nykredit.pmp.core.remote.json.raw_types.RawParameterChange;
import dk.nykredit.pmp.core.remote.json.raw_types.RawParameterRevert;

public class RawChangeDeserializer extends StdDeserializer<RawChange> {

    AuditLog auditLog;

    public RawChangeDeserializer() {
        this((Class<?>) null);
    }

    public RawChangeDeserializer(AuditLog auditLog) {
        this();
        this.auditLog = auditLog;
    }

    public RawChangeDeserializer(Class<?> vc) {
        super(vc);
    }

    /**
     * Deserializes a RawChange object from a JsonParser.
     * 
     * @param p   The JsonParser to deserialize from.
     * @param ctx is not used, but is required by the super class.
     * @return The deserialized RawChange object.
     */
    @Override
    public RawChange deserialize(JsonParser p, DeserializationContext ctx) throws IOException {

        // Gets the root JsonNode from the JsonParser
        ObjectCodec codec = p.getCodec();
        JsonNode node = codec.readTree(p);

        // If the node does not have a revertType field, it is a RawParameterChange
        if (!node.has("revertType")) {
            return codec.treeToValue(node, RawParameterChange.class);
        }

        // If the node has a revertType field, it is either a RawParameterRevert or a
        // RawCommitRevert
        switch (node.get("revertType").asText()) {
            case "parameter":
                return codec.treeToValue(node, RawParameterRevert.class);

            case "commit":
                return codec.treeToValue(node, RawCommitRevert.class);

            default:
                throw new IllegalArgumentException("Invalid revert type: " + node.get("revertType").asText());
        }
    }
}
