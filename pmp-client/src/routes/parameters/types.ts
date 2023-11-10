
export interface Parameter<T> {
	id: string;
	service: string;
	name: string;
	type: string;
	value: T;
}

export interface ParameterResponse {
	parameters: Parameter<unknown>[];
}


export enum SortingOption {
	Type = 'type',
	Name = 'name',
	Value = 'value',
}

export const InputTextFieldTypes = [
	"string",
	"character",
	"integer",
	"long",
	"short",
	"byte",
	"float",
	"double",
	"bigdecimal"
]

