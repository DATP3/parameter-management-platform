import {
    CollapsibleList,
    DataTable,
    DataTableBody,
    DataTableCell,
    DataTableContent,
    DataTableHead,
    DataTableHeadCell,
    DataTableRow,
    Grid,
    GridCell,
    IconButton,
    SimpleListItem,
    Typography
} from 'rmwc';

import { Revert } from '../types';
import { isRevert } from '../commitStoreHelpers';
import useAuditLogEntries from '../../audit/useAuditLogEntries';
import useCommitStore from '../useCommitStore';

interface RevertDetailsProps {
    revert: Revert;
}

const RevertDetails = ({ revert }: RevertDetailsProps) => {
    const { data: commits } = useAuditLogEntries(''); // TODO: Add query with commit hash, to only fetch the commit we are reverting
    const commit = commits?.find((c) => c.hash === revert.commitReference);

    const message = commit?.message && (
        <>
            <br />
            <Typography use='caption' style={{ whiteSpace: 'pre-wrap' }}>
                &quot;{commit.message}&quot;
            </Typography>
        </>
    );

    switch (revert.revertType) {
        case 'commit':
            return (
                <>
                    Revert changes added in <Typography use='overline'>{revert.commitReference}</Typography>
                    {message}
                </>
            );
        case 'parameter':
            return (
                <>
                    Revert change to parameter &quot;{revert.parameterName}&quot; added in commit{' '}
                    <Typography use='overline'>{revert.commitReference}</Typography>
                    {message}
                </>
            );
    }
};

const RevertList = () => {
    const changes = useCommitStore((s) => s.changes);
    const reverts = changes.filter(isRevert); // Filter outside callback to possibly aviod rerenders

    return (
        <Grid style={{ padding: '0px' }}>
            <GridCell span={12}>
                {/** This grid approach is only used to get the same layout as in ./ServiceChaneList, which already exists */}
                <CollapsibleList
                    defaultOpen
                    handle={
                        <SimpleListItem
                            className='serviceListItem'
                            text={'Reverts'}
                            metaIcon='chevron_right'
                            style={{ padding: '5px' }}
                        >
                            <IconButton icon='delete' />
                        </SimpleListItem>
                    }
                >
                    <DataTable className='dataTable'>
                        <DataTableContent className='tableHead'>
                            <DataTableHead>
                                <DataTableHeadCell style={{backgroundColor: "transparent"}}>Type</DataTableHeadCell>
                                <DataTableHeadCell style={{backgroundColor: "transparent"}}>Details</DataTableHeadCell>
                            </DataTableHead>
                            <DataTableBody>
                                {reverts.map((revert, i) => (
                                    <DataTableRow className="talbeRow" key={i}>
                                        {/** key i is not optimal */}
                                        <DataTableCell>{revert.revertType}</DataTableCell>
                                        <DataTableCell>
                                            <RevertDetails revert={revert} />
                                        </DataTableCell>
                                    </DataTableRow>
                                ))}
                            </DataTableBody>
                        </DataTableContent>
                    </DataTable>
                </CollapsibleList>
            </GridCell>
        </Grid>
    );
};

export default RevertList;
