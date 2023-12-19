import { ParameterValue } from '../changes/types';

/** General parameter interface. All parameter values have a toString() function for display */
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
    BIGDECIMAL = 'BigDecimal',
    BOOLEAN = 'Boolean',
    LOCALDATE = 'LocalDate',
    LOCALDATETIME = 'LocalDateTime'
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
