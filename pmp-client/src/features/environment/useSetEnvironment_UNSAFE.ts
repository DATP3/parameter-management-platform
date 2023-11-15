import { useLocation, useNavigate } from 'react-router-dom';

import { Environment } from './types';
import useEnvironment from './useEnvironment';

/**
 * UNSAFE OPERATION
 *
 * Changes the current working environment.
 * This hook is marked UNSAFE because changing the environment should only happen rarely, and always in the same way.
 *
 * do NOT use this hook to change the environment in a component. Leave that to the global environment picker.
 */
const useSetEnvironment_UNSAFE = () => {
    const { environment } = useEnvironment();
    const navigate = useNavigate();
    const location = useLocation();
    const setEnvironment = (newEnvironment: Environment) => {
        navigate(location.pathname.replace(environment, newEnvironment.environment) + location.search + location.hash);
    };

    return setEnvironment;
};

export default useSetEnvironment_UNSAFE;
