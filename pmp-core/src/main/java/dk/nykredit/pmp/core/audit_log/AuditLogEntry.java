package dk.nykredit.pmp.core.audit_log;

import dk.nykredit.pmp.core.commit.Change;
import dk.nykredit.pmp.core.commit.Commit;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "PMP_AUDIT_LOG")
@Getter
@Setter
public class AuditLogEntry {
	@Id
	@Column(name = "COMMIT_ID")
	private long commitId;

	@OneToMany(mappedBy = "commit")
	private List<ChangeEntity> changes;

	@Column(name = "PUSH_DATE")
	private LocalDateTime pushDate;

	@Column(name = "AUTHOR")
	private String user;

	@Column(name = "MESSAGE")
	private String message;

	public List<Change> getChanges() {
		return changes.stream().map(ChangeEntity::toChange).collect(Collectors.toList());
	}

	public List<ChangeEntity> getChangeEntities() {
		return changes;
	}

	public Commit toCommit() {
		Commit commit = new Commit();
		commit.setPushDate(pushDate);
		commit.setUser(user);
		commit.setMessage(message);
		commit.setChanges(getChanges());

		return commit;
	}
}
