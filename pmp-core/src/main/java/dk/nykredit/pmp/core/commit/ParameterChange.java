package dk.nykredit.pmp.core.commit;

import com.fasterxml.jackson.annotation.JsonProperty;
import dk.nykredit.pmp.core.audit_log.ChangeEntity;
import dk.nykredit.pmp.core.audit_log.ParameterChangeEntityFactory;
import dk.nykredit.pmp.core.commit.exception.CommitException;
import dk.nykredit.pmp.core.commit.exception.OldValueInconsistentException;
import dk.nykredit.pmp.core.commit.exception.TypeInconsistentException;
import dk.nykredit.pmp.core.service.ParameterService;
import dk.nykredit.pmp.core.util.ChangeVisitor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ParameterChange implements Change {
    private String name;
    private String type;

    @JsonProperty("value")
    private String oldValue;
    private String newValue;
    private String id;

    private Service service;

    public ParameterChange() {
    }

    public ParameterChange(String name, String type, String oldValue, String newValue, Service service, String id) {
        this.name = name;
        this.type = type;
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.service = service;
        this.id = id;
    }

    @Override
    public List<ChangeEntity> apply(CommitDirector commitDirector) throws CommitException {
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

        ChangeEntity resultingChangeEntity = new ParameterChangeEntityFactory().createChangeEntity(this);

        return List.of(resultingChangeEntity);
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

    @Override
    public void acceptVisitor(ChangeVisitor changeVisitor) {
        changeVisitor.visit(this);
    }
}
