import './style.css';

import ServiceList from './ServiceList';

/**
 * Page for displaying and editing parameters.
 */
const ParametersPage = () => {
    return (
        <div className='paramPage'>
            <h1>Parameters</h1>
            <ServiceList />
        </div>
    );
};

export default ParametersPage;
