package dk.nykredit.pmp.core.commit;

import dk.nykredit.pmp.core.remote.json.raw_types.RawCommit;

public interface CommitFactory {
    /**
     * Creates a Commit object from a RawCommit object.
     * 
     * @param rawCommit the raw commit to be converted to a Commit object.
     * @return a Commit object with validated changes.
     */
    Commit createCommit(RawCommit rawCommit);
}
