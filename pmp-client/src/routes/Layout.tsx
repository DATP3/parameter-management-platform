import { Button, Grid, GridCell } from 'rmwc';

import AppBar from './AppBar';
import ChangeList from '../features/changes/sidebar/ChangeList';
import ListofServices from '../features/services/ListOfServices';
import { Environment } from '../features/environment/environment';
import { Outlet } from 'react-router-dom';
import useEnvironment from '../features/environment/useEnvironment';
import useSetEnvironment_UNSAFE from '../features/environment/useSetEnvironment_UNSAFE';

/**
 * Root layout encompassing the entire app
 *
 * Adds a top app bar for navigation and prohibits the user from navigating to any page before picking an environment
 */
const Layout = () => {
    const { isValid } = useEnvironment();
    const setEnvironment = useSetEnvironment_UNSAFE();

    return (
        <>
            <AppBar />
            {!isValid && (
                <>
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
                </>
            )}
            {isValid && (
                <Grid>
                    <GridCell span={3}>
                        <ListofServices />
                    </GridCell>
                    <GridCell span={6}>
                        <Outlet />
                    </GridCell>
                    <GridCell span={3}>
                        <ChangeList />
                    </GridCell>
                </Grid>
            )}
        </>
    );
};

export default Layout;
