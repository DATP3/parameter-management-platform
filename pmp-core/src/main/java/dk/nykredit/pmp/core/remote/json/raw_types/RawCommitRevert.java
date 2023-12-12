package dk.nykredit.pmp.core.remote.json.raw_types;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties("affectedServices")
public class RawCommitRevert implements RawChange {

    @JsonProperty("commitReference")
    private long commitHash;
    private String revertType;

    public RawCommitRevert() {
    }

    public void setCommitHash(String commitHash) {
        this.commitHash = Long.parseUnsignedLong(commitHash, 16);
    }

    @Override
    public String toString() {
        return "RawCommitRevert{" +
                "commitHash=" + commitHash +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof RawCommitRevert))
            return false;

        RawCommitRevert that = (RawCommitRevert) o;

        return commitHash == that.commitHash;
    }

    @Override
    public int hashCode() {
        return (int) (commitHash ^ (commitHash >>> 32));
    }
}
