import { createContext } from 'react';
import { AuditFilterContextValue } from './types';

export const auditFilterContext = createContext<AuditFilterContextValue | null>(null);
