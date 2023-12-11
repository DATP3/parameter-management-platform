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
    Menu,
    MenuSurfaceAnchor,
    SimpleListItem,
    Tooltip,
    Typography
} from 'rmwc';

import { Revert } from '../types';
import { isRevert } from '../commitStoreHelpers';
import useAuditLogEntries from '../../audit/useAuditLogEntries';
import useCommitStore from '../useCommitStore';
import { useState } from 'react';

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
            return <>{message}</>;
        case 'service':
            return (
                <>
                    {revert.service.name}
                    {message}
                </>
            );
        case 'parameter':
            return (
                <>
                    {revert.parameterName}
                    {message}
                </>
            );
    }
};

const RevertInfoMenu = ({ revert }: { revert: Revert }) => {
    const [open, setOpen] = useState(false);
    const { data: commits } = useAuditLogEntries(''); // TODO: Add query with commit hash, to only get the commit we are reverting

    const getText = () => {
        const commit = commits?.find((c) => c.hash === revert.commitReference);

        const message = commit?.message && (
            <>
                <br />
                <Typography use='caption' style={{ whiteSpace: 'pre-wrap' }}>
                    Message: &quot;{commit.message}&quot;
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
            case 'service':
                return (
                    <>
                        Revert change to service &quot;{revert.service.name}&quot; added in commit{' '}
                        <Typography use='overline'>{revert.commitReference}</Typography>
                        {message}
                    </>
                );
            case 'parameter':
                return (
                    <>
                        Revert change to parameter &quot;{revert.parameterName}&quot; on service &quot;
                        {revert.service.name}&quot; added in commit{' '}
                        <Typography use='overline'>{revert.commitReference}</Typography>
                        {message}
                    </>
                );
        }
    };

    return (
        <MenuSurfaceAnchor>
            <IconButton onClick={() => setOpen(true)} className='-mr-4' icon='info'></IconButton>
            {/* <MenuSurface open={open} onClose={() => setOpen(false)} renderToPortal>
                <div style={{ padding: '1rem', width: '8rem' }}>{getText()}</div>
            </MenuSurface> */}
            <Menu open={open} onClose={() => setOpen(false)} renderToPortal>
                <div className='p-4 w-80'>{getText()}</div>
            </Menu>
        </MenuSurfaceAnchor>
    );
};

/** List of reverts in changelist */
const RevertList = () => {
    const removeChange = useCommitStore((s) => s.removeChange);
    const changes = useCommitStore((s) => s.changes);
    const reverts = changes.filter(isRevert); // Filter outside callback to possibly aviod rerenders

    const handleClick = (e: React.MouseEvent) => {
        e.stopPropagation();
        reverts.forEach((revert) => {
            removeChange(revert);
        });
    };

    if (reverts.length === 0) return null;

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
                            <IconButton icon='delete' onClick={handleClick} />
                        </SimpleListItem>
                    }
                >
                    <DataTable className='dataTable w-full'>
                        <DataTableContent className='tableHead'>
                            <DataTableHead>
                                <DataTableHeadCell style={{ backgroundColor: 'transparent' }}>Type</DataTableHeadCell>
                                <DataTableHeadCell style={{ backgroundColor: 'transparent' }}>
                                    Details
                                </DataTableHeadCell>
                            </DataTableHead>
                            <DataTableBody>
                                {reverts.map((revert, i) => (
                                    <DataTableRow className='tableRow' key={i}>
                                        {/** key i is not optimal */}
                                        <DataTableCell>{revert.revertType}</DataTableCell>
                                        <DataTableCell>
                                            <RevertDetails revert={revert} />
                                        </DataTableCell>
                                        <DataTableCell>
                                            <Tooltip content='See more'>
                                                <RevertInfoMenu revert={revert} />
                                            </Tooltip>
                                        </DataTableCell>
                                        <DataTableCell>
                                            <IconButton
                                                className='-mr-4'
                                                icon='delete'
                                                onClick={() => {
                                                    removeChange(revert);
                                                }}
                                            ></IconButton>
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
