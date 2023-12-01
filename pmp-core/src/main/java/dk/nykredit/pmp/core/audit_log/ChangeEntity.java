package dk.nykredit.pmp.core.audit_log;

import dk.nykredit.pmp.core.commit.Change;
import dk.nykredit.pmp.core.commit.ParameterChange;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "PMP_PARAM_CHANGES")
@Getter
@Setter
public class ChangeEntity {
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@ManyToOne
	@JoinColumn(name = "COMMIT_ID")
	private AuditLogEntry commit;

	@Column(name = "PARAMETER_NAME")
	private String parameterName;

	@Column(name = "PARAMETER_TYPE")
	private String parameterType;

	@Column(name = "OLD_VALUE")
	private String oldValue;

	@Column(name = "NEW_VALUE")
	private String newValue;

	@Column(name = "CHANGE_TYPE")
	@Enumerated(EnumType.ORDINAL)
	private ChangeType changeType;

	@Column(name = "COMMIT_REVERT_REF")
	private long commitRevertRef;

	public Change toChange() {
		switch (changeType) {
			case PARAMETER_CHANGE:
				return new ParameterChange(parameterName, parameterType, oldValue, newValue);

			default:
				// TODO: implement RevertChange
				return null;
		}
	}

	public enum ChangeType {
		PARAMETER_CHANGE, COMMIT_REVERT, PARAMETER_REVERT, SERVICE_COMMIT_REVERT
	}

	@Override
	public String toString() {
		return "ChangeEntity{" +
				"id=" + id +
				", commit=" + commit.getCommitId() +
				", parameterName='" + parameterName + '\'' +
				", parameterType='" + parameterType + '\'' +
				", oldValue='" + oldValue + '\'' +
				", newValue='" + newValue + '\'' +
				", changeType=" + changeType +
				", commitRevertRef=" + commitRevertRef +
				'}';
	}
}
