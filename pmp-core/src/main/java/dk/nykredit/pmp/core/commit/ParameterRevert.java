package dk.nykredit.pmp.core.commit;

import dk.nykredit.pmp.core.audit_log.ChangeType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParameterRevert extends ParameterChange {
	private long commitHash;
	private ChangeType revertType;

	ParameterRevert() {
	}

	public ParameterRevert(String name, String type, String oldValue, String newValue, long commitHash,
			ChangeType revertType) {
		super(name, type, oldValue, newValue);
		this.commitHash = commitHash;
		this.revertType = revertType;
	}
}
