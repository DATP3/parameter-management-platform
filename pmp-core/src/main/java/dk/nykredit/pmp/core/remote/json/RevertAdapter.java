package dk.nykredit.pmp.core.remote.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
// TODO: Delete this when we put services on this object
public class RevertAdapter {
    private String parameterName;
    private String revertType;
    private String commitReference;
}
