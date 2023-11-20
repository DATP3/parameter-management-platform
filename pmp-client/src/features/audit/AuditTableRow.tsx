import { DataTableCell, DataTableRow } from 'rmwc';

import AuditDetailsDialog from './AuditDetailsDialog';
import { AuditLogEntry } from './useAuditLogEntries';
import { useState } from 'react';

interface AuditTableRowProps {
    entry: AuditLogEntry;
}

const AuditTableRow = ({ entry }: AuditTableRowProps) => {
    const [open, setOpen] = useState(false);
    return (
        <>
            <AuditDetailsDialog entry={entry} open={open} onClose={() => setOpen(false)} />
            <DataTableRow onClick={() => setOpen(true)}>
                <DataTableCell>{entry.pushDate.toLocaleString()}</DataTableCell>
                <DataTableCell>{entry.email}</DataTableCell>
                <DataTableCell>{entry.hash}</DataTableCell>
                <DataTableCell>{entry.message}</DataTableCell>
            </DataTableRow>
        </>
    );
};

export default AuditTableRow;
