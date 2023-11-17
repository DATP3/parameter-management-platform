import { ParameterRevert, Revert } from './types';

const isParameterRevert = (revert: Revert): revert is ParameterRevert => {
    return revert.revertType === 'parameter';
};

export const areRevertsEqual = (a: Revert, b: Revert): boolean => {
    if (isParameterRevert(a) && isParameterRevert(b)) {
        return a.parameterName === b.parameterName;
    }

    return a.commitReference === b.commitReference && a.revertType === b.revertType;
};
