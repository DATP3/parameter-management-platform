import './audit.css';

import AuditTable from '../../features/audit/AuditTable';
import { Typography } from 'rmwc';
import Search from '../../features/search_filter/Search';
import AuditFilterProvider from '../../features/search_filter/AuditFilterProvider';
import AuditSearch from '../../features/search_filter/AuditSearch';

const AuditPage = () => {
    return (
        <AuditFilterProvider>
            <Typography use='headline4'>Audit</Typography>
            <div className='mb-5'>
                <AuditSearch />
			</div>
            <AuditTable />
        </AuditFilterProvider>
    );
};

export default AuditPage;
