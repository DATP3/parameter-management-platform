import { Grid, GridCell, GridRow, IconButton, List, ListItemPrimaryText, ListItemSecondaryText, TextField } from 'rmwc';

import { ParameterChange } from '../types';
import { Service } from '../../services/types';
import useCommitStore from '../useCommitStore';

const ParamChangeEntry = ({ change, service }: { change: ParameterChange; service: Service }) => {
    // TODO: Make the new value editable. For some reason, if the "value" property of the TextField is set to the new value, the text field is not editable.

    const removeParameterChange = useCommitStore((s) => s.removeParameterChange);

    return (
        <Grid
            style={{ width: '100%', textAlign: 'left', paddingLeft: '10px', paddingRight: '10px', paddingTop: '0px' }}
        >
            <GridCell span={12} style={{ padding: '0px', paddingLeft: '5px', marginTop: '-10px' }}>
                <Grid style={{ padding: '0px' }}>
                    <GridRow>
                        <GridCell span={10}>
                            <List twoLine>
                                <ListItemPrimaryText>{change.parameter.name}</ListItemPrimaryText>
                                <ListItemSecondaryText>{change.parameter.type}</ListItemSecondaryText>
                            </List>
                        </GridCell>
                        <GridCell span={2} style={{ textAlign: 'right', marginTop: '10px' }}>
                            <IconButton
                                onClick={() => {
                                    removeParameterChange(service, change);
                                }}
                                icon='delete'
                            ></IconButton>
                        </GridCell>
                        <GridCell span={12} style={{ marginTop: '-20px', marginBottom: '-15px' }}>
                            <GridRow>
                                <GridCell span={12}>
                                    <TextField
                                        style={{ width: '100%' }}
                                        outlined
                                        value={change.parameter.value.toString()}
                                        label='Old Value'
                                        disabled
                                        type={change.parameter.type}
                                    />
                                </GridCell>
                            </GridRow>
                            <GridRow>
                                <GridCell span={12} style={{ padding: '0px', paddingTop: '10px' }}>
                                    <TextField
                                        style={{ width: '100%' }}
                                        outlined
                                        label='New Value'
                                        value={change.newValue.toString()}
                                        type={change.parameter.type}
                                    />
                                </GridCell>
                            </GridRow>
                        </GridCell>
                    </GridRow>
                </Grid>
            </GridCell>
        </Grid>
    );
};

export default ParamChangeEntry;
