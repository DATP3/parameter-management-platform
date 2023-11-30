package dk.nykredit.pmp.core.commit;

import dk.nykredit.pmp.core.commit.exception.CommitException;
import dk.nykredit.pmp.core.commit.exception.OldValueInconsistentException;
import dk.nykredit.pmp.core.commit.exception.StoredValueNullException;
import dk.nykredit.pmp.core.commit.exception.TypeInconsistentException;
import dk.nykredit.pmp.core.service.ParameterService;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParameterChange implements Change {
    private String name;
    private String type;
    private String oldValue;
    private String newValue;

    @Override
    public void apply(ParameterService parameterService) throws CommitException {
        Object storedValue = parameterService.findParameterByName(name);
        // TODO: How specific should the error message be in the responses to the client?
        if (storedValue == null) {
            throw new StoredValueNullException("Parameter with name " + name + " does not exist.");
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
    }

    @Override
    public void undo(ParameterService parameterService) {
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
