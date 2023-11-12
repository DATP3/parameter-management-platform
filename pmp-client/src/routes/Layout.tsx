import AppBar from './AppBar';
import ChangeList from '../features/changes/sidebar/ChangeList';
import InvalidEnvironmentScreen from './InvalidEnvironmentScreen';
import ListofServices from '../features/services/ListOfServices';
import { Outlet } from 'react-router-dom';
import PendingChanges from '../features/changes/sidebar/PendingChanges';
import SideDrawer from '../features/components/SideDrawer';
import useEnvironment from '../features/environment/useEnvironment';
import useSetEnvironment_UNSAFE from '../features/environment/useSetEnvironment_UNSAFE';

/**
 * Root layout encompassing the entire app
 *
 * Adds a top app bar for navigation and prohibits the user from navigating to any page before picking an environment
 */
const Layout = () => {
    const { isValid } = useEnvironment();

    return (
        <>
            <div className='flex flex-col h-screen'>
                {/** Top */}
                <div className='flex-none'>
                    <AppBar />
                </div>
                <div className='flex flex-1 overflow-hidden'>
                    {!isValid && <InvalidEnvironmentScreen />}
                    {isValid && (
                        <>
                            {/** Left */}
                            <div className='flex-none h-full overflow-auto max-w-xs'>
                                <SideDrawer>
                                    <ListofServices />
                                </SideDrawer>
                            </div>
                            {/** Middle */}
                            <div className='flex-1 p-4 h-full overflow-auto'>
                                <Outlet />
                            </div>
                            {/** Right */}
                            <div className='flex-none h-full overflow-auto max-w-xs'>
                                <SideDrawer rtl>
                                    <ChangeList />
                                </SideDrawer>
                            </div>
                        </>
                    )}
                </div>
            </div>
        </>
    );
};

export default Layout;
