import { ReactNode, useCallback, useState } from 'react';

import { Service } from './types';
import { selectedServiceContext } from './selectedServiceContext';
import useEnvironment from '../environment/useEnvironment';

/**
 * Provider for the list of selected services.
 */
export const SelectedServiceProvider = ({ children }: { children: ReactNode }) => {
    const [selctedServicesRecord, setSelectedServicesRecord] = useState<Record<string, Service[]>>({});
    const { environment } = useEnvironment();

    const setSelectedServices = useCallback(
        (selectedServices: Service[]) => {
            setSelectedServicesRecord((servicesRecord) => ({
                ...servicesRecord,
                [environment]: selectedServices
            }));
        },
        [environment]
    );

    const selectedServices = selctedServicesRecord[environment] ?? [];

    return (
        <selectedServiceContext.Provider value={[selectedServices, setSelectedServices]}>
            {children}
        </selectedServiceContext.Provider>
    );
};
