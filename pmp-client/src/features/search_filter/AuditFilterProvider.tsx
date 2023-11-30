import { ReactNode, useState } from 'react';
import { AuditFilter } from './types';
import { auditFilterContext } from './auditFilterContext';
import useSelectedServices from '../services/useSelectedServices';

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
