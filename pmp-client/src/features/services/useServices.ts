import { Service } from './types';
import { Environment } from '../environment/environment';
import useEnvironment from '../environment/useEnvironment';

const useServices = () => {
    const list: Service[] = [
        { name: 'service1', address: '1.1.1.1', environment: Environment.TEST },
        { name: 'service2', address: '2.2.2.2', environment: Environment.PROD },
        { name: 'service3', address: '3.3.3.3', environment: Environment.PREPROD },
        { name: 'service1', address: '1.1.1.1', environment: Environment.PROD },
        { name: 'service2', address: '2.2.2.2', environment: Environment.DEV },
        { name: 'service3', address: '3.3.3.3', environment: Environment.TEST },
        { name: 'service4', address: '4.4.4.4', environment: Environment.TEST },
        { name: 'service5', address: '5.5.5.5', environment: Environment.PROD },
        { name: 'service6', address: '6.6.6.6', environment: Environment.TEST },
        { name: 'service7', address: '7.7.7.7', environment: Environment.PROD },
        { name: 'service8', address: '8.8.8.8', environment: Environment.PREPROD },
        { name: 'service9', address: '9.9.9.9', environment: Environment.DEV }
    ];

    const { environment } = useEnvironment();
    return list.filter((s) => s.environment === environment);
};

export default useServices;
