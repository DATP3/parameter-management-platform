package dk.nykredit.pmp.core.commit;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Service {

    private String name;
    private String address;
    private Environment environment;

    public Service() {
        
    }

    public Service (String name, String address, Environment environment) {
        this.address = address;
        this.environment = environment;
        this.name = name;
    }

    public String getPmpRoot() {
        return address;
    }
    
}
