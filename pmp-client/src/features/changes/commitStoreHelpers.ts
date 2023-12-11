import { Change, CommitRevert, ParameterChange, ParameterRevert, Revert, ServiceRevert } from './types';

/** ParameterRevert type predicate */
export const isParameterRevert = (revert: Revert): revert is ParameterRevert => {
    return revert.revertType === 'parameter';
};

/** CommitRevert type predicate */
export const isCommitRevert = (revert: Revert): revert is CommitRevert => {
    return revert.revertType === 'commit';
};

/** ServiceRevert type predicate */
export const isServiceRevert = (revert: Revert): revert is ServiceRevert => {
    return revert.revertType === 'service';
};

/** Revert type predicate */
export const isRevert = (change: Change): change is Revert => {
    return (change as Revert).revertType !== undefined;
};

/** ParameterChange type predicate */
export const isParameterChange = (change: Change): change is ParameterChange => {
    return !isRevert(change);
};

interface CompareChangesOptions {
    /** Ignore value when comparing parameter changes.
     * This means they are equal if they change the same parameter from the same service,
     * with the same original value
     */
    ignoreValue?: boolean;
}

/** Compares changes for sorting or equality checks*/
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

/** Compares parameter changes for sorting or equality checks */
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

/** Compares reverts for sorting or equality checks */
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
