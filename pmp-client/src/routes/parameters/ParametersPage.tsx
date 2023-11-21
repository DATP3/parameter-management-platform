import ServiceList from './ServiceList';
import ParameterSearch from '../../features/search_filter/ParameterSearch';
import ParameterFilterProvider from '../../features/search_filter/ParameterFilterProvider';
import ParameterFilter from '../../features/search_filter/ParameterFilter';

/**
 * Page for displaying and editing parameters.
 */
const ParametersPage = () => {
	return (
		<ParameterFilterProvider>
			<div className='paramPage'>
				<h1>Parameters</h1>
				<ParameterSearch/>
				<ParameterFilter/>
				<ServiceList />
			</div>
		</ParameterFilterProvider>
	);
};

export default ParametersPage;
