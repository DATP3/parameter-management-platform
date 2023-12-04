package dk.nykredit.pmp.core.util;

import java.io.InputStream;

import javax.inject.Inject;

import com.fasterxml.jackson.databind.ObjectMapper;

import dk.nykredit.pmp.core.remote.json.ObjectMapperFactory;
public class ServiceInfoProviderImpl implements ServiceInfoProvider {
    
    private static ServiceInfo serviceInfo;
    private final String SERVICE_INFO_FILE = "service-info.json";

    @Inject
    ObjectMapperFactory objectMapperFactory;
    
    @Override
    public ServiceInfo getServiceInfo() {
        if (serviceInfo == null) {
            serviceInfo = readServiceFile();
        }
        return serviceInfo;
    }

    private ServiceInfo readServiceFile() {

        ObjectMapper mapper = objectMapperFactory.getObjectMapper();


        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(SERVICE_INFO_FILE);

        try {
            
            ServiceInfo serviceInfo = mapper.readValue(inputStream, ServiceInfo.class);
            return serviceInfo;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
        
    }
}
