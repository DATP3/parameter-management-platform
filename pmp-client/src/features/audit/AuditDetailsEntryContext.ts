import { AuditLogEntry } from './useAuditLogEntries';
import { createContext } from 'react';

/** Context for a single audit entry */
export const auditDetailsEntryContext = createContext<AuditLogEntry | null>(null);
