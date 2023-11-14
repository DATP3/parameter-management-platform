import { ParameterType } from '../../features/parameters/types';

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
    Value = 'value'
}

export const InputTextFieldTypes = [
    ParameterType.STRING,
    ParameterType.CHARACTER,
    ParameterType.INTEGER,
    ParameterType.LONG,
    ParameterType.SHORT,
    ParameterType.BYTE,
    ParameterType.FLOAT,
    ParameterType.DOUBLE,
    ParameterType.BIGDECIMAL
];
