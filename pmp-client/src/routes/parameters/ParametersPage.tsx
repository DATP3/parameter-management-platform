import './style.css';

import ServiceList from './ServiceList';
import FilterData from '../../features/search/Filter';
import ParameterSearch from '../../features/search/ParameterSearch';

/**
 * Page for displaying and editing parameters.
 */
const ParametersPage = () => {
	return (
		<div className='paramPage'>
			<h1>Parameters</h1>
			<ParameterSearch />
			<ServiceList />
		</div>
	);
};

export default ParametersPage;
