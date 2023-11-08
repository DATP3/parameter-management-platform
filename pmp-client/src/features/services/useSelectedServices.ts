import { selectedServiceContext } from './selectedServiceContext';
import { useContext } from 'react';

/** Gets the array of currently selected services */
const useSelectedServices = () => useContext(selectedServiceContext);

export default useSelectedServices;
