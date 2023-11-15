import { Parameter } from "../parameters/types";
import { ParameterFilter } from "./types";

const validateParameterFilterMatch = (filter: ParameterFilter, parameter: Parameter) => {
	const compareString = `${parameter.name}¤${parameter.type}¤${parameter.value.toString()}`;

	console.log(compareString, filter.searchQuery);
	let isMatching = true;
	if (filter.types) {
		isMatching = isMatching && filter.types.includes(parameter.type);
	}
	if (filter.searchQuery) {
		isMatching = isMatching && compareString.includes(filter.searchQuery);
	}
	return isMatching;
}

export default validateParameterFilterMatch;