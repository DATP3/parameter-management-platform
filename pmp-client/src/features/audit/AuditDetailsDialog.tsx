import { Dialog, DialogContent, DialogTitle } from 'rmwc';

import AuditDetails from './AuditDetails';
import { AuditLogEntry } from './useAuditLogEntries';

interface AuditDetailsDialogProps {
    entry: AuditLogEntry;
    open: boolean;
    onClose?: () => void;
}

const AuditDetailsDialog = ({ entry, open, onClose }: AuditDetailsDialogProps) => {
    return (
        <>
            <Dialog open={open} onClose={onClose} className='audit-dialog'>
                <DialogTitle>{entry.message}</DialogTitle>
                <DialogContent>
                    <AuditDetails entry={entry} />
                </DialogContent>
            </Dialog>
        </>
    );
};

export default AuditDetailsDialog;
