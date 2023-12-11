import {
    DataTableCell,
    DataTableRow,
    IconButton,
    List,
    ListItemPrimaryText,
    ListItemSecondaryText,
    Typography
} from 'rmwc';
import { ParameterChange, ParameterValue } from '../types';

import Input from '../../parameters/Input';
import useCommitStore from '../useCommitStore';
import validateParamChange from '../validateParamChange';

interface ParamChangeEntryProps {
    change: ParameterChange;
}

/** Single parameter change row in change list */
const ParamChangeEntry = ({ change }: ParamChangeEntryProps) => {
    const removeChange = useCommitStore((s) => s.removeChange);
    const addParameterChange = useCommitStore((s) => s.addChange);

    const isValid = validateParamChange(change);
    const handleParameterChange = (newValue: ParameterValue) => {
        addParameterChange({ ...change, newValue });
    };

    return (
        <>
            <DataTableRow style={{ borderBottom: 'none' }}>
                <DataTableCell className='headCell'>
                    <List twoLine style={{ padding: '0px', paddingBottom: '5px' }}>
                        <ListItemPrimaryText>{change.parameter.name}</ListItemPrimaryText>
                        <ListItemSecondaryText>{change.parameter.type}</ListItemSecondaryText>
                    </List>
                </DataTableCell>
                <DataTableCell alignEnd>
                    <IconButton
                        className='-mr-3'
                        icon='delete'
                        onClick={() => {
                            removeChange(change);
                        }}
                    ></IconButton>
                </DataTableCell>
            </DataTableRow>
            <DataTableRow className='tableRow' style={{ borderTop: 'none', borderBottom: 'none' }}>
                <DataTableCell className='w-1'>
                    <Typography use='body1'>Old</Typography>
                </DataTableCell>
                <DataTableCell style={{ padding: '10px' }}>
                    <Input
                        type={change.parameter.type}
                        disabled
                        value={change.parameter.value}
                        isValid={true}
                        onParamChange={() => {}}
                    ></Input>
                </DataTableCell>
            </DataTableRow>
            <DataTableRow className='tableRow' style={{ borderTop: 'none' }}>
                <DataTableCell>
                    <Typography use='body1'>New</Typography>
                </DataTableCell>
                <DataTableCell style={{ padding: '10px', paddingTop: '7px' }}>
                    <Input
                        isValid={isValid}
                        value={change.newValue}
                        type={change.parameter.type}
                        onParamChange={handleParameterChange}
                    />
                </DataTableCell>
            </DataTableRow>
        </>
    );
};

export default ParamChangeEntry;
