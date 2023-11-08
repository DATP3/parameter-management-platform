import { selectedServiceContext } from './selectedServiceContext';
import { useContext } from 'react';

const useSelectedServices = () => useContext(selectedServiceContext);

export default useSelectedServices;
