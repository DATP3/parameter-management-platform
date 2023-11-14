import './style.css';

import ServiceList from './ServiceList';
import Search from '../../features/search/Search';

/**
 * Page for displaying and editing parameters.
 */
const ParametersPage = () => {
    return (
        <div className='paramPage'>
            <h1>Parameters</h1>
			<Search />
            <ServiceList />
        </div>
    );
};

export default ParametersPage;
