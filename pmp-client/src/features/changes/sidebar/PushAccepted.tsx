import { Dialog, DialogActions, DialogButton, DialogTitle } from 'rmwc';
import usePushCommit from './usePushCommit';
import useCommitStore from '../useCommitStore';

interface PushAcceptedProps {
    open: boolean;
    onClose?: () => void;
}

const PushAccepted = ({ open, onClose }: PushAcceptedProps) => {
    // TODO: Missing implementation: usePushCommit currently only returns true.
    // It should check whether the push can get done or not.
    // Also actual backend implementation is missing to change params on services.
    // Finally, the parameters are currently just reverted back to their original values.
    const accepted = usePushCommit();
    const clearChanges = useCommitStore((s) => s.clear);

    if (accepted) {
        console.log('In accepted component');
        return (
            <>
                <Dialog open={open} onClose={onClose} onOpen={clearChanges}>
                    <DialogTitle>Changes pushed succesfully</DialogTitle>
                    <DialogActions>
                        <DialogButton danger outlined action='accept'>
                            OK
                        </DialogButton>
                    </DialogActions>
                </Dialog>
            </>
        );
    } else {
        console.log('In discarded component');
        return (
            <>
                <Dialog open={open} onClose={onClose}>
                    <DialogTitle>Applying changes was unsuccesful</DialogTitle>
                    <DialogActions>
                        <DialogButton danger outlined action='accept'>
                            OK
                        </DialogButton>
                    </DialogActions>
                </Dialog>
            </>
        );
    }
};

export default PushAccepted;
