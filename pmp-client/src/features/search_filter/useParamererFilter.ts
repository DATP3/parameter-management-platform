import { useContext } from 'react';
import { parameterFilterContext } from './parameterFilterContext';
import { ParameterFilterContextValue } from './types';

export const useParameterFilter = () => useContext(parameterFilterContext) as ParameterFilterContextValue;
