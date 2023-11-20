import { DataTableCell, DataTableRow, IconButton, Tooltip } from 'rmwc';

import AuditDetailsDialog from './AuditDetailsDialog';
import { AuditLogEntry } from './useAuditLogEntries';
import { CommitRevert } from '../changes/types';
import useCommitStore from '../changes/useCommitStore';
import { useState } from 'react';

interface AuditTableRowRevertProps {
    entry: AuditLogEntry;
}

const AuditTableRowRevert = ({ entry }: AuditTableRowRevertProps) => {
    const change: CommitRevert = {
        revertType: 'commit',
        commitReference: entry.hash
    };
    const addChange = useCommitStore((s) => s.addChange);
    const removeChange = useCommitStore((s) => s.removeChange);
    const foundChange = useCommitStore((s) => s.findChange(change));

    const handleClick = (e: React.MouseEvent<HTMLButtonElement, MouseEvent>) => {
        e.stopPropagation();
        if (foundChange) removeChange(foundChange);
        else addChange(change);
    };

    if (foundChange) {
        return (
            <>
                <Tooltip content='Remove revert'>
                    <IconButton icon={{ icon: 'restart_alt' }} onClick={handleClick} />
                </Tooltip>
            </>
        );
    }

    return (
        <>
            <Tooltip content='Revert commit'>
                <IconButton icon={{ icon: 'undo' }} onClick={handleClick} />
            </Tooltip>
        </>
    );
};

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
                <DataTableCell>
                    <AuditTableRowRevert entry={entry} />
                </DataTableCell>
            </DataTableRow>
        </>
    );
};

export default AuditTableRow;
