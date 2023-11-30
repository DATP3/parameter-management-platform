package dk.nykredit.pmp.core.commit;

import dk.nykredit.pmp.core.service.ParameterService;
import dk.nykredit.pmp.core.commit.exception.CommitException;

public interface Change {
    void apply(ParameterService parameterService) throws CommitException;

    void undo(ParameterService parameterService);
}
