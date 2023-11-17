import { ReactNode, useCallback, useState } from 'react';

import { Service } from './types';
import { selectedServiceContext } from './selectedServiceContext';
import useEnvironment from '../environment/useEnvironment';
import useServices from './useServices';

/**
 * Provider for the list of selected services.
 */
export const SelectedServiceProvider = ({ children }: { children: ReactNode }) => {
    const { data: services } = useServices();
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
	
	let selectedServices = selctedServicesRecord[environment] ?? [];
	
	if (services && services.length > 0 && services[1].name) {
		selectedServices = selctedServicesRecord[environment] ?? services;
	} 

    return (
        <selectedServiceContext.Provider value={[selectedServices, setSelectedServices]}>
            {children}
        </selectedServiceContext.Provider>
    );
};
