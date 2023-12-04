import { Dialog, DialogActions, DialogButton, DialogContent, DialogTitle, TextField, Typography } from 'rmwc';

import { CommitBody } from '../types';
import PushStatus from './PushStatus';
import useCommitStore from '../useCommitStore';
import { useMsal } from '@azure/msal-react';
import { useState } from 'react';

interface PushDialogProps {
    open: boolean;
    onClose?: () => void;
    showWarning: boolean;
    environment: string;
}

const PushDialog = ({ environment, open, onClose, showWarning }: PushDialogProps) => {
    const [input, setInput] = useState('');
    const [commitMessage, setCommitMessage] = useState('');
    const isInputValid = input === environment;
    const [openNextDialog, setOpenNextDialog] = useState(false);

    const changes = useCommitStore((s) => s.changes);
    const { accounts } = useMsal();

    const commit: CommitBody = {
        changes,
        user: accounts[0].name || 'Invalid User',
        message: commitMessage,
        pushDate: new Date()
    };

    const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setInput(e.target.value);
    };

    const handleCommitMessageChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setCommitMessage(e.target.value);
    };

    return (
        <>
            <Dialog open={open} onClose={onClose} onClosed={() => setInput('')}>
                <DialogTitle>Push Pending Changes</DialogTitle>
                <DialogContent>
                    <Typography use='headline6'>Are you sure you want to push the current pending changes?</Typography>
                    {showWarning && (
                        <>
                            <div>
                                <TextField
                                    className='w-full'
                                    label={'Enter  "' + environment + '" to confirm'}
                                    value={input}
                                    full-width
                                    onChange={handleInputChange}
                                />
                            </div>
                        </>
                    )}
                    <div>
                        <TextField
                            className='mt-5 w-full'
                            full-width
                            label={'Commit message'}
                            value={commitMessage}
                            onChange={handleCommitMessageChange}
                        />
                    </div>
                </DialogContent>
                <DialogActions>
                    <DialogButton raised action='close' isDefaultAction>
                        Cancel
                    </DialogButton>
                    <DialogButton
                        danger
                        outlined
                        action='accept'
                        disabled={(showWarning && !isInputValid) || commitMessage === ''}
                        onClick={() => setOpenNextDialog(true)}
                    >
                        Push
                    </DialogButton>
                </DialogActions>
            </Dialog>
            {openNextDialog && (
                <PushStatus open={openNextDialog} onClose={() => setOpenNextDialog(false)} commit={commit} />
            )}
        </>
    );
};

export default PushDialog;
