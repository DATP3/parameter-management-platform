import { Button, CircularProgress, Typography } from 'rmwc';
import useSetEnvironment_UNSAFE from '../features/environment/useSetEnvironment_UNSAFE';
import useEnvironmentQuery from '../features/environment/useEnvironmentQuery';

const InvalidEnvironmentScreen = () => {
    const setEnvironment = useSetEnvironment_UNSAFE();

    const { data: environments, isPending, error } = useEnvironmentQuery();

    if (isPending)
        return (
            <>
                <Typography use='headline6'>Waiting on environments</Typography> <CircularProgress />
            </>
        );

    if (error) return <Typography use='headline6'>Error getting environments</Typography>;

    return (
        // TODO: Change button style per environment
        <>
            <div className='prose max-w-full'>
                <h3>To proceed, please pick an environment</h3>

                {environments.map((e) => (
                    <Button key={e.environment} raised onClick={() => setEnvironment(e)}>
                        {e.environment}
                    </Button>
                ))}
            </div>
        </>
    );
};

export default InvalidEnvironmentScreen;
