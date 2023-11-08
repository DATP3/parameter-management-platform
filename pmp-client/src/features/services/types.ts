import { Environment } from '../environment/environment';

export interface Service {
    name: string;
    address: string;
    environment: Environment;
}

export type SelectedServiceContextValue = [Service[], React.Dispatch<React.SetStateAction<Service[]>>];
