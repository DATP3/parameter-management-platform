package dk.nykredit.pmp.core.commit;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Environment {
    String environment;

    public Environment() {
    }

    public Environment(String environment) {
        this.environment = environment;
    }

    @Override
    public String toString() {
        return "Environment{" +
                "environment='" + environment + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Environment)) return false;

        Environment that = (Environment) o;

        return environment != null ? environment.equals(that.environment) : that.environment == null;
    }

    @Override
    public int hashCode() {
        return environment != null ? environment.hashCode() : 0;
    }
}
