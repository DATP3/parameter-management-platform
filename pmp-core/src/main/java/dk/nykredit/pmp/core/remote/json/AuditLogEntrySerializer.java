package dk.nykredit.pmp.core.remote.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import dk.nykredit.pmp.core.audit_log.AuditLogEntry;
import dk.nykredit.pmp.core.audit_log.ChangeEntity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AuditLogEntrySerializer extends StdSerializer<AuditLogEntry> {
    public AuditLogEntrySerializer() {
        this(null);
    }

    protected AuditLogEntrySerializer(Class<AuditLogEntry> t) {
        super(t);
    }

    @Override
    public void serialize(AuditLogEntry entry, JsonGenerator gen, SerializerProvider serializerProvider)
            throws IOException {

        // Uses the JsonGenerator to write the AuditLogEntry object to a json object
        gen.writeStartObject();
        gen.writeStringField("user", entry.getUser());
        gen.writeObjectField("pushDate", entry.getPushDate());
        gen.writeStringField("hash", Long.toHexString(entry.getCommitId()));
        gen.writeStringField("message", entry.getMessage());

        // Begin array of affected services and add each service to the array. Then end
        // the array
        gen.writeArrayFieldStart("affectedServices");
        for (String service : entry.getAffectedServices().split(",")) {
            gen.writeString(service);
        }
        gen.writeEndArray();

        // Two separate lists are created, to sort the various changes into.
        List<ChangeEntity> parameterChanges = new ArrayList<>();
        List<ChangeEntity> reverts = new ArrayList<>();

        // Sorts the changes into the two lists
        for (ChangeEntity c : entry.getChangeEntities()) {
            switch (c.getChangeType()) {
                case PARAMETER_CHANGE:
                    parameterChanges.add(c);
                    break;
                case COMMIT_REVERT:
                case PARAMETER_REVERT:
                case SERVICE_COMMIT_REVERT:
                    reverts.add(c);
                    break;
            }
        }

        // Add array of parameter changes in the changes object
        gen.writeObjectFieldStart("changes");
        gen.writeArrayFieldStart("parameterChanges");
        for (ChangeEntity c : parameterChanges) {
            gen.writeObject(c);
        }
        gen.writeEndArray();

        // Add array of reverts in the changes object
        gen.writeArrayFieldStart("reverts");
        for (ChangeEntity c : reverts) {
            gen.writeObject(c);
        }
        gen.writeEndArray();
        gen.writeEndObject(); // End changes object

        gen.writeEndObject(); // End AuditLogEntry object
    }
}
