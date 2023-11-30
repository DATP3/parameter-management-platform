import { AuditLogEntry } from './useAuditLogEntries';
import { auditDetailsEntryContext } from './AuditDetailsEntryContext';

interface AuditDetailsEntryProviderProps {
    entry: AuditLogEntry;
    children: React.ReactNode;
}

export const AuditDetailsEntryProvider = ({ entry, children }: AuditDetailsEntryProviderProps) => {
    return <auditDetailsEntryContext.Provider value={entry}>{children}</auditDetailsEntryContext.Provider>;
};
