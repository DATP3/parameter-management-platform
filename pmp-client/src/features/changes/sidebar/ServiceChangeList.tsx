import {Button, Grid, GridCell, CollapsibleList, GridRow, IconButton, Typography, ListItemText, List, ListItemSecondaryText, Card } from "rmwc";
import useCommitStore from "../useCommitStore";
import { ServiceChanges } from "../types";
import ParamChangeEntry from "./ParamChangeEntry";
import React from "react";

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

    return (
        <>
            <Card outlined style={{paddingLeft: '10px', marginLeft: '5px', marginBottom: '5px'}}>
                <Grid style={{padding: '0px'}}>
                    <GridCell span={12}>
                        <GridRow>
                            <GridCell span={9}>
                                <List twoLine>
                                    <ListItemText>
                                        <Typography use="headline5">{serviceChanges.service.name}</Typography>
                                    </ListItemText>
                                    <ListItemSecondaryText style={{}}>
                                        <Typography use="subtitle1">{serviceChanges.service.address}</Typography>
                                    </ListItemSecondaryText>
                                </List>
                            </GridCell>
                            <GridCell span={3} style={{textAlign: 'right'}}>
                                <IconButton onClick={removeChangesFromService} icon="delete"></IconButton>
                            </GridCell>
                        </GridRow>
                        <GridRow style={{marginTop: '-42px'}}>
                            <GridCell span={12} style={{textAlign: 'right', padding: '0px'}}>
                                <CollapsibleList defaultOpen onOpen={() => {setOpen(true)}} onClose={() => setOpen(false)} handle={<Button icon={buttonIcon}>{buttonText}</Button>}>
                                    {serviceChanges.parameterChanges.map((parameterChange) => (
                                        <Card key={parameterChange.parameter.id} outlined style={{padding: '0px', marginBottom: '5px', marginRight: '5px'}}>
                                            <ParamChangeEntry change={parameterChange} service={serviceChanges.service}/>
                                        </Card>
                                    ))}
                                </CollapsibleList>
                            </GridCell>
                        </GridRow>
                    </GridCell>
                </Grid>
            </Card>
        </>
    )
}

export default ServiceChangeList;