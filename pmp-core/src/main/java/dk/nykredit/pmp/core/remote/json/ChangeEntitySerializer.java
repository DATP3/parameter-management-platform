package dk.nykredit.pmp.core.remote.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import dk.nykredit.pmp.core.audit_log.ChangeEntity;

import java.io.IOException;

public class ChangeEntitySerializer extends StdSerializer<ChangeEntity> {
	public ChangeEntitySerializer() {
		this(null);
	}

	protected ChangeEntitySerializer(Class<ChangeEntity> t) {
		super(t);
	}

	@Override
	public void serialize(ChangeEntity change, JsonGenerator gen, SerializerProvider serializerProvider)
			throws IOException {
		gen.writeStartObject();
		switch (change.getChangeType()) {
			case PARAMETER_CHANGE:
				gen.writeStringField("name", change.getParameterName());
				gen.writeStringField("newValue", change.getNewValue());
				gen.writeStringField("oldValue", change.getOldValue());
				break;
			case COMMIT_REVERT:
				gen.writeStringField("referenceHash", Long.toHexString(change.getCommitRevertRef()));
				gen.writeStringField("revertType", String.valueOf(change.getChangeType()));

				// TODO: Find some way to group changes made by the same revert together
				gen.writeArrayFieldStart("parameterChanges");
				gen.writeStartObject();

				gen.writeStringField("name", change.getParameterName());
				gen.writeStringField("newValue", change.getNewValue());
				gen.writeStringField("oldValue", change.getOldValue());

				gen.writeEndObject();
				gen.writeEndArray();
		}

		gen.writeEndObject();
	}
}
