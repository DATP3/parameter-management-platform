import { AuditLogEntry } from './useAuditLogEntries';
import { auditDetailsEntryContext } from './AuditDetailsEntryContext';

interface AuditDetailsEntryProviderProps {
    entry: AuditLogEntry;
    children: React.ReactNode;
}

/** Provides information about a single audit entry. Subscribe with useAuditDetailsEntry */
export const AuditDetailsEntryProvider = ({ entry, children }: AuditDetailsEntryProviderProps) => {
    return <auditDetailsEntryContext.Provider value={entry}>{children}</auditDetailsEntryContext.Provider>;
};
