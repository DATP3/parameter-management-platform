import {CollapsibleList, DataTable, DataTableBody, DataTableContent, Grid, GridCell, IconButton, SimpleListItem} from "rmwc";
import { ParameterChange, ServiceChanges } from "../types";

import ParamChangeEntry from "./ParamChangeEntry";
import React from "react";
import useCommitStore from "../useCommitStore";

const ServiceChangeList = ({ serviceChanges }: {serviceChanges: ServiceChanges}) => {

    const removeParameterChange = useCommitStore((s) => s.removeParameterChange);

    const removeChangesFromService = (e: React.MouseEvent<HTMLButtonElement>) => {
        e.stopPropagation();
        serviceChanges.parameterChanges.forEach((change) => {
            removeParameterChange(serviceChanges.service, change);
        }
        );
    }

    const sortedChanges = serviceChanges.parameterChanges.sort((a, b) => a.parameter.name.localeCompare(b.parameter.name));

    return (
        <>
            <Grid style={{padding: '0px'}}>
                <GridCell span={12}>
                <CollapsibleList
                    defaultOpen
                    handle={
                        <SimpleListItem
                            className="serviceListItem"
                            text={serviceChanges.service.name}
                            secondaryText={serviceChanges.service.address}
                            metaIcon="chevron_right"
                            style={{padding: '5px'}}
                            
                        ><IconButton icon="delete" onClick={removeChangesFromService}/></SimpleListItem>
                    }
                >
                        <DataTable className="parameterTable">
                        <DataTableContent className="tableHead">
                            <DataTableBody>
                                {sortedChanges.map((change: ParameterChange) => (
                                    <ParamChangeEntry key={change.parameter.id} service={serviceChanges.service} change={change} />
                                ))}
                            </DataTableBody>
                        </DataTableContent>
                        </DataTable>
                </CollapsibleList>
                </GridCell>
            </Grid>
        </>
    )
}

export default ServiceChangeList;