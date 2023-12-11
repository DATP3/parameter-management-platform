import { ReactNode, useState } from 'react';

import { AuditFilter } from './types';
import { auditFilterContext } from './auditFilterContext';
import useSelectedServices from '../services/useSelectedServices';

/**
 * Provider for the audit filter context. Provides the audit filter and a function to update it.
 */
const AuditFilterProvider = ({ children }: { children: ReactNode }) => {
    const [auditFilter, setAuditFilter] = useState<AuditFilter>({});
    const [selectedServices] = useSelectedServices();
    const filterWithServices = { ...auditFilter, services: selectedServices };

    return (
        <auditFilterContext.Provider value={[filterWithServices, setAuditFilter]}>
            {children}
        </auditFilterContext.Provider>
    );
};

export default AuditFilterProvider;
