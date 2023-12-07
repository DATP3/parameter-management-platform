package dk.nykredit.pmp.core.remote.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import dk.nykredit.pmp.core.audit_log.AuditLog;
import dk.nykredit.pmp.core.commit.Change;
import dk.nykredit.pmp.core.commit.CommitRevert;
import dk.nykredit.pmp.core.commit.ParameterChange;
import dk.nykredit.pmp.core.commit.ParameterRevert;

public class ChangeDeserializer extends StdDeserializer<Change> {

    AuditLog auditLog;

    public ChangeDeserializer() {
        this((Class<?>) null);
    }

    public ChangeDeserializer(AuditLog auditLog) {
        this();
        this.auditLog = auditLog;
    }

    public ChangeDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Change deserialize(JsonParser p, DeserializationContext ctx) throws IOException {
        ObjectCodec codec = p.getCodec();
        JsonNode node = codec.readTree(p);
        if (!node.has("revertType")) {
            return codec.treeToValue(node, ParameterChange.class);
        }

        RevertAdapter adapter = codec.treeToValue(node, RevertAdapter.class);

        if (adapter.getRevertType().equals("parameter")) {
            return new ParameterRevert(adapter.getParameterName(),
                    Long.parseUnsignedLong(adapter.getCommitReference(), 16));
        }

        if (adapter.getRevertType().equals("commit")) {
            return new CommitRevert(Long.parseUnsignedLong(adapter.getCommitReference(), 16));
        }

        if (adapter.getRevertType().equals("service")) {
            return new CommitRevert(Long.parseUnsignedLong(adapter.getCommitReference(), 16));
        }

        throw new IllegalArgumentException("Invalid revert type: " + node.get("revertType").asText());
    }
}
