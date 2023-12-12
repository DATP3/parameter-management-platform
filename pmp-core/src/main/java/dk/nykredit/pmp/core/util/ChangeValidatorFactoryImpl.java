package dk.nykredit.pmp.core.util;

import javax.inject.Inject;

import dk.nykredit.pmp.core.audit_log.AuditLog;

public class ChangeValidatorFactoryImpl implements ChangeValidatorFactory {

    @Inject
    AuditLog auditLog;

    @Inject
    ServiceInfoProvider serviceInfoProvider;

    @Override
    public ChangeValidator createChangeValidator() {
        return new ChangeValidatorImpl(auditLog, serviceInfoProvider.getServiceInfo());
    }

}
