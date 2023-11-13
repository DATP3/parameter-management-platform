import {Button, Card, CollapsibleList, DataTable, DataTableBody, DataTableContent, DataTableHead, DataTableHeadCell, DataTableRow, Grid, GridCell, GridRow, IconButton, List, ListItemSecondaryText, ListItemText, SimpleListItem, Typography} from "rmwc";
import { ParameterChange, ServiceChanges } from "../types";

import ParamChangeEntry from "./ParamChangeEntry";
import ParameterChangeList from "./ParameterChangeList";
import React from "react";
import useCommitStore from "../useCommitStore";

const ServiceChangeList = ({ serviceChanges }: {serviceChanges: ServiceChanges}) => {

    const [open, setOpen] = React.useState(false);
    const buttonText = open ? "Collapse" : "Show";
    const buttonIcon = open ? "chevron_right" : "expand_more";

    const removeParameterChange = useCommitStore((s) => s.removeParameterChange);

    const removeChangesFromService = () => {
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
                        />
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