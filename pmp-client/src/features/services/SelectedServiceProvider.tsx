import { ReactNode, useEffect, useState } from 'react';

import { Service } from './types';
import { selectedServiceContext } from './selectedServiceContext';
import useEnvironment from '../environment/useEnvironment';

/**
 * Provider for the list of selected services.
 */
export const SelectedServiceProvider = ({ children }: { children: ReactNode }) => {
    const [services, setServices] = useState<Service[]>([]);
    const { environment } = useEnvironment();

    // Clear the list when the environment changes.
    useEffect(() => {
        setServices([]);
    }, [environment]);

    return (
        <selectedServiceContext.Provider value={[services, setServices]}>{children}</selectedServiceContext.Provider>
    );
};
