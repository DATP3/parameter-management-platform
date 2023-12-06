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
		gen.writeStartObject();

		gen.writeStringField("user", entry.getUser());
		gen.writeObjectField("pushDate", entry.getPushDate());
		gen.writeStringField("hash", Long.toHexString(entry.getCommitId()));
		gen.writeStringField("message", entry.getMessage());
		// TODO: Write affected services

		// TODO: Maybe this should have a better type?
		List<ChangeEntity> parameterChanges = new ArrayList<>();
		List<ChangeEntity> reverts = new ArrayList<>();

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

		gen.writeObjectFieldStart("changes");
		gen.writeArrayFieldStart("parameterChanges");
		for (ChangeEntity c : parameterChanges) {
			gen.writeObject(c);
		}
		gen.writeEndArray();

		gen.writeArrayFieldStart("reverts");
		for (ChangeEntity c : reverts) {
			gen.writeObject(c);
		}
		gen.writeEndArray();
		gen.writeEndObject();

		gen.writeEndObject();
	}
}
