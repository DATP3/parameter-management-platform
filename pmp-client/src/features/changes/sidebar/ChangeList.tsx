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

import RevertList from './RevertList';
import ServiceChangeList from './ServiceChangeList';
import { groupBy } from '../../../utils/array';
import { isParameterChange } from '../commitStoreHelpers';
import useCommitStore from '../useCommitStore';
import useEnvironment from '../../environment/useEnvironment';

/**
 * Displays a list of changes made in the current commit.
 */
const ChangeList = () => {
    const changes = useCommitStore((s) => s.changes);
    const clearChanges = useCommitStore((s) => s.clear);
    const { environment } = useEnvironment();

    const parameterChanges = changes.filter(isParameterChange);
    const hasChanges = changes.length > 0;
    const serviceChanges = groupBy(parameterChanges, (c) => c.service.name);
    const sortedNames = Object.keys(serviceChanges).sort();

    // TODO: Implement push functionality

    return (
        <div className='flex flex-col h-full'>
            <div className='flex-none'>
                <Grid style={{ padding: '0px', paddingRight: '0px', paddingLeft: '5px', paddingBottom: '5px' }}>
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
                    <>
                        <RevertList />
                        {sortedNames.map((name) => (
                            <ServiceChangeList key={name} serviceName={name} changes={serviceChanges[name]} />
                        ))}
                    </>
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
