import useEnvironment from './useEnvironment';
import useEnvironmentQuery from './useEnvironmentQuery';

const useIsEnvironmentValid = () => {
    const { data: environments } = useEnvironmentQuery();
    const { environment } = useEnvironment();

    if (environments && environments.some((env) => env.environment === environment)) {
        return true;
    }

    return false;
};

export default useIsEnvironmentValid;
