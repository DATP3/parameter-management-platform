import './style.css';

import ServiceList from './ServiceList';
import ParameterSearch from '../../features/search_filter/ParameterSearch';
import { ParameterFilter } from '../../features/parameters/types';
import { useState } from 'react';
import ParameterFilterProvider from '../../features/search_filter/ParameterFilterProvider';

/**
 * Page for displaying and editing parameters.
 */
const ParametersPage = () => {
	return (
		<ParameterFilterProvider>
			<div className='paramPage'>
				<h1>Parameters</h1>
				<ParameterSearch/>
				<ServiceList />
			</div>
		</ParameterFilterProvider>
	);
};

export default ParametersPage;
