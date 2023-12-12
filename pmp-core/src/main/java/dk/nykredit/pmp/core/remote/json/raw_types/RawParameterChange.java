package dk.nykredit.pmp.core.remote.json.raw_types;

import com.fasterxml.jackson.annotation.JsonProperty;

import dk.nykredit.pmp.core.commit.Service;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RawParameterChange implements RawChange {
    private String name;
    private String type;
    private String id;

    @JsonProperty("value")
    private String oldValue;
    private String newValue;

    private Service service;

    public RawParameterChange(String name, String type, String oldValue, String newValue, Service service) {
        this.name = name;
        this.type = type;
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.service = service;
    }

    public RawParameterChange() {
    }

    @Override
    public String toString() {

        return "RawParameterChange{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", id='" + id + '\'' +
                ", oldValue='" + oldValue + '\'' +
                ", newValue='" + newValue + '\'' +
                ", service=" + service +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RawParameterChange)) return false;

        RawParameterChange that = (RawParameterChange) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (oldValue != null ? !oldValue.equals(that.oldValue) : that.oldValue != null) return false;
        if (newValue != null ? !newValue.equals(that.newValue) : that.newValue != null) return false;
        return service != null ? service.equals(that.service) : that.service == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (oldValue != null ? oldValue.hashCode() : 0);
        result = 31 * result + (newValue != null ? newValue.hashCode() : 0);
        result = 31 * result + (service != null ? service.hashCode() : 0);
        return result;
    }
}
