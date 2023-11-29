package dk.nykredit.pmp.core.persistence;


import java.util.Map;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import dk.nykredit.pmp.core.attribute.converter.StringToObjectConverter;
import lombok.Getter;
import lombok.Setter;
import org.eclipse.jetty.util.ajax.JSON;


@Entity
@Table(name = "PARAMETER_MANAGEMENT")
@Getter
@Setter
public class ParameterEntity implements JSON.Convertible {
    @Id
    @Column(name = "ID", length = 36)
    private String id;

    @Column(name = "NAME", unique = true)
    private String name;

    @Column(name = "TYPE")
    private String type;


    @Column(name = "PVALUE", length = 4000)
    @Convert(converter = StringToObjectConverter.class)
    private Object pValue;

    @PrePersist
    public void generatedId() {
        if (this.id == null) {
            this.id = UUID.randomUUID().toString();
        }
    }

    @Override
    public void toJSON(JSON.Output output) {
        output.add("name", this.getName());
        // Is this evil?
        output.add("value", this.getPValue().toString());
        output.add("type", this.getType());
        output.add("id", this.getId());
    }

    @Override
    public void fromJSON(Map map) {
        this.setName((String) map.get("name"));
        // TODO: i am 98% sure that this will crash like instantly but we aren't using this function yet
        // we should really just use the converters from `EntityParser`
        this.setPValue(map.get("value"));
        this.setType((String) map.get("type"));
        this.setId((String) map.get("id"));
    }
}
