import { SelectedServiceContextValue } from './types';
import { createContext } from 'react';

/**
 * Context for the commit store.
 */
export const selectedServiceContext = createContext<SelectedServiceContextValue | null>(null);
