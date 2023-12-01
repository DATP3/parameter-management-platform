package dk.nykredit.pmp.core.audit_log;

import dk.nykredit.pmp.core.audit_log.ChangeEntity.ChangeType;
import dk.nykredit.pmp.core.commit.Change;
import dk.nykredit.pmp.core.commit.ParameterChange;

public class ChangeEntityFactory {
	public ChangeEntity createChangeEntity(Change change, AuditLogEntry commit) {
		ChangeEntity changeEntity = new ChangeEntity();

		if (change instanceof ParameterChange) {
			ParameterChange paramChange = (ParameterChange) change;
			changeEntity.setCommit(commit);
			changeEntity.setParameterName(paramChange.getName());
			changeEntity.setParameterType(paramChange.getType());
			changeEntity.setOldValue(paramChange.getOldValue());
			changeEntity.setNewValue(paramChange.getNewValue());
			changeEntity.setChangeType(ChangeType.PARAMETER_CHANGE);
			changeEntity.setCommitRevertRef(-1);
		}

		return changeEntity;
	}
}
