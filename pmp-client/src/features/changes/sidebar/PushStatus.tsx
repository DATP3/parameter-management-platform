import { CircularProgress, Dialog, DialogActions, DialogButton, DialogContent, DialogTitle } from 'rmwc';

import { CommitBody } from '../types';
import useCommitStore from '../useCommitStore';
import usePushCommit from './usePushCommit';

interface PushStatusProps {
    open: boolean;
    onClose?: () => void;
    commit: CommitBody;
}

/** Loading state for pushing commits. Automatically pushed current commit on mount */
const PushStatus = ({ open, onClose, commit }: PushStatusProps) => {
    const { requestState } = usePushCommit(commit);
    const clearChanges = useCommitStore((s) => s.clear);

    const handleClose = () => {
        if (requestState === 'success') {
            clearChanges();
        }
        onClose?.();
    };

    if (requestState === 'loading') {
        return (
            <Dialog open={open}>
                <DialogTitle>Pushing commit</DialogTitle>
                <DialogContent>
                    <CircularProgress />
                </DialogContent>
            </Dialog>
        );
    }

    if (requestState === 'error') {
        return (
            <>
                <Dialog open={open} onClose={handleClose} onOpen={clearChanges}>
                    <DialogTitle>Failed to push changes</DialogTitle>
                    <DialogContent>
                        Changes could not be pushed to the services, try again later. The current change list has been
                        preserved.
                    </DialogContent>
                    <DialogActions>
                        <DialogButton danger outlined action='accept'>
                            OK
                        </DialogButton>
                    </DialogActions>
                </Dialog>
            </>
        );
    }

    if (requestState === 'partial') {
        return (
            <>
                <Dialog open={open} onClose={handleClose} onOpen={clearChanges}>
                    <DialogTitle>Pushed changes, but some services failed</DialogTitle>
                    <DialogContent>
                        Changes were successfully pushed to some services. Others however failed. See details in commit
                        history.
                    </DialogContent>
                    <DialogActions>
                        <DialogButton danger outlined action='accept'>
                            OK
                        </DialogButton>
                    </DialogActions>
                </Dialog>
            </>
        );
    }

    if (requestState === 'success') {
        return (
            <>
                <Dialog open={open} onClose={handleClose} onOpen={clearChanges}>
                    <DialogTitle>Changes pushed succesfully</DialogTitle>
                    <DialogContent>
                        Changes were successfully pushed to all services. Change list will be cleared shortly
                    </DialogContent>
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

export default PushStatus;
