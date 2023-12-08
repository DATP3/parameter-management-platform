import { TRACKER_URL } from '../../config';
import axios from 'axios';
import useEnvironment from '../environment/useEnvironment';
import { useMsal } from '@azure/msal-react';
import { useQuery } from '@tanstack/react-query';
import { z } from 'zod';

const rawServicesParser = z.object({
    environment: z.string(),
    services: z.array(
        z.object({
            name: z.string(),
            pmpRoot: z.string()
        })
    )
});

const servicesParser = z.array(
    z.object({
        name: z.string(),
        address: z.string(),
        environment: z.object({ environment: z.string() })
    })
);

type RawServiceData = z.infer<typeof rawServicesParser>;
type ServiceData = z.infer<typeof servicesParser>;

const adaptServiceData = (data: RawServiceData): ServiceData => {
    const { environment, services } = data;
    return services.map((s) => ({
        name: s.name,
        address: s.pmpRoot,
        environment: { environment }
    }));
};

/**
 * Get a list of services for the current environment.
 * @returns A UseQueryResult object, identical to the one returned by react-query's useQuery hook.
 *
 * TODO: Remove mock data and as such, the filter.
 */
const useServices = () => {
    const { environment } = useEnvironment();
    const { accounts } = useMsal();

    return useQuery<ServiceData>({
        queryKey: ['services', environment],
        queryFn: async () => {
            const token = accounts[0].idToken;
            if (!token) throw new Error('No token');
            const result = await axios.get(`${TRACKER_URL}/services`, {
                headers: {
                    'pmp-environment': environment,
                    Authorization: `Bearer ${token}`
                }
            });
            const data = rawServicesParser.parse(result.data);
            const adaptedData = adaptServiceData(data);
            return adaptedData;
        },
        select: (data) => {
            return data.filter((s) => s.environment.environment === environment);
        }
    });
};

export default useServices;
