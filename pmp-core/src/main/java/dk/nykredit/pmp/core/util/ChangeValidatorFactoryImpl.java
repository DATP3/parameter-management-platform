package dk.nykredit.pmp.core.util;

import javax.inject.Inject;

import dk.nykredit.pmp.core.audit_log.AuditLog;

public class ChangeValidatorFactoryImpl implements ChangeValidatorFactory {

    @Inject
    AuditLog auditLog;

    @Inject
    ServiceInfoProvider serviceInfoProvider;

    /**
     * Creates a new instance of {@link ChangeValidatorImpl} to make sure the
     * validatedChanges list is empty, while still allowing access to the
     * {@link AuditLog}
     * and {@link ServiceInfo}.
     * 
     * @return a new instance of {@link ChangeValidatorImpl}
     */
    @Override
    public ChangeValidator createChangeValidator() {
        return new ChangeValidatorImpl(auditLog, serviceInfoProvider.getServiceInfo());
    }
}
