package dk.nykredit.pmp.core.commit;

import java.util.ArrayList;
import java.util.List;

import dk.nykredit.pmp.core.audit_log.ChangeEntity;
import dk.nykredit.pmp.core.audit_log.ChangeEntityFactory;
import dk.nykredit.pmp.core.audit_log.ChangeType;
import dk.nykredit.pmp.core.commit.exception.CommitException;
import dk.nykredit.pmp.core.commit.exception.OldValueInconsistentException;
import dk.nykredit.pmp.core.commit.exception.TypeInconsistentException;
import dk.nykredit.pmp.core.service.ParameterService;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParameterChange implements PersistableChange {
    protected String name;
    protected String type;
    protected String oldValue;
    protected String newValue;
    protected Service service;

    public ParameterChange() {
    }

    public ParameterChange(String name, String type, String oldValue, String newValue, Service service) {
        this.name = name;
        this.type = type;
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.service = service;
    }

    public ParameterChange(String name, String type, String oldValue, String newValue) {
        this.name = name;
        this.type = type;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    public String getPmpRoot() {
        return service.getPmpRoot();
    }

    @Override
    public ChangeType getChangeType() {
        return ChangeType.PARAMETER_CHANGE;
    }

    @Override
    public List<PersistableChange> apply(CommitDirector commitDirector) throws CommitException {

        if (!commitDirector.getChangeValidator().validateChange(this)) {
            return new ArrayList<>();
        }

        ParameterService parameterService = commitDirector.getParameterService();


        Object storedValue = parameterService.findParameterByName(name);
        // TODO: How specific should the error message be in the responses to the
        // client?
        if (storedValue == null) {
            // Do not do anything if the parameter does not exist on service
            return new ArrayList<>();
        }

        if (!type.equalsIgnoreCase(parameterService.getParameterTypeName(name))) {
            throw new TypeInconsistentException("Parameter types do not match.");
        }

        Object oldValueTyped = parameterService.getTypeParsers().parse(oldValue, type);
        Object newValueTyped = parameterService.getTypeParsers().parse(newValue, type);

        // Check if the value has changed since the commit was created
        if (!oldValueTyped.equals(storedValue)) {
            throw new OldValueInconsistentException(
                    "Old value inconsistent with stored value. Stored value has changed since the commit was created.");
        }

        parameterService.updateParameter(name, newValueTyped);

        List<PersistableChange> appliedChanges = new ArrayList<>();
        appliedChanges.add(this);
        return appliedChanges;
    }

    @Override
    public ChangeEntity toChangeEntity(ChangeEntityFactory changeEntityFactory) {
        return changeEntityFactory.createChangeEntity(this);
    }

    @Override
    public void undo(CommitDirector commitDirector) {
        ParameterService parameterService = commitDirector.getParameterService();

        Object oldValueTyped = parameterService.getTypeParsers().parse(oldValue, type);
        parameterService.updateParameter(name, oldValueTyped);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof ParameterChange && ((ParameterChange) obj).getName().equals(name)
                && ((ParameterChange) obj).getNewValue().equals(newValue)
                && ((ParameterChange) obj).getOldValue().equals(oldValue)
                && ((ParameterChange) obj).getType().equals(type);
    }

    @Override
    public int hashCode() {
        return name.hashCode() + newValue.hashCode() + oldValue.hashCode() + type.hashCode();
    }

    @Override
    public String toString() {
        return "ParameterChange{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", oldValue='" + oldValue + '\'' +
                ", newValue='" + newValue + '\'' +
                '}';
    }
}
