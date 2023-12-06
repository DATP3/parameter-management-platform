package dk.nykredit.pmp.core.audit_log;

import dk.nykredit.pmp.core.commit.Change;

public abstract class ChangeEntityFactory {
    protected AuditLogEntry auditLogEntry;

    public ChangeEntityFactory(AuditLogEntry auditLogEntry) {
        this.auditLogEntry = auditLogEntry;
    }

    public abstract ChangeEntity createChangeEntity(Change change);
}