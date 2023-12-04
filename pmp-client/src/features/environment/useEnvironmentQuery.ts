import useSimpleQuery from '../../features/network/useSimpleQuery';
import { z } from 'zod';

const environmentParser = z.array(
    z.object({
        environment: z.string()
    })
);

/**
 * Get a list of environments.
 * @returns A UseQueryResult object.
 *
 * TODO: Remove mock data.
 */
const useEnvironmentQuery = () => {
    // TODO: Use real data. Test is set up to intercept
    // return useSimpleQuery(['environments'], `${TRACKER_URL}/environment`, environmentParser);
    return useSimpleQuery(['environments'], '/mock/environments.json', environmentParser);
};

export default useEnvironmentQuery;
