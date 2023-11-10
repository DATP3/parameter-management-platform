import { Button, Card, CollapsibleList, Grid, GridCell, GridRow, IconButton, List, ListItemSecondaryText, ListItemText, Typography } from 'rmwc';
import useCommitStore from '../useCommitStore';
import useEnvironment from '../../environment/useEnvironment';
import ServiceChangeList from './ServiceChangeList';

/**
 * Displays a list of changes made in the current commit.
 */
const ChangeList = () => {

    const serviceChanges = useCommitStore((s) => s.serviceChanges).sort((a, b) => a.service.name.localeCompare(b.service.name));
    const clearChanges = useCommitStore((s) => s.clear);
    const environment = useEnvironment().environment;

    if (serviceChanges.length === 0) return <EmptyChangeList/>;

    // TODO: Implement push functionality

    return (
        <>
            <div style={{padding: '0px'}}>
                <GridCell span={12} style={{overflow: 'clip'}}>
                    <GridRow style={{padding: '5px'}}>
                        <GridCell span={7}>
                            <Button raised onClick={() => {}}>Push to {environment}</Button>
                        </GridCell>
                        <GridCell span={5} style={{textAlign: 'right'}}>
                            <Button onClick={clearChanges} danger outlined trailingIcon="delete">Delete All</Button>
                        </GridCell>
                    </GridRow>
                    <GridRow style={{padding: '0px'}}>
                        <GridCell span={12} style={{padding: '0px'}}>
                            <GridRow style={{padding: '0px'}}>
                                <GridCell span={12} style={{maxHeight: '80vh', overflowY: 'scroll', padding: '0px'}}>
                                    {serviceChanges.map((serviceChange) => (
                                        <ServiceChangeList key={serviceChange.service.address} serviceChanges={serviceChange}/>
                                    ))}
                                </GridCell>
                            </GridRow>
                        </GridCell>
                    </GridRow>
                </GridCell>
            </div>
        </>
    );
};

const EmptyChangeList = () => {

    const environment = useEnvironment().environment;

    return (
        <>
            <div style={{padding: '0px'}}>
                <GridCell span={12} style={{overflow: 'clip'}}>
                    <GridRow style={{padding: '5px'}}>
                        <GridCell span={7}>
                            <Button outlined disabled>Push to {environment}</Button>
                        </GridCell>
                        <GridCell span={5} style={{textAlign: 'right'}}>
                            <Button disabled danger outlined trailingIcon="delete">Delete All</Button>
                        </GridCell>
                    </GridRow>
                    <GridRow style={{padding: '0px'}}>
                        <GridCell span={12} style={{padding: '0px'}}>
                            <GridRow style={{padding: '0px'}}>
                                <GridCell span={12} style={{maxHeight: '80vh', overflowY: 'scroll', padding: '0px'}}>
                                    <EmptyEntry/>
                                </GridCell>
                            </GridRow>
                        </GridCell>
                    </GridRow>
                </GridCell>
            </div>
        </>
    );
}

const EmptyEntry = () => {

    return (
        <Card outlined style={{paddingLeft: '10px', marginLeft: '5px', marginBottom: '5px'}}>
            <Grid style={{padding: '0px', width: '100%'}}>
                <GridCell span={12}>
                    <GridRow>
                        <GridCell span={9}>
                            <List twoLine>
                                <ListItemText style={{textAlign: 'left'}}>
                                    <Typography use="headline5">No changes.</Typography>
                                </ListItemText>
                                <ListItemSecondaryText style={{textAlign: 'left'}}>
                                    <Typography use="subtitle1">Changes will be listed here.</Typography>
                                </ListItemSecondaryText>
                            </List>
                        </GridCell>
                        <GridCell span={3} style={{textAlign: 'right'}}>
                            <IconButton disabled icon="delete"></IconButton>
                        </GridCell>
                    </GridRow>
                    <GridRow style={{marginTop: '-42px'}}>
                        <GridCell span={12} style={{textAlign: 'right', padding: '0px'}}>
                            <CollapsibleList defaultOpen handle={<Button disabled icon="expand_more">Show</Button>}>
                            </CollapsibleList>
                        </GridCell>
                    </GridRow>
                </GridCell>
            </Grid>
        </Card>
    )

}

export default ChangeList;
