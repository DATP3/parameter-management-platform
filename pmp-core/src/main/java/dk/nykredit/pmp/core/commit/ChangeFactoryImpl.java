package dk.nykredit.pmp.core.commit;

import dk.nykredit.pmp.core.remote.json.raw_types.RawChange;
import dk.nykredit.pmp.core.remote.json.raw_types.RawCommitRevert;
import dk.nykredit.pmp.core.remote.json.raw_types.RawParameterChange;
import dk.nykredit.pmp.core.remote.json.raw_types.RawParameterRevert;

public class ChangeFactoryImpl implements ChangeFactory{

    public Change createChange(RawChange rawChange) {
        if (rawChange instanceof RawParameterChange) {
            return createChange((RawParameterChange) rawChange);
        } else if (rawChange instanceof RawParameterRevert) {
            return createChange((RawParameterRevert) rawChange);
        } else if (rawChange instanceof RawCommitRevert) {
            return createChange((RawCommitRevert) rawChange);
        } else {
            throw new IllegalArgumentException("Unknown raw change type: " + rawChange.getClass().getName());
        }
    }

    public ParameterChange createChange(RawParameterChange rawChange) {
        return new ParameterChange(
            rawChange.getName(),
            rawChange.getType(),
            rawChange.getOldValue(),
            rawChange.getNewValue(),
            rawChange.getService(),
            rawChange.getId()
        );
    }

    public ParameterRevert createChange(RawParameterRevert rawChange) {
        return new ParameterRevert(
            rawChange.getParameterName(),
            rawChange.getCommitHash(),
            rawChange.getRevertType(),
            rawChange.getService()
        );
    }

    public CommitRevert createChange(RawCommitRevert rawChange) {
        return new CommitRevert(
            rawChange.getCommitHash()
        );
    }
}
