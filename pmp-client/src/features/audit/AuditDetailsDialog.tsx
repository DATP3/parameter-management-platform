import { Dialog, DialogContent, DialogTitle } from 'rmwc';

import AuditDetails from './AuditDetails';
import RevertButton from './RevertButton';
import { useAuditDetailsEntry } from './useAuditDetailsEntry';

interface AuditDetailsDialogProps {
    open: boolean;
    onClose?: () => void;
}

/** Dialog displaying details of a single commit */
const AuditDetailsDialog = ({ open, onClose }: AuditDetailsDialogProps) => {
    const entry = useAuditDetailsEntry();
    return (
        <>
            <Dialog open={open} onClose={onClose} className='audit-dialog'>
                <DialogTitle>
                    {entry.message}
                    <div className='w-fit inline-block float-right pt-3'>
                        <RevertButton
                            revert={{
                                revertType: 'commit',
                                commitReference: entry.hash,
                                affectedServices: entry.affectedServices
                            }}
                        />
                    </div>
                </DialogTitle>
                <DialogContent>
                    <AuditDetails />
                </DialogContent>
            </Dialog>
        </>
    );
};

export default AuditDetailsDialog;
