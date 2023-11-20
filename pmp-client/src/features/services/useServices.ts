import { Environment } from '../environment/environment';
import axios from 'axios';
import useEnvironment from '../environment/useEnvironment';
import { useQuery } from '@tanstack/react-query';
import { z } from 'zod';
import { useMsalAuthentication } from '@azure/msal-react';
import { InteractionType } from '@azure/msal-browser';

const servicesParser = z.array(
    z.object({
        name: z.string(),
        address: z.string(),
        environment: z.nativeEnum(Environment)
    })
);

/**
 * Get a list of services for the current environment.
 * @returns A UseQueryResult object, identical to the one returned by react-query's useQuery hook.
 *
 * TODO: Remove mock data and as such, the filter.
 */
const useServices = () => {
    const { environment } = useEnvironment();
    const { acquireToken } = useMsalAuthentication(InteractionType.Redirect);

    return useQuery({
        queryKey: ['services', environment],
        queryFn: async () => {
            const token = await acquireToken();
            if (!token) throw new Error('No token');

            const result = await axios.get('/mock/services.json', {
                headers: {
                    'pmp-environment': environment,
                    Authorization: `Bearer ${token.accessToken}`
                }
            });
            const data = servicesParser.parse(result.data);
            return data;
        },
        select: (data) => {
            return data.filter((s) => s.environment === environment);
        }
    });
};

export default useServices;
