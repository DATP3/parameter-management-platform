package dk.nykredit.pmp.core.util;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServiceInfo {
    
    private String pmpRoot;
    private String name;
    private String environment;

    public String getPmpRoot() {
        return pmpRoot;
    }

    public void setPmpRoot(String pmpRoot) {
        this.pmpRoot = pmpRoot;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return ("pmpRoot: " + pmpRoot + ", name: " + name);
    }
}
