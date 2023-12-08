import { Button, CircularProgress, Grid, GridCell, Typography } from 'rmwc';

import useEnvironmentQuery from '../features/environment/useEnvironmentQuery';
import useSetEnvironment_UNSAFE from '../features/environment/useSetEnvironment_UNSAFE';

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
            <div className='prose max-w-full p-40 flex justify-center w-full'>
                <div>
                    <Typography use='headline4'>Parameter Management Platform</Typography>
                    <br></br>
                    <Typography use='headline5'>To proceed, please pick an environment</Typography>
                    {/* <h3>To proceed, please pick an environment</h3> */}
                    <Grid>
                        {environments.map((e) => (
                            <GridCell key={e.environment}>
                                <Button
                                    className='w-full h-20'
                                    key={e.environment}
                                    raised
                                    onClick={() => setEnvironment(e)}
                                >
                                    {e.environment}
                                </Button>
                            </GridCell>
                        ))}
                    </Grid>
                </div>
            </div>
        </>
    );
};

export default InvalidEnvironmentScreen;
