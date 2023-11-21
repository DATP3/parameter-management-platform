import { DataTable, DataTableBody, DataTableContent, DataTableHead, DataTableHeadCell, DataTableRow } from 'rmwc';

import AuditTableRow from './AuditTableRow';
import useAuditLogEntries from './useAuditLogEntries';

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
                        <DataTableHeadCell style={{backgroundColor: "transparent"}}>Date</DataTableHeadCell>
                        <DataTableHeadCell style={{backgroundColor: "transparent"}}>User</DataTableHeadCell>
                        <DataTableHeadCell style={{backgroundColor: "transparent"}}>Hash</DataTableHeadCell>
                        <DataTableHeadCell style={{backgroundColor: "transparent"}}>Message</DataTableHeadCell>
                        <DataTableHeadCell style={{backgroundColor: "transparent"}}>Options</DataTableHeadCell>
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
