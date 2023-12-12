package dk.nykredit.pmp.core.commit;

import dk.nykredit.pmp.core.remote.json.raw_types.RawChange;
import dk.nykredit.pmp.core.remote.json.raw_types.RawCommitRevert;
import dk.nykredit.pmp.core.remote.json.raw_types.RawParameterChange;
import dk.nykredit.pmp.core.remote.json.raw_types.RawParameterRevert;

public interface ChangeFactory {
    Change createChange(RawChange rawChange);
    ParameterChange createChange(RawParameterChange rawChange);
    ParameterRevert createChange(RawParameterRevert rawChange);
    CommitRevert createChange(RawCommitRevert rawChange);
}
