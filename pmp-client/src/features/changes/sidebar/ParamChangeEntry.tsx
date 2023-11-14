import { DataTableCell, DataTableRow, Grid, GridCell, GridRow, IconButton, List, ListItemPrimaryText, ListItemSecondaryText, TextField } from "rmwc";
import { ParameterChange, ParameterValue } from "../types";

import Input from "../../parameters/Input";
import { Service } from "../../services/types";
import { isValid } from "zod";
import useCommitStore from "../useCommitStore";
import validateParamChange from "../validateParamChange";

const ParamChangeEntry = ({ change, service }: { change: ParameterChange, service: Service }) => {

    // TODO: Make the new value editable. For some reason, if the "value" property of the TextField is set to the new value, the text field is not editable.

    const removeParameterChange = useCommitStore((s) => s.removeParameterChange);
    const addParameterChange = useCommitStore((s) => s.addParameterChange);

    const parameter = change.parameter;
    const newValue = change.newValue;
    const isValid = validateParamChange({parameter, newValue})
    const handleParameterChange = (newValue: ParameterValue) => {
        addParameterChange(service, { parameter, newValue })
    }


    return (
        <>
            <DataTableRow style={{borderBottom: 'none'}}>
                <DataTableCell className="headCell">
                    <List twoLine style={{padding: '0px', paddingBottom: '5px'}}>
                        <ListItemPrimaryText>{change.parameter.name}</ListItemPrimaryText>
                        <ListItemSecondaryText>{change.parameter.type}</ListItemSecondaryText>
                    </List>
                </DataTableCell>
            </DataTableRow>
            <DataTableRow className="tableRow" style={{borderTop: 'none', borderBottom: 'none'}}>
                <DataTableCell style={{padding: '10px'}}>
                    <TextField 
                        style={{width: '100%'}} 
                        outlined 
                        value={change.parameter.value.toString()} 
                        label="Old Value" 
                        disabled 
                        type={change.parameter.type}/>
                </DataTableCell>
            </DataTableRow>
            <DataTableRow className="tableRow" style={{borderTop: 'none'}}>
                <DataTableCell style={{padding: '10px', paddingTop: '7px'}}>
                    <Input isValid={isValid} value={change.newValue} type={change.parameter.type} onParamChange={handleParameterChange}>
                    </Input>

                    <TextField 
                        style={{width: '100%'}} 
                        outlined 
                        label="New Value" 
                        value={change.newValue.toString()} 
                        type={change.parameter.type}
                        trailingIcon={{
                            icon: 'delete',
                            tabIndex: 0,
                            onClick: () => removeParameterChange(service, change)
                        }}/>
                </DataTableCell>
            </DataTableRow>
        </>
    )


}

export default ParamChangeEntry;