import {
    AuditLogEntry,
    AuditLogEntryChange,
    AuditLogEntryParameterChange,
    AuditLogEntryRevert
} from './useAuditLogEntries';
import {
    CollapsibleList,
    DataTable,
    DataTableBody,
    DataTableCell,
    DataTableContent,
    DataTableHead,
    DataTableHeadCell,
    DataTableRow,
    List,
    SimpleListItem,
    Typography
} from 'rmwc';

interface AuditDetailsServiceCollapsProps extends AuditLogEntryChange {}

const AuditDetailsServiceCollaps = ({ service, parameterChanges, reverts }: AuditDetailsServiceCollapsProps) => {
    const revertChanges = reverts.reduce((acc: AuditDetailsRevertRowProps[], revert) => {
        const c = revert.parameterChanges.map((p) => ({
            ...p,
            revertType: revert.revertType,
            referenceHash: revert.referenceHash
        }));
        return acc.concat(c);
    }, []);

    return (
        <CollapsibleList
            defaultOpen
            handle={<SimpleListItem className='serviceListItem' text={service.name} metaIcon='chevron_right' />}
        >
            <DataTable className='w-full'>
                <DataTableContent>
                    <DataTableHead>
                        <DataTableRow>
                            <DataTableHeadCell>Name</DataTableHeadCell>
                            <DataTableHeadCell>New Value</DataTableHeadCell>
                            <DataTableHeadCell>Old Value</DataTableHeadCell>
                            <DataTableHeadCell>Details</DataTableHeadCell>
                        </DataTableRow>
                    </DataTableHead>
                    <DataTableBody>
                        {revertChanges.map((rc) => (
                            <AuditDetailsRevertRow key={rc.name} {...rc} />
                        ))}
                        {parameterChanges.map((p) => (
                            <AuditDetailsParameterChangeRow key={p.name} {...p} />
                        ))}
                    </DataTableBody>
                </DataTableContent>
            </DataTable>
        </CollapsibleList>
    );
};

interface AuditDetailsParameterChangeRowProps extends AuditLogEntryParameterChange {}

const AuditDetailsParameterChangeRow = ({ name, newValue, oldValue }: AuditDetailsParameterChangeRowProps) => {
    return (
        <DataTableRow>
            <DataTableCell>{name}</DataTableCell>
            <DataTableCell>{newValue}</DataTableCell>
            <DataTableCell>{oldValue}</DataTableCell>
            <DataTableCell></DataTableCell>
        </DataTableRow>
    );
};

interface AuditDetailsRevertRowProps {
    referenceHash: AuditLogEntryRevert['referenceHash'];
    revertType: AuditLogEntryRevert['revertType'];
    name: AuditLogEntryParameterChange['name'];
    newValue: AuditLogEntryParameterChange['newValue'];
    oldValue: AuditLogEntryParameterChange['oldValue'];
}

const AuditDetailsRevertRow = ({ name, newValue, oldValue, referenceHash, revertType }: AuditDetailsRevertRowProps) => {
    return (
        <DataTableRow>
            <DataTableCell>{name}</DataTableCell>
            <DataTableCell>{newValue}</DataTableCell>
            <DataTableCell>{oldValue}</DataTableCell>
            <DataTableCell>
                <Typography use='body2'>Revert of type: {revertType}</Typography>
                <br />
                <Typography use='body2'>Reference: {referenceHash}</Typography>
            </DataTableCell>
        </DataTableRow>
    );
};

interface AuditDetailsProps {
    entry: AuditLogEntry;
}

const AuditDetails = ({ entry }: AuditDetailsProps) => {
    return (
        <div className='flex'>
            <div className='flex-none'>
                <div className='pb-1'>
                    <Typography use='subtitle1'>Date:</Typography>
                    <br />
                    <Typography use='subtitle2'>{entry.pushDate.toLocaleString()}</Typography>
                </div>
                <div className='pb-1'>
                    <Typography use='subtitle1'>User:</Typography>
                    <br />
                    <Typography use='subtitle2'>{entry.email}</Typography>
                </div>
                <div>
                    <Typography use='subtitle1'>Hash:</Typography>
                    <br />
                    <Typography use='subtitle2'>{entry.hash}</Typography>
                </div>
            </div>
            <div className='flex-1 pl-4'>
                <List>
                    {entry.changes.map((c) => (
                        <AuditDetailsServiceCollaps key={c.service.address} {...c} />
                    ))}
                </List>
            </div>
        </div>
    );
};

export default AuditDetails;
