import {
    DataTable,
    DataTableBody,
    DataTableContent,
    DataTableHead,
    DataTableHeadCell,
    DataTableRow,
    Typography
} from 'rmwc';

import AuditTableRow from './AuditTableRow';
import useAuditLogEntries from './useAuditLogEntries';
import ThemeMarkerWrapper from '../components/ThemeMarkerWrapper';
import validateAuditFilterMatch from '../search_filter/validateAuditFilterMatch';
import { useAuditFilter } from '../search_filter/useAuditFilter';

const AuditList = () => {
    const { data: entries, isPending, isError, errors } = useAuditLogEntries('');
    const [filter] = useAuditFilter();

    if (isPending) return <div>Loading...</div>;

    if (isError) {
        console.error(errors);
        return <div>Error</div>;
    }

    const filteredEntries = entries?.filter((e) => validateAuditFilterMatch(filter, e));

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
                                <DataTableHeadCell style={{ backgroundColor: 'transparent' }}>
                                    Options
                                </DataTableHeadCell>
                            </DataTableRow>
                        </DataTableHead>
                        <DataTableBody>
                            {filteredEntries!.map((e) => (
                                <AuditTableRow key={e.hash} entry={e} />
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
