import './audit.css';

import AuditTable from '../../features/audit/AuditTable';
import { Typography } from 'rmwc';

const AuditPage = () => {
    return (
        <div>
            <Typography use="headline4">Audit</Typography>
            <AuditTable />
        </div>
    );
};

export default AuditPage;
