import { TRACKER_URL } from '../../config';
import useSimpleQuery from '../../features/network/useSimpleQuery';
import { z } from 'zod';

// Client expects an array of objects with an environment property, but tracker provides an array of strings.
// Parser transforms to adapt data to client expectations.
const rawEnvironmentParser = z
    .object({
        environmentNames: z.string().array()
    })
    .transform((e) => e.environmentNames.map((e) => ({ environment: e })));

/**
 * Get a list of environments.
 * @returns A UseQueryResult object.
 *
 */
const useEnvironmentQuery = () => {
    return useSimpleQuery(['environments'], `${TRACKER_URL}/environment`, rawEnvironmentParser);
};

export default useEnvironmentQuery;
