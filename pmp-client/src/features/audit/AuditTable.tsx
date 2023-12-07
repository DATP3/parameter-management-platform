import {
    DataTable,
    DataTableBody,
    DataTableContent,
    DataTableHead,
    DataTableHeadCell,
    DataTableRow,
    Typography
} from 'rmwc';

import { AuditDetailsEntryProvider } from './AuditDetailsEntryProvider';
import AuditTableRow from './AuditTableRow';
import ThemeMarkerWrapper from '../components/ThemeMarkerWrapper';
import { useAuditFilter } from '../search_filter/useAuditFilter';
import useAuditLogEntries from './useAuditLogEntries';
import validateAuditFilterMatch from '../search_filter/validateAuditFilterMatch';

const AuditList = () => {
    const { data: entries, isPending, isError, errors } = useAuditLogEntries('');
    const [filter] = useAuditFilter();

    if (isPending) return <div>Loading...</div>;

    if (isError) {
        console.error(errors);
        return <div>Error</div>;
    }

    let filteredEntries = entries?.filter((e) => validateAuditFilterMatch(filter, e));
    filteredEntries = filteredEntries?.sort((a, b) => b.pushDate.getTime() - a.pushDate.getTime());

    return (
        <>
            <ThemeMarkerWrapper>
                <DataTable className='dataTable'>
                    <DataTableContent className='tableHead'>
                        <DataTableHead>
                            <DataTableRow>
                                <DataTableHeadCell style={{ backgroundColor: 'transparent' }}>Date</DataTableHeadCell>
                                <DataTableHeadCell style={{ backgroundColor: 'transparent' }}>User</DataTableHeadCell>
                                <DataTableHeadCell style={{ backgroundColor: 'transparent' }}>Hash</DataTableHeadCell>
                                <DataTableHeadCell style={{ backgroundColor: 'transparent' }}>
                                    Message
                                </DataTableHeadCell>
                            </DataTableRow>
                        </DataTableHead>
                        <DataTableBody>
                            {filteredEntries!.map((e) => (
                                <AuditDetailsEntryProvider key={e.hash} entry={e}>
                                    <AuditTableRow key={e.hash} />
                                </AuditDetailsEntryProvider>
                            ))}
                        </DataTableBody>
                    </DataTableContent>
                </DataTable>
            </ThemeMarkerWrapper>

            {filteredEntries!.length === 0 && (
                <div className='mt-2'>
                    <Typography use='subtitle1'>Choose a service from the list in the left drawer</Typography>
                </div>
            )}
        </>
    );
};

export default AuditList;
