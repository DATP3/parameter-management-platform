package dk.nykredit.pmp.core.commit;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Environment {
    private String environment;

    public Environment() {};

    public Environment(String environment) {
        this.environment = environment;
    }
}
