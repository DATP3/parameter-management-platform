import ServiceList from './ServiceList';
import ParameterSearch from '../../features/search_filter/ParameterSearch';
import ParameterFilterProvider from '../../features/search_filter/ParameterFilterProvider';
import ParameterFilter from '../../features/search_filter/ParameterFilter';
import { Typography } from 'rmwc';
import PageNavigator from '../../features/routing/PageNavigator';

/**
 * Page for displaying and editing parameters.
 */
const ParametersPage = () => {
    return (
        <ParameterFilterProvider>
            <div className='flex flex-col h-full'>
                <div className='flex-0 pb-5'>
                    <div className='flex justify-between pb-3'>
                        <Typography use='headline4'>Parameters</Typography>
                        <PageNavigator />
                    </div>
                    <ParameterSearch />
                    <ParameterFilter />
                </div>
                <div className='flex-1 overflow-auto pb-20'>
                    <ServiceList />
                </div>
            </div>
        </ParameterFilterProvider>
    );
};

export default ParametersPage;
