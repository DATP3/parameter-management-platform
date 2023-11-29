package dk.nykredit.pmp.core.commit;

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
	public boolean apply(ParameterService parameterService) {
		Object storedValue = parameterService.findParameterByName(name);
		if (storedValue == null) {
			return false;
		}

		if (!type.equalsIgnoreCase(parameterService.getParameterTypeName(name))) {
			return false;
		}

		Object oldValueTyped = parameterService.getTypeParsers().parse(oldValue, type);
		Object newValueTyped = parameterService.getTypeParsers().parse(newValue, type);

		// Check if the value has changed since the commit was created
		if (!oldValueTyped.equals(storedValue)) {
			return false;
		}

		parameterService.updateParameter(name, newValueTyped);
		return true;
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
