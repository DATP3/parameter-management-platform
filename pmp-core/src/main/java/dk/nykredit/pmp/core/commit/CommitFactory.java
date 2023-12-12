package dk.nykredit.pmp.core.commit;

import dk.nykredit.pmp.core.remote.json.raw_types.RawCommit;

public interface CommitFactory {
    Commit createCommit(RawCommit rawCommit);
}
