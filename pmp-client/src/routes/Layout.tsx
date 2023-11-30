import AppBar from './AppBar';
import InvalidEnvironmentScreen from './InvalidEnvironmentScreen';
import ListofServices from '../features/services/ListOfServices';
import { Outlet } from 'react-router-dom';
import PendingChanges from '../features/changes/sidebar/PendingChanges';
import { Portal } from 'rmwc';
import SideDrawer from '../features/components/SideDrawer';
import useIsEnvironmentValid from '../features/environment/useIsEnvironmentValid';

/**
 * Root layout encompassing the entire app
 *
 * Adds a top app bar for navigation and prohibits the user from navigating to any page before picking an environment
 */
const Layout = () => {
    const isValid = useIsEnvironmentValid();

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
                            <div data-testid='service-segment' className='flex-none h-full overflow-auto max-w-xs'>
                                <SideDrawer>
                                    <ListofServices />
                                </SideDrawer>
                            </div>
                            {/** Middle */}
                            <div data-testid='main-segment' className='flex-1 p-4 h-full overflow-auto'>
                                <Outlet />
                            </div>
                            {/** Right */}
                            <div data-testid='changes-segment' className='flex-none h-full overflow-auto max-w-lg'>
                                <SideDrawer rtl>
                                    <PendingChanges />
                                </SideDrawer>
                            </div>
                        </>
                    )}
                </div>
            </div>
            <Portal />
        </>
    );
};

export default Layout;
