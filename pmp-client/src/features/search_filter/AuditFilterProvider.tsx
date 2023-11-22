import { ReactNode, useState } from 'react';
import { AuditFilter } from './types';
import { auditFilterContext } from './auditFilterContext';

const AuditFilterProvider = ({ children }: { children: ReactNode }) => {
    const [auditFilter, setAuditFilter] = useState<AuditFilter>({});

    return <auditFilterContext.Provider value={[auditFilter, setAuditFilter]}>{children}</auditFilterContext.Provider>;
};

export default AuditFilterProvider;
