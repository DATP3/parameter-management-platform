import { ParameterValue } from '../changes/types';

export interface Parameter<T extends ParameterValue = ParameterValue> {
    id: string;
    name: string;
    type: ParameterType;
    value: T;
}

export interface ParameterResponse {
    parameters: Parameter[];
}

export enum ParameterType {
    STRING = 'String',
    CHARACTER = 'Character',
    INTEGER = 'Integer',
    LONG = 'Long',
    SHORT = 'Short',
    BYTE = 'Byte',
    FLOAT = 'Float',
    DOUBLE = 'Double',
    BIGDECIMAL = 'Bigdecimal',
    BOOLEAN = 'Boolean',
    LOCALDATE = 'Localdate',
    LOCALDATETIME = 'Localdatetime'
}

export interface ParameterFilter {
    types?: ParameterType[];
    name?: string;
    value?: ParameterValue;
}

export interface ParameterSortingOption {
    ascending: boolean;
    option: SortingCatagory;
}
export enum SortingCatagory {
    TYPE = 'type',
    NAME = 'name',
    VALUE = 'value'
}

export const TextTypes = ['string', 'character'];

export const InputTextFieldTypes = [
    'string',
    'character',
    'integer',
    'long',
    'short',
    'byte',
    'float',
    'double',
    'bigdecimal'
];
