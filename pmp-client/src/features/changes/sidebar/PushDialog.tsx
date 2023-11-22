import { useState } from 'react';
import { Dialog, DialogActions, DialogButton, DialogContent, DialogTitle, TextField, Typography } from 'rmwc';
import PushAccepted from './PushAccepted';

interface PushDialogProps {
    open: boolean;
    onClose?: () => void;
    showWarning: boolean;
    environment: string;
}

const PushDialog = ({ environment, open, onClose, showWarning }: PushDialogProps) => {
    const [input, setInput] = useState('');
    const isInputValid = input === environment;
    const [openNextDialog, setOpenNextDialog] = useState(false);

    const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setInput(e.target.value);
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
                                    label={'Enter  "' + environment + '" to confirm'}
                                    value={input}
                                    onChange={handleInputChange}
                                />
                            </div>
                        </>
                    )}
                </DialogContent>
                <DialogActions>
                    <DialogButton raised action='close' isDefaultAction>
                        Cancel
                    </DialogButton>
                    <DialogButton
                        danger
                        outlined
                        action='accept'
                        disabled={showWarning && !isInputValid}
                        onClick={() => setOpenNextDialog(true)}
                    >
                        Push
                    </DialogButton>
                </DialogActions>
            </Dialog>
            <PushAccepted open={openNextDialog} onClose={() => setOpenNextDialog(false)} />
        </>
    );
};

export default PushDialog;
