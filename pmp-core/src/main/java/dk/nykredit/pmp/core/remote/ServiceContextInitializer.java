package dk.nykredit.pmp.core.remote;

import dk.nykredit.pmp.core.util.ServiceInfo;
import dk.nykredit.pmp.core.util.ServiceInfoProvider;

import javax.inject.Inject;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ServiceContextInitializer implements ServletContextListener {

    @Inject
    TrackerService trackerService;

    @Inject
    ServiceInfoProvider serviceInfoProvider;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ServiceInfo serviceInfo = serviceInfoProvider.getServiceInfo();

        // Announce the service to the tracker
        try {
            trackerService.announce(serviceInfo.getPmpRoot(), serviceInfo.getName(), serviceInfo.getEnvironment());
        } catch (Exception e) {
            throw new Error(e);
        }

    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
    }
}
