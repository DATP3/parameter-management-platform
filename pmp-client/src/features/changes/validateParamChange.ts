import { ParameterType } from '../parameters/types';
import { ParameterChange, ParameterValue } from './types';

const validateNonDecimalNumber = (value: ParameterValue, bits: number): boolean => {
    const isNonDecimal = /^-?\d+$/.test(value as string);
    if (!isNonDecimal) {
        return false;
    }

    const numberValue = parseInt(value as string);

    if (numberValue < -Math.pow(2, bits - 1) || numberValue > Math.pow(2, bits - 1) - 1) {
        return false;
    }

    return true;
};

enum Bits {
    INTEGER = 32,
    SHORT = 16,
    BYTE = 8,
    LONG = 64
}

const validateParamChange = (change: ParameterChange): boolean => {
    const { newValue, parameter } = change;
    const { type } = parameter;

    const decimalRegex = /^-?\d+(\.\d+)?$/;

    if (type === ParameterType.STRING) {
        return true;
    }

    if (type === ParameterType.INTEGER) {
        return validateNonDecimalNumber(newValue, Bits.INTEGER);
    }

    if (type === ParameterType.LONG) {
        return validateNonDecimalNumber(newValue, Bits.LONG);
    }

    if (type === ParameterType.SHORT) {
        return validateNonDecimalNumber(newValue, Bits.SHORT);
    }

    if (type === ParameterType.BYTE) {
        return validateNonDecimalNumber(newValue, Bits.BYTE);
    }

    if (type === ParameterType.FLOAT) {
        return decimalRegex.test(newValue as string);
    }

    if (type === ParameterType.DOUBLE) {
        return decimalRegex.test(newValue as string);
    }

    if (type === ParameterType.BOOLEAN) {
        return /^(true|false)$/.test(newValue as string);
    }

    if (type === ParameterType.CHARACTER) {
        return (newValue as string).length === 1;
    }

    if (type === ParameterType.BIGDECIMAL) {
        //Regex for bigdecimal
        return decimalRegex.test(newValue as string);
    }

    if (type === ParameterType.LOCALDATE) {
        //Regex for localdate
        return /^\d{4}-\d{2}-\d{2}$/.test(newValue as string);
    }

    if (type === ParameterType.LOCALDATETIME) {
        //Regex for localdatetime
        return /^\d{4}-\d{2}-\d{2}T\d{2}:\d{2}$/.test(newValue as string);
    }

    return false;
};

export default validateParamChange;
