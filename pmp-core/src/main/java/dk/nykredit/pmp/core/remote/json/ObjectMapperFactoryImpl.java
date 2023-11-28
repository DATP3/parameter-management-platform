package dk.nykredit.pmp.core.remote.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import dk.nykredit.pmp.core.commit.Change;

import javax.inject.Singleton;
import java.text.SimpleDateFormat;

@Singleton
public class ObjectMapperFactoryImpl implements ObjectMapperFactory {

    private final ObjectMapper mapper = new ObjectMapper();

    public ObjectMapperFactoryImpl() {
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Change.class, new ChangeDeserializer());
        mapper.registerModule(module);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mapper.setDateFormat(dateFormat);
    }
    public ObjectMapper getObjectMapper() {
        return mapper;
    }

}
