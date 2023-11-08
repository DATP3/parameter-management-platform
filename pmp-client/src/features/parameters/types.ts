export interface Parameter<T> {
    id: string;
    name: string;
    type: string;
    value: T;
}

export interface ParameterResponse {
    parameters: Parameter<unknown>[];
}
