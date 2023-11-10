import { SelectedServiceContextValue } from './types';
import { selectedServiceContext } from './selectedServiceContext';
import { useContext } from 'react';

/** Gets the array of currently selected services */
const useSelectedServices = () => useContext(selectedServiceContext) as SelectedServiceContextValue;

export default useSelectedServices;
