import './audit.css';

import AuditFilterProvider from '../../features/search_filter/AuditFilterProvider';
import AuditSearch from '../../features/search_filter/AuditSearch';
import AuditTable from '../../features/audit/AuditTable';
import PageNavigator from '../../features/routing/PageNavigator';
import { Typography } from 'rmwc';

/** Audot / Commit history page */
const AuditPage = () => {
    return (
        <AuditFilterProvider>
            <div className='flex flex-col  h-full'>
                <div className='flex-0 pb-5 pt-5'>
                    <div className='flex justify-between pb-3'>
                        <Typography use='headline4'>Commit History</Typography>
                        <PageNavigator />
                    </div>
                    <AuditSearch />
                </div>
                <div className='flex-1 overflow-auto pb-20'>
                    <AuditTable />
                </div>
            </div>
        </AuditFilterProvider>
    );
};

export default AuditPage;
