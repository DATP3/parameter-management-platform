package dk.nykredit.pmp.core.commit;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Service {
    private String name;
    @JsonProperty("address")
    private String pmpRoot;
    private Environment environment;

    public Service() {
    }

    public Service(String name, String pmpRoot, String environment) {
        this.name = name;
        this.pmpRoot = pmpRoot;
        this.environment = new Environment(environment);
    }

    @Override
    public String toString() {
        return "Service{" +
                "name='" + name + '\'' +
                ", pmpRoot='" + pmpRoot + '\'' +
                ", environment=" + environment +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Service)) return false;

        Service service = (Service) o;

        if (name != null ? !name.equals(service.name) : service.name != null) return false;
        if (pmpRoot != null ? !pmpRoot.equals(service.pmpRoot) : service.pmpRoot != null) return false;
        return environment != null ? environment.equals(service.environment) : service.environment == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (pmpRoot != null ? pmpRoot.hashCode() : 0);
        result = 31 * result + (environment != null ? environment.hashCode() : 0);
        return result;
    }
}
