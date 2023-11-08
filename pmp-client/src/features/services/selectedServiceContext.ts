import { SelectedServiceContextValue } from './types';
import { createContext } from 'react';

/**
 * Context for the selected service list.
 */
export const selectedServiceContext = createContext<SelectedServiceContextValue | null>(null);
