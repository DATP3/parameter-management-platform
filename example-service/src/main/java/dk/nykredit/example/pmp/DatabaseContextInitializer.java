package dk.nykredit.example.pmp;

import dk.nykredit.pmp.core.persistence.ParameterEntity;
import dk.nykredit.pmp.core.service.ParameterService;

import javax.inject.Inject;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.IOException;
import java.util.List;

public class DatabaseContextInitializer implements ServletContextListener {

    @Inject
    private ParameterService parameterService;

    @Inject
    private InitialParameterReader initialParameterReader;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        try {
            List<ParameterEntity> params = initialParameterReader.readParameters();
            for (ParameterEntity p : params) {
                parameterService.persistParameter(p.getName(), p.getPValue());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
