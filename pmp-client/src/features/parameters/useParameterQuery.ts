import { Service } from '../services/types';
import useSimpleQuery from '../../features/network/useSimpleQuery';
import { z } from 'zod';
import { ParameterType } from './types';

const parameterParser = z.array(
    z.object({
        id: z.string(),
        name: z.string(),
        type: z.nativeEnum(ParameterType),
        value: z.union([z.string(), z.number(), z.boolean(), z.date()])
    })
);

/**
 * Get a list of parameters for checked services.
 * @returns A UseQueryResult object.
 *
 * TODO: Remove mock data.
 */
const useParameterQuery = (service: Service) => {
    return useSimpleQuery(['parameters', service.name], '/mock/parameters.json', parameterParser);
};

export default useParameterQuery;
