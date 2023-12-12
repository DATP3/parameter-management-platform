package dk.nykredit.pmp.core.remote.json.raw_types;

import com.fasterxml.jackson.annotation.JsonProperty;

import dk.nykredit.pmp.core.commit.Service;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RawParameterRevert implements RawChange {
    private String parameterName;
    @JsonProperty("commitReference")
    private long commitHash;
    private String revertType;
    private Service service;

    public RawParameterRevert() {
    }

    public RawParameterRevert(String parameterName, String revertType, String commitHash, Service service) {
        this.parameterName = parameterName;
        setCommitHash(commitHash);
        this.revertType = revertType;
    }

    public void setCommitHash(String commitHash) {
        this.commitHash = Long.parseUnsignedLong(commitHash, 16);
    }

    @Override
    public String toString() {
        return "RawParameterRevert{" +
                "parameterName='" + parameterName + '\'' +
                ", commitHash=" + commitHash +
                ", revertType='" + revertType + '\'' +
                ", service=" + service +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof RawParameterRevert))
            return false;

        RawParameterRevert that = (RawParameterRevert) o;

        if (commitHash != that.commitHash)
            return false;
        if (parameterName != null ? !parameterName.equals(that.parameterName) : that.parameterName != null)
            return false;
        if (revertType != null ? !revertType.equals(that.revertType) : that.revertType != null)
            return false;
        return service != null ? service.equals(that.service) : that.service == null;
    }

    @Override
    public int hashCode() {
        int result = parameterName != null ? parameterName.hashCode() : 0;
        result = 31 * result + (int) (commitHash ^ (commitHash >>> 32));
        result = 31 * result + (revertType != null ? revertType.hashCode() : 0);
        result = 31 * result + (service != null ? service.hashCode() : 0);
        return result;
    }
}
