import { DataTableCell, DataTableRow } from 'rmwc';

import AuditDetailsDialog from './AuditDetailsDialog';
import { AuditLogEntry } from './useAuditLogEntries';
import { useState } from 'react';

// interface AuditTableRowRevertProps {
//     entry: AuditLogEntry;
// }

// const AuditTableRowRevert = ({ entry }: AuditTableRowRevertProps) => {
//     // const addRevert = useCommitStore(s => s.addRevert);

//     const handleClick = (e: React.MouseEvent<HTMLButtonElement, MouseEvent>) => {
//         e.stopPropagation();
//     };

//     return (
//         <>
//             <Tooltip content='Revert commit'>
//                 <IconButton icon={{ icon: 'undo' }} onClick={handleClick} />
//             </Tooltip>
//         </>
//     );
// };

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
                {/* <DataTableCell>
                    <AuditTableRowRevert entry={entry} />
                </DataTableCell> */}
            </DataTableRow>
        </>
    );
};

export default AuditTableRow;
