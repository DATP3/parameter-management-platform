import './audit.css';

import AuditTable from '../../features/audit/AuditTable';
import { Typography } from 'rmwc';
import AuditFilterProvider from '../../features/search_filter/AuditFilterProvider';
import AuditSearch from '../../features/search_filter/AuditSearch';
import PageNavigator from '../../features/routing/PageNavigator';

const AuditPage = () => {
    return (
        <AuditFilterProvider>
            <div className='flex flex-col  h-full'>
                <div className='flex-0 pb-5'>
                    <div className='flex justify-between pb-3'>
                        <Typography use='headline4'>Audit</Typography>
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
