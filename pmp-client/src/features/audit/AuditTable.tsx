import { DataTable, DataTableBody, DataTableContent, DataTableHead, DataTableHeadCell, DataTableRow } from 'rmwc';

import AuditTableRow from './AuditTableRow';
import useAuditLogEntries from './useAuditLogEntries';
import '../../routes/parameters/style.css';

const AuditList = () => {
    const { data: entries, isPending, isError, errors } = useAuditLogEntries('');

    if (isPending) return <div>Loading...</div>;

    if (isError) {
        console.error(errors);
        return <div>Error</div>;
    }

    return (
        <DataTable className='dataTable'>
            <DataTableContent className='tableHead'>
                <DataTableHead>
                    <DataTableRow>
                        <DataTableHeadCell>Date</DataTableHeadCell>
                        <DataTableHeadCell>User</DataTableHeadCell>
                        <DataTableHeadCell>Hash</DataTableHeadCell>
                        <DataTableHeadCell>Message</DataTableHeadCell>
                        <DataTableHeadCell>Options</DataTableHeadCell>
                    </DataTableRow>
                </DataTableHead>
                <DataTableBody>
                    {entries!.map((e) => (
                        <AuditTableRow key={e.hash} entry={e} />
                    ))}
                </DataTableBody>
            </DataTableContent>
        </DataTable>
    );
};

export default AuditList;
