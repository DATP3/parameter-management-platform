package dk.nykredit.pmp.core.remote.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import dk.nykredit.pmp.core.audit_log.AuditLog;
import dk.nykredit.pmp.core.commit.RawChange;
import dk.nykredit.pmp.core.commit.RawCommitRevert;
import dk.nykredit.pmp.core.commit.RawParameterChange;
import dk.nykredit.pmp.core.commit.RawParameterRevert;

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

    @Override
    public RawChange deserialize(JsonParser p, DeserializationContext ctx) throws IOException {
        ObjectCodec codec = p.getCodec();
        JsonNode node = codec.readTree(p);


        if (!node.has("revertType")) {
            return codec.treeToValue(node, RawParameterChange.class);
        }

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
