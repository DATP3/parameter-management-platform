package dk.nykredit.pmp.core.remote.json;

import java.io.IOException;

import javax.inject.Inject;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import dk.nykredit.pmp.core.audit_log.AuditLog;
import dk.nykredit.pmp.core.commit.Change;
import dk.nykredit.pmp.core.commit.ParameterChange;

public class ChangeDeserializer extends StdDeserializer<Change> {

    @Inject
    AuditLog auditLog;

    public ChangeDeserializer() {
        this(null);
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

        RevertAdapter revertAdapter = codec.treeToValue(node, RevertAdapter.class);
        return revertAdapter.toRevert();
    }
}
