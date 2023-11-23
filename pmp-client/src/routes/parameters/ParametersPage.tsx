import ServiceList from './ServiceList';
import ParameterSearch from '../../features/search_filter/ParameterSearch';
import ParameterFilterProvider from '../../features/search_filter/ParameterFilterProvider';
import ParameterFilter from '../../features/search_filter/ParameterFilter';
import { Typography } from 'rmwc';

/**
 * Page for displaying and editing parameters.
 */
const ParametersPage = () => {
    return (
        <ParameterFilterProvider>
            <div className='paramPage mb-20'>
                <Typography use='headline4'>Parameters</Typography>
                <ParameterSearch />
                <ParameterFilter />
                <ServiceList />
            </div>
        </ParameterFilterProvider>
    );
};

export default ParametersPage;
