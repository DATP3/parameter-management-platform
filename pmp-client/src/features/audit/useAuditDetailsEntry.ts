import { AuditLogEntry } from './useAuditLogEntries';
import { auditDetailsEntryContext } from './AuditDetailsEntryContext';
import { useContext } from 'react';

/** Get the current entry in teh audit log. Requires context */
export const useAuditDetailsEntry = () => useContext(auditDetailsEntryContext) as AuditLogEntry;
