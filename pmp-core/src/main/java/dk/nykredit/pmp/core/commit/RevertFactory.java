package dk.nykredit.pmp.core.commit;

import dk.nykredit.pmp.core.audit_log.ChangeEntity;
import dk.nykredit.pmp.core.audit_log.ChangeType;

public class RevertFactory {
	/**
	 * Creates a ParameterRevert object from an origianl ChangeEntity object by
	 * fliping the old and new values.
	 * 
	 * @param change
	 * @param commitHash
	 * @return
	 */
	public static PersistableChange createChange(ChangeEntity originalChangeEntity, ChangeType reverType) {
		ParameterRevert revert = new ParameterRevert();
		revert.setName(originalChangeEntity.getParameterName());
		revert.setType(originalChangeEntity.getParameterType());
		revert.setCommitHash(originalChangeEntity.getCommitRevertRef());

		revert.setOldValue(originalChangeEntity.getNewValue());
		revert.setNewValue(originalChangeEntity.getOldValue());
		revert.setRevertType(reverType);

		return revert;
	}
}
