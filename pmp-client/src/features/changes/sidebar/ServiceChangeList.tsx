import {
    CollapsibleList,
    DataTable,
    DataTableBody,
    DataTableContent,
    Grid,
    GridCell,
    IconButton,
    SimpleListItem
} from 'rmwc';

import ParamChangeEntry from './ParamChangeEntry';
import { ParameterChange } from '../types';
import React from 'react';
import useCommitStore from '../useCommitStore';

interface ServiceChangeListProps {
    serviceName: string;
    changes: ParameterChange[];
}

const ServiceChangeList = ({ serviceName, changes }: ServiceChangeListProps) => {
    const removeChange = useCommitStore((s) => s.removeChange);

    const removeChangesFromService = (e: React.MouseEvent<HTMLButtonElement>) => {
        e.stopPropagation();
        changes.forEach((change) => {
            removeChange(change);
        });
    };

    return (
        <>
            <Grid style={{ padding: '0px' }}>
                <GridCell span={12}>
                    <CollapsibleList
                        defaultOpen
                        handle={
                            <SimpleListItem
                                className='serviceListItem'
                                text={serviceName}
                                metaIcon='chevron_right'
                                style={{ padding: '5px' }}
                            >
                                <IconButton icon='delete' onClick={removeChangesFromService} />
                            </SimpleListItem>
                        }
                    >
                        <DataTable className='parameterTable'>
                            <DataTableContent className='tableHead'>
                                <DataTableBody>
                                    {changes.map((change) => (
                                        <ParamChangeEntry key={change.parameter.id} change={change} />
                                    ))}
                                </DataTableBody>
                            </DataTableContent>
                        </DataTable>
                    </CollapsibleList>
                </GridCell>
            </Grid>
        </>
    );
};

export default ServiceChangeList;
