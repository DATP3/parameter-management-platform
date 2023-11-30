import { AuditLogEntry } from './useAuditLogEntries';
import { createContext } from 'react';

export const auditDetailsEntryContext = createContext<AuditLogEntry | null>(null);
