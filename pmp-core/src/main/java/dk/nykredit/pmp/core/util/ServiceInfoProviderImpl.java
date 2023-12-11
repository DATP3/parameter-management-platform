package dk.nykredit.pmp.core.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import dk.nykredit.pmp.core.remote.json.ObjectMapperFactory;

import javax.inject.Inject;
import java.io.InputStream;

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

            // Overwrite with environment variables if they exist to allow for easier
            // mass-deployment with different configurations
            String envPmpRoot = System.getenv(SystemEnvKeys.SERVICE_INFO_PMPROOT);
            String envEnvironment = System.getenv(SystemEnvKeys.SERVICE_INFO_ENVIRONMENT);
            String envName = System.getenv(SystemEnvKeys.SERVICE_INFO_NAME);
            if (envPmpRoot != null) {
                serviceInfo.setPmpRoot(envPmpRoot);
            }
            if (envEnvironment != null) {
                serviceInfo.setEnvironment(envEnvironment);
            }
            if (envName != null) {
                serviceInfo.setName(envName);
            }

            return serviceInfo;
        } catch (Exception e) {
            throw new Error(e);
        }
    }
}
