import { Change, CommitRevert, ParameterChange, ParameterRevert, Revert } from './types';

export const isParameterRevert = (revert: Revert): revert is ParameterRevert => {
    return revert.revertType === 'parameter';
};

export const isCommitRevert = (revert: Revert): revert is CommitRevert => {
    return revert.revertType === 'commit';
};

export const isServiceRevert = (revert: Revert): revert is ParameterRevert => {
    return revert.revertType === 'service';
};

export const isRevert = (change: Change): change is Revert => {
    return (change as Revert).revertType !== undefined;
};

export const isParameterChange = (change: Change): change is ParameterChange => {
    return !isRevert(change);
};

interface CompareChangesOptions {
    ignoreValue?: boolean;
}

export const compareChanges = (a: Change, b: Change, options?: CompareChangesOptions): number => {
    if (isRevert(a) && isRevert(b)) {
        return compareReverts(a, b);
    }

    if (isParameterChange(a) && isParameterChange(b)) {
        return compareParameterChanges(a, b, options);
    }

    if (isRevert(a)) {
        return -1;
    }

    return 1;
};

export const compareParameterChanges = (
    a: ParameterChange,
    b: ParameterChange,
    options?: CompareChangesOptions
): number => {
    const comparedServices = a.service.name.localeCompare(b.service.name);
    if (comparedServices !== 0) return comparedServices;

    const comparedNames = a.parameter.name.localeCompare(b.parameter.name);
    if (comparedNames !== 0 || options?.ignoreValue) return comparedNames;

    // TODO: Implement a better value comparison
    return a.newValue.toString().localeCompare(b.newValue.toString());
};

export const compareReverts = (a: Revert, b: Revert): number => {
    const typeComparison = a.revertType.localeCompare(b.revertType);
    if (typeComparison !== 0) return typeComparison;

    if (isServiceRevert(a) && isServiceRevert(b)) {
        const comparedServices = a.service.name.localeCompare(b.service.name);
        if (comparedServices !== 0) return comparedServices;
    }

    if (isParameterRevert(a) && isParameterRevert(b)) {
        const comparedServices = a.service.name.localeCompare(b.service.name);
        if (comparedServices !== 0) return comparedServices;

        const comparedNames = a.parameterName.localeCompare(b.parameterName);
        if (comparedNames !== 0) return comparedNames;
    }

    return a.commitReference.localeCompare(b.commitReference);
};
