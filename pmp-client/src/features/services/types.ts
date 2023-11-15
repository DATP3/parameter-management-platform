import { Environment } from '../environment/types';

/** Information regarding services */
export interface Service {
    name: string;
    address: string;
    environment: Environment;
}

/** Value kept in the selectedServiceContext */
export type SelectedServiceContextValue = [Service[], (services: Service[]) => void];
