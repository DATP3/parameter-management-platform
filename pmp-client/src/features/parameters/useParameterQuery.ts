import { ParameterType } from './types';
import { Service } from '../services/types';
import useSimpleQuery from '../../features/network/useSimpleQuery';
import { z } from 'zod';

const parameterParser = z.object({
    parameters: z.array(
        z.object({
            id: z.string(),
            name: z.string(),
            type: z.nativeEnum(ParameterType),
            value: z.union([z.string(), z.number(), z.boolean(), z.date()])
        })
    )
});

/**
 * Get a list of parameters for checked services.
 * @returns A UseQueryResult object.
 *
 * TODO: Remove mock data.
 */
const useParameterQuery = (service: Service) => {
    // TODO: Use real data. Test is set up to intercept
    // return useSimpleQuery(['parameters', service.name], `http://${service.address}/pmp/parameters`, parameterParser);
    return useSimpleQuery(['parameters', service.name], `/mock/parameters/${service.address}.json`, parameterParser);
};

export default useParameterQuery;
