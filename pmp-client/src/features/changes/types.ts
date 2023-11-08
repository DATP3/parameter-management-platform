import { Parameter } from '../parameters/types';
import { Service } from '../services/types';

/**
 * Extendable type for parameter values.
 *
 * Will require changes if we want to support complex parameter values.
 */
export type ParameterValue = { toString(): string };

/**
 * A parameter change.
 *
 * The type of the parameter value is determined by the parameter key.
 */
export interface ParameterChange<T extends ParameterValue = ParameterValue> {
    parameter: Parameter<T>;
    newValue: T;
}

/**
 * A revert change.
 */
export interface Revert {
    commitReference: string;
}

/** A change entry with all changes for the service */
export interface ServiceChanges {
    service: Service;
    parameterChanges: ParameterChange[];
    reverts: Revert[];
}
