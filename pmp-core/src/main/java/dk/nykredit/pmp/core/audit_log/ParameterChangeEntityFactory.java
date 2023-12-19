package dk.nykredit.pmp.core.audit_log;

import dk.nykredit.pmp.core.commit.ParameterChange;

public class ParameterChangeEntityFactory {

    /**
     * Creates a ChangeEntity from a ParameterChange
     * 
     * @param paramChange The ParameterChange to create a ChangeEntity from.
     * @return A ChangeEntity representing the ParameterChange.
     */
    public ChangeEntity createChangeEntity(ParameterChange paramChange) {
        ChangeEntity resultingChangeEntity = new ChangeEntity();
        resultingChangeEntity.setChangeType(ChangeType.PARAMETER_CHANGE);
        resultingChangeEntity.setParameterName(paramChange.getName());
        resultingChangeEntity.setOldValue(paramChange.getOldValue());
        resultingChangeEntity.setNewValue(paramChange.getNewValue());
        resultingChangeEntity.setParameterType(paramChange.getType());

        return resultingChangeEntity;
    }
}
