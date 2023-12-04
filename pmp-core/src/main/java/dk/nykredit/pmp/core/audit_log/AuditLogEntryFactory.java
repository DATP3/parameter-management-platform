package dk.nykredit.pmp.core.audit_log;

import dk.nykredit.pmp.core.commit.PersistableChange;
import dk.nykredit.pmp.core.commit.Commit;

import java.util.ArrayList;
import java.util.List;

public class AuditLogEntryFactory {

	public AuditLogEntry createAuditLogEntry(Commit commit) {
		AuditLogEntry entry = new AuditLogEntry();

		entry.setCommitId(commit.getCommitHash());
		entry.setPushDate(commit.getPushDate());
		entry.setUser(commit.getUser());
		entry.setMessage(commit.getMessage());

		List<ChangeEntity> changes = new ArrayList<>();
		for (PersistableChange change : commit.getAppliedChanges()) {
			changes.add(change.toChangeEntity(new ChangeEntityFactory(entry)));
		}

		entry.setChanges(changes);

		return entry;
	}

}
