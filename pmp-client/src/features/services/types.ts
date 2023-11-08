import { Environment } from '../environment/environment';

/** Information regarding services */
export interface Service {
    name: string;
    address: string;
    environment: Environment;
}

/** Value kept in the selectedServiceContext */
export type SelectedServiceContextValue = [Service[], React.Dispatch<React.SetStateAction<Service[]>>];
