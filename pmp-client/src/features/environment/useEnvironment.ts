import { useParams } from 'react-router-dom';
import { Environment } from './types';

/**
 * Get the current environment from the URL.
 * @returns The current environment, and wether it is valid. Purposefully does NOT provide an easy way to change the environment, as that should only be done in very specific circumstances.
 */
const useEnvironment = (): Environment => {
    const { environment } = useParams<{ environment: string }>();
    return {
        /** We know that environments exist as we get them from the root router. The components are used on routes were we know the environments exist */
        environment: environment!
    };
};

export default useEnvironment;
