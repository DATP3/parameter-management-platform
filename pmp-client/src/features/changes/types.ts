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
    service: Service;
}

interface IRevert {
    commitReference: string;
}

export interface CommitRevert extends IRevert {
    revertType: 'commit';
}

export interface ParameterRevert extends IRevert {
    revertType: 'parameter';
    parameterName: string; // TODO: Check if this can feasibly be a full parameter instead
    service: Service;
}

/**
 * A revert change.
 */
export type Revert = CommitRevert | ParameterRevert;

export type Change = ParameterChange | Revert;

/** A change entry with all changes for the service */
export interface ServiceChanges {
    service: Service;
    parameterChanges: ParameterChange[];
    reverts: Revert[];
}
