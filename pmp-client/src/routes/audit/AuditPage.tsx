import './audit.css';

import AuditTable from '../../features/audit/AuditTable';
import { Typography } from 'rmwc';
import AuditFilterProvider from '../../features/search_filter/AuditFilterProvider';
import AuditSearch from '../../features/search_filter/AuditSearch';

const AuditPage = () => {
    return (
        <div className='mb-20'>
            <AuditFilterProvider>
                <Typography use='headline4'>Audit</Typography>
                <div className='mb-5'>
                    <AuditSearch />
                </div>
                <AuditTable />
            </AuditFilterProvider>
        </div>
    );
};

export default AuditPage;
