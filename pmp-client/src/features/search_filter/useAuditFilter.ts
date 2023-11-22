import { useContext } from 'react';
import { auditFilterContext } from './auditFilterContext';
import { AuditFilterContextValue } from './types';

export const useAuditFilter = () => useContext(auditFilterContext) as AuditFilterContextValue;
