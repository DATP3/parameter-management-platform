import { Button } from 'rmwc';
import { Environment } from '../features/environment/environment';
import useSetEnvironment_UNSAFE from '../features/environment/useSetEnvironment_UNSAFE';

const InvalidEnvironmentScreen = () => {
    const setEnvironment = useSetEnvironment_UNSAFE();

    return (
        <div className='prose max-w-full'>
            <h3>To proceed, please pick an environment</h3>

            <Button raised onClick={() => setEnvironment(Environment.TEST)}>
                Test
            </Button>
            <Button outlined onClick={() => setEnvironment(Environment.DEV)}>
                Dev
            </Button>
            <Button outlined danger onClick={() => setEnvironment(Environment.PREPROD)}>
                Preprod
            </Button>
            <Button raised danger onClick={() => setEnvironment(Environment.PROD)}>
                Prod
            </Button>
        </div>
    );
};

export default InvalidEnvironmentScreen;
