package dk.nykredit.pmp.core.remote.json;

import com.fasterxml.jackson.databind.ObjectMapper;

public interface ObjectMapperFactory {
    ObjectMapper getObjectMapper();
}
