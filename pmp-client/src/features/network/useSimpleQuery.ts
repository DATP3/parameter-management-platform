import { QueryKey, UseQueryResult, useQuery } from '@tanstack/react-query';

import axios from 'axios';
import useEnvironment from '../environment/useEnvironment';
import { useMsal } from '@azure/msal-react';
import { z } from 'zod';

/**
 * An abstraction over react-query's useQuery hook, which makes it easier to make simple, validated get requests
 * @param queryKey The query key to use for the request. This is used to identify the request in react-query's cache.
 * @param url The URL to make the request to.
 * @param parser An optional parser to use to validate the response. If not provided, the response will not be validated.
 *
 * @returns A UseQueryResult object, identical to the one returned by react-query's useQuery hook.
 */
function useSimpleQuery<TData>(queryKey: QueryKey, url: string): UseQueryResult<TData>;
function useSimpleQuery<TParser extends z.ZodType>(
    queryKey: QueryKey,
    url: string,
    parser: TParser
): UseQueryResult<z.infer<TParser>>;
function useSimpleQuery<TData>(queryKey: QueryKey, url: string, parser?: z.ZodType<TData>) {
    const { environment } = useEnvironment();
    const { accounts } = useMsal();

    return useQuery({
        queryKey,
        queryFn: async () => {
            const token = accounts[0].idToken;
            if (!token) throw new Error('No token');

            const response = await axios.get<TData>(url, {
                headers: {
                    'pmp-environment': environment,
                    Authorization: `Bearer ${token}`
                }
            });
            if (parser) return parser.parse(response.data);
            return response.data;
        }
    });
}

export default useSimpleQuery;
