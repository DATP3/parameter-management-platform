import { AuditLogEntryChange, AuditLogEntryParameterChange, AuditLogEntryRevert } from './useAuditLogEntries';
import {
    CollapsibleList,
    DataTable,
    DataTableBody,
    DataTableCell,
    DataTableContent,
    DataTableHead,
    DataTableHeadCell,
    DataTableRow,
    Icon,
    List,
    ListItem,
    ListItemMeta,
    Typography
} from 'rmwc';

import { Fragment } from 'react';
import RevertButton from './RevertButton';
import { Service } from '../services/types';
import { formatDate } from '../../utils/date';
import { useAuditDetailsEntry } from './useAuditDetailsEntry';
import useSelectedServices from '../services/useSelectedServices';
import useServices from '../services/useServices';

interface AuditDetailsServiceCollapsProps extends AuditLogEntryChange {}

const AuditDetailsServiceCollaps = ({ service, parameterChanges, reverts }: AuditDetailsServiceCollapsProps) => {
    const entry = useAuditDetailsEntry();
    const revertChanges = reverts.reduce((acc: AuditDetailsRevertRowProps[], revert) => {
        const c = revert.parameterChanges.map((p) => ({
            ...p,
            revertType: revert.revertType,
            referenceHash: revert.referenceHash,
            service
        }));
        return acc.concat(c);
    }, []);

    return (
        <CollapsibleList
            className='pt-6'
            defaultOpen
            handle={
                <ListItem className='serviceListItem'>
                    <div className='w-full flex justify-between pr-4'>
                        {service.name}{' '}
                        <RevertButton revert={{ revertType: 'service', service, commitReference: entry.hash }} />
                    </div>
                    <ListItemMeta>
                        <Icon icon='chevron_right' />
                    </ListItemMeta>
                </ListItem>
            }
        >
            <DataTable className='w-full'>
                <DataTableContent className='tableHead'>
                    <DataTableHead>
                        <DataTableRow>
                            <DataTableHeadCell className='headCell'>Name</DataTableHeadCell>
                            <DataTableHeadCell className='headCell'>New Value</DataTableHeadCell>
                            <DataTableHeadCell className='headCell'>Old Value</DataTableHeadCell>
                            <DataTableHeadCell className='headCell'>Details</DataTableHeadCell>
                            <DataTableHeadCell className='headCell'>Actions</DataTableHeadCell>
                        </DataTableRow>
                    </DataTableHead>
                    <DataTableBody>
                        {revertChanges.map((rc) => (
                            <AuditDetailsRevertRow key={rc.name} {...rc} />
                        ))}
                        {parameterChanges.map((p) => (
                            <AuditDetailsParameterChangeRow key={p.name} {...p} service={service} />
                        ))}
                    </DataTableBody>
                </DataTableContent>
            </DataTable>
        </CollapsibleList>
    );
};

interface AuditDetailsParameterChangeRowProps extends AuditLogEntryParameterChange {
    service: Service;
}

const AuditDetailsParameterChangeRow = ({ name, newValue, oldValue, service }: AuditDetailsParameterChangeRowProps) => {
    const entry = useAuditDetailsEntry();
    return (
        <DataTableRow className='tableRow'>
            <DataTableCell>{name}</DataTableCell>
            <DataTableCell>{newValue}</DataTableCell>
            <DataTableCell>{oldValue}</DataTableCell>
            <DataTableCell></DataTableCell>
            <DataTableCell>
                <RevertButton
                    revert={{ revertType: 'parameter', parameterName: name, service, commitReference: entry.hash }}
                />
            </DataTableCell>
        </DataTableRow>
    );
};

interface AuditDetailsRevertRowProps {
    service: Service;
    referenceHash: AuditLogEntryRevert['referenceHash'];
    revertType: AuditLogEntryRevert['revertType'];
    name: AuditLogEntryParameterChange['name'];
    newValue: AuditLogEntryParameterChange['newValue'];
    oldValue: AuditLogEntryParameterChange['oldValue'];
}

const AuditDetailsRevertRow = ({
    name,
    newValue,
    oldValue,
    referenceHash,
    revertType,
    service
}: AuditDetailsRevertRowProps) => {
    const entry = useAuditDetailsEntry();

    return (
        <DataTableRow className='tableRow'>
            <DataTableCell>{name}</DataTableCell>
            <DataTableCell>{newValue}</DataTableCell>
            <DataTableCell>{oldValue}</DataTableCell>
            <DataTableCell>
                <Typography use='body2'>Revert of type: {revertType}</Typography>
                <br />
                <Typography use='body2'>Reference: {referenceHash}</Typography>
            </DataTableCell>

            <DataTableCell>
                <RevertButton
                    revert={{ revertType: 'parameter', parameterName: name, service, commitReference: entry.hash }}
                />
            </DataTableCell>
        </DataTableRow>
    );
};

interface AuditDetailsProps {}

const AuditDetails = (_: AuditDetailsProps) => {
    const entry = useAuditDetailsEntry();
    const { data: allServices } = useServices();
    const [selectedServices] = useSelectedServices();

    const affectedServices = entry.affectedServices.map((s) => ({
        serviceName: s,
        selected: selectedServices.some((ss) => ss.name === s),
        connected: allServices?.some((ss) => ss.name === s)
    }));

    return (
        <div className='flex'>
            <div className='flex-none'>
                <div className='pb-1'>
                    <Typography use='subtitle1'>Date:</Typography>
                    <br />
                    <Typography use='subtitle2'>{formatDate(entry.pushDate)}</Typography>
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
                <div>
                    <Typography use='subtitle1'>Affected services:</Typography>
                    <br />
                    {affectedServices.map((s) => (
                        <Fragment key={s.serviceName}>
                            <Typography key={s.serviceName} use='subtitle2'>
                                {s.serviceName} -{' '}
                                {!s.connected ? 'not connected' : s.selected ? 'selected' : 'not selected'}
                            </Typography>
                            <br />
                        </Fragment>
                    ))}
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
