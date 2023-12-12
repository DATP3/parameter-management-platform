import { DataTableCell, DataTableRow, Typography } from 'rmwc';

import AuditDetailsDialog from './AuditDetailsDialog';
import { formatDate } from '../../utils/date';
import { useAuditDetailsEntry } from './useAuditDetailsEntry';
import { useState } from 'react';

interface AuditTableRowProps {}

/** Row item for the audit list table. Displays information about commit and provides the details dialog */
const AuditTableRow = (_: AuditTableRowProps) => {
    const [open, setOpen] = useState(false);
    const entry = useAuditDetailsEntry();
    return (
        <>
            <AuditDetailsDialog open={open} onClose={() => setOpen(false)} />
            <DataTableRow className='tableRow cursor-pointer' onClick={() => setOpen(true)}>
                <DataTableCell>{formatDate(entry.pushDate)}</DataTableCell>
                <DataTableCell>{entry.user}</DataTableCell>
                <DataTableCell>
                    <Typography use='overline'>{entry.hash}</Typography>
                </DataTableCell>
                <DataTableCell>{entry.message}</DataTableCell>
            </DataTableRow>
        </>
    );
};

export default AuditTableRow;
