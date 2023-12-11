import {
    Grid,
    GridCell,
    GridRow,
    IconButton,
    List,
    ListItemPrimaryText,
    ListItemSecondaryText,
    Typography
} from 'rmwc';

import ChangeList from './ChangeList';
import useCommitStore from '../useCommitStore';
import useEnvironment from '../../environment/useEnvironment';

/** List of all pending changes */
const PendingChanges = () => {
    const environment = useEnvironment();
    const undo = useCommitStore((s) => s.undo);
    const redo = useCommitStore((s) => s.redo);

    return (
        <>
            <div>
                <Grid style={{ padding: '0px', height: '100%', width: '100%' }}>
                    <GridRow style={{ paddingLeft: '10px' }}>
                        <GridCell span={12} style={{}}>
                            <GridRow style={{ padding: '0px', marginLeft: '-6px' }}>
                                <GridCell span={8} style={{}}>
                                    <List twoLine>
                                        <ListItemPrimaryText>
                                            <Typography use='headline5'>Pending Changes</Typography>
                                        </ListItemPrimaryText>
                                        <ListItemSecondaryText>
                                            <Typography use='subtitle1'>
                                                environment: {environment.environment}
                                            </Typography>
                                        </ListItemSecondaryText>
                                    </List>
                                </GridCell>
                                <GridRow style={{ paddingTop: '10px', paddingLeft: '40px' }}>
                                    <GridCell span={2}>
                                        <IconButton icon='undo' onClick={undo}></IconButton>
                                    </GridCell>
                                    <GridCell span={2} style={{ textAlign: 'center' }}>
                                        <IconButton icon='redo' onClick={redo}></IconButton>
                                    </GridCell>
                                </GridRow>
                            </GridRow>
                        </GridCell>
                    </GridRow>
                    <GridRow style={{ height: '100%', marginTop: '-10px' }}>
                        <GridCell span={12} style={{ height: '100%' }}>
                            <ChangeList />
                        </GridCell>
                    </GridRow>
                </Grid>
            </div>
        </>
    );
};

export default PendingChanges;
