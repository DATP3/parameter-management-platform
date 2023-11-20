import { Change, ParameterChange, ParameterRevert, Revert } from './types';

export const isParameterRevert = (revert: Revert): revert is ParameterRevert => {
    return revert.revertType === 'parameter';
};

export const isRevert = (change: Change): change is Revert => {
    return (change as Revert).revertType !== undefined;
};

export const isParameterChange = (change: Change): change is ParameterChange => {
    return !isRevert(change);
};

export const compareChanges = (a: Change, b: Change): number => {
    if (isRevert(a) && isRevert(b)) {
        return a.commitReference.localeCompare(b.commitReference);
    }

    if (isParameterChange(a) && isParameterChange(b)) {
        return compareParameterChanges(a, b);
    }

    if (isRevert(a)) {
        return -1;
    }

    return 1;
};

export const compareParameterChanges = (a: ParameterChange, b: ParameterChange): number => {
    const comparedServices = a.service.name.localeCompare(b.service.name);
    if (comparedServices !== 0) return comparedServices;

    return a.parameter.name.localeCompare(b.parameter.name);
};

export const compareReverts = (a: Revert, b: Revert): number => {
    const typeComparison = a.revertType.localeCompare(b.revertType);
    if (typeComparison !== 0) return typeComparison;

    return a.commitReference.localeCompare(b.commitReference);
};

export const areRevertsEqual = (a: Revert, b: Revert): boolean => {
    if (isParameterRevert(a) && isParameterRevert(b)) {
        return a.parameterName === b.parameterName;
    }

    return a.commitReference === b.commitReference && a.revertType === b.revertType;
};
