import {
    Button,
    Card,
    CollapsibleList,
    Grid,
    GridCell,
    GridRow,
    IconButton,
    List,
    ListItemSecondaryText,
    ListItemText,
    Typography
} from 'rmwc';

import ServiceChangeList from './ServiceChangeList';
import useCommitStore from '../useCommitStore';
import useEnvironment from '../../environment/useEnvironment';

/**
 * Displays a list of changes made in the current commit.
 */
const ChangeList = () => {
    const serviceChanges = useCommitStore((s) => s.serviceChanges);
    const clearChanges = useCommitStore((s) => s.clear);
    const { environment } = useEnvironment();

    const sortedChanges = serviceChanges.sort((a, b) => a.service.name.localeCompare(b.service.name));
    const hasChanges = sortedChanges.length > 0;

    // TODO: Implement push functionality

    return (
        <div className='flex flex-col h-full'>
            <div className='flex-none'>
                <Grid style={{ padding: '5px' }}>
                    <GridCell span={7}>
                        <Button raised={hasChanges} outlined={!hasChanges} disabled={!hasChanges}>
                            Push to {environment}
                        </Button>
                    </GridCell>
                    <GridCell span={5} style={{ textAlign: 'right' }}>
                        <Button onClick={clearChanges} danger outlined trailingIcon='delete' disabled={!hasChanges}>
                            Delete All
                        </Button>
                    </GridCell>
                </Grid>
            </div>
            <div className='flex-1'>
                {!hasChanges ? (
                    <EmptyEntry />
                ) : (
                    sortedChanges.map((serviceChange) => (
                        <ServiceChangeList key={serviceChange.service.address} serviceChanges={serviceChange} />
                    ))
                )}
            </div>
        </div>
    );
};

const EmptyEntry = () => {
    return (
        <>
            <Card outlined style={{ paddingLeft: '10px', marginLeft: '5px', marginBottom: '5px' }}>
                <GridRow>
                    <GridCell span={9}>
                        <List twoLine>
                            <ListItemText style={{ textAlign: 'left' }}>
                                <Typography use='headline5'>No changes.</Typography>
                            </ListItemText>
                            <ListItemSecondaryText style={{ textAlign: 'left' }}>
                                <Typography use='subtitle1'>Changes will be listed here.</Typography>
                            </ListItemSecondaryText>
                        </List>
                    </GridCell>
                    <GridCell span={3} style={{ textAlign: 'right' }}>
                        <IconButton disabled icon='delete'></IconButton>
                    </GridCell>
                </GridRow>
                <div className='text-right mt-[-42px]'>
                    <CollapsibleList
                        defaultOpen
                        handle={
                            <Button disabled icon='expand_more'>
                                Show
                            </Button>
                        }
                    ></CollapsibleList>
                </div>
            </Card>
        </>
    );
};

export default ChangeList;
