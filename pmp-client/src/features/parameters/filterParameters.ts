import { Parameter, ParameterFilter, ParameterType } from './types';

/**
 * Filters a list of parameters based on a filter object.
 * @param parameters The parameters to filter
 * @param filter The filter options to apply
 * @returns a filtered list of parameters
 */
const filterParameters = (parameters: Parameter[], filter: ParameterFilter) => {
    return parameters.filter((parameter) => {
        let isMatching = true;
        if (filter.types) {
            isMatching = isMatching && filter.types.includes(parameter.type as ParameterType);
        }
        if (filter.name) {
            isMatching = isMatching && parameter.name.includes(filter.name);
        }
        if (filter.value) {
            isMatching = isMatching && parameter.value === filter.value;
        }
        return isMatching;
    });
};

export default filterParameters;
