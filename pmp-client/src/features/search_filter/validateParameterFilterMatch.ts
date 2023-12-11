import { Parameter } from '../parameters/types';
import { ParameterFilter } from './types';

/**
 * Checks if the provided parameter matches the filter
 * @param filter The parameter filter to check against
 * @param entry The parameter to check
 * @returns true if it matches, false otherwise
 */
const validateParameterFilterMatch = (filter: ParameterFilter, parameter: Parameter) => {
    const compareString = `${parameter.name}¤${parameter.type}¤${parameter.value.toString()}`;

    let isMatching = true;
    if (filter.types) {
        isMatching = isMatching && filter.types.includes(parameter.type);
    }
    if (filter.searchQuery) {
        isMatching = isMatching && compareString.includes(filter.searchQuery);
    }
    return isMatching;
};

export default validateParameterFilterMatch;
