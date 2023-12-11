import useEnvironment from './useEnvironment';
import useEnvironmentQuery from './useEnvironmentQuery';

/**
 * Checks if the current environment as set in the url is recognized by the Tracker.
 * @returns true if the current environment is valid, false otherwise.
 */
const useIsEnvironmentValid = () => {
    const { data: environments } = useEnvironmentQuery();
    const { environment } = useEnvironment();

    if (environments && environments.some((env) => env.environment === environment)) {
        return true;
    }

    return false;
};

export default useIsEnvironmentValid;
