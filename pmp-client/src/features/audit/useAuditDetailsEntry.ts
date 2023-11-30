import { AuditLogEntry } from './useAuditLogEntries';
import { auditDetailsEntryContext } from './AuditDetailsEntryContext';
import { useContext } from 'react';

export const useAuditDetailsEntry = () => useContext(auditDetailsEntryContext) as AuditLogEntry;
