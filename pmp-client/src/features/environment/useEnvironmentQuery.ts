import useSimpleQuery from '../../features/network/useSimpleQuery';
import { z } from 'zod';

// Client expects an array of objects with an environment property, but tracker provides an array of strings.
// Parser transforms to adapt data to client expectations.
const rawEnvironmentParser = z
    .string()
    .array()
    .transform((e) => e.map((e) => ({ environment: e })));

/**
 * Get a list of environments.
 * @returns A UseQueryResult object.
 *
 * TODO: Remove mock data.
 */
const useEnvironmentQuery = () => {
    // TODO: Use real data. Test is set up to intercept
    // return useSimpleQuery(['environments'], `${TRACKER_URL}/environment`, rawEnvironmentParser);
    return useSimpleQuery(['environments'], '/mock/environments.json', rawEnvironmentParser);
};

export default useEnvironmentQuery;
