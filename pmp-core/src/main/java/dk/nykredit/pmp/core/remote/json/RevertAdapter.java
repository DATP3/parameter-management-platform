package dk.nykredit.pmp.core.remote.json;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RevertAdapter {
    private String parameterName;
    private String revertType;
    private String commitReference;
}
