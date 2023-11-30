import { Button, IconButton, Tooltip } from 'rmwc';

import { Revert } from '../changes/types';
import useCommitStore from '../changes/useCommitStore';

interface RevertButtonProps {
    revert: Revert;
}

const RevertButton = ({ revert }: RevertButtonProps): JSX.Element => {
    const addChange = useCommitStore((s) => s.addChange);
    const removeChange = useCommitStore((s) => s.removeChange);
    const foundChange = useCommitStore((s) => s.findChange(revert));

    const handleClick = (e: React.MouseEvent<HTMLButtonElement, MouseEvent>) => {
        e.stopPropagation();
        if (foundChange) removeChange(foundChange);
        else addChange(revert);
    };

    const icon = foundChange ? 'restart_alt' : 'undo';

    // This is not entirely DRY, but readable
    if (revert.revertType === 'commit') {
        return (
            <Tooltip content={foundChange ? 'Remove revert' : 'Add revert of changes in this commit'}>
                <Button label='Revert commit' icon={icon} onClick={handleClick} raised />
            </Tooltip>
        );
    }

    if (revert.revertType === 'service') {
        return (
            <Tooltip content={foundChange ? 'Remove revert' : 'Add revert of changes in this service'}>
                <Button label='Revert service' icon={icon} onClick={handleClick} outlined />
            </Tooltip>
        );
    }

    if (revert.revertType === 'parameter') {
        return (
            <Tooltip content={foundChange ? 'Remove revert' : 'Add revert of change to this parameter'}>
                <IconButton icon={{ icon }} onClick={handleClick} />
            </Tooltip>
        );
    }

    return (
        <Tooltip content={foundChange ? 'Remove revert' : 'Add revert'}>
            <IconButton icon={{ icon }} onClick={handleClick} />
        </Tooltip>
    );
};

export default RevertButton;
