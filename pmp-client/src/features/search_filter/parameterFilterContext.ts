import { createContext } from 'react';
import { ParameterFilterContextValue } from './types';

export const parameterFilterContext = createContext<ParameterFilterContextValue | null>(null);
