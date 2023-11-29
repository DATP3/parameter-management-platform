package dk.nykredit.pmp.core.commit;

import dk.nykredit.pmp.core.service.ParameterService;

public interface Change {
	boolean apply(ParameterService parameterService);

	void undo(ParameterService parameterService);
}
