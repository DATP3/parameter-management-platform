package dk.nykredit.pmp.core.audit_log;

public class RevertPartChangeEntityFactory {
    public ChangeEntity createChangeEntity(ChangeEntity originalChangeEntity, ChangeType revertType, long commitRef) {
        ChangeEntity resultingChangeEntity = new ChangeEntity();
        resultingChangeEntity.setCommitRevertRef(commitRef);
        resultingChangeEntity.setChangeType(ChangeType.COMMIT_REVERT);
        resultingChangeEntity.setParameterName(originalChangeEntity.getParameterName());
        resultingChangeEntity.setParameterType(originalChangeEntity.getParameterType());

        // Swap the old and new values
        resultingChangeEntity.setOldValue(originalChangeEntity.getNewValue());
        resultingChangeEntity.setNewValue(originalChangeEntity.getOldValue());

        return resultingChangeEntity;
    }

}