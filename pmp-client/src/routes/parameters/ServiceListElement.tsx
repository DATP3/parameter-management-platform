import { CollapsibleList, SimpleListItem, Typography } from 'rmwc';

import ParameterList from './ParameterList';
import { Service } from '../../features/services/types';
import useParameterQuery from '../../features/parameters/useParameterQuery';
import { useEffect } from 'react';

interface ServiceListElementProps {
    service: Service;
}

const ServiceListElement = ({ service }: ServiceListElementProps) => {
    const { data, error } = useParameterQuery(service);

    return (
        <CollapsibleList
            defaultOpen
            handle={<SimpleListItem className='serviceListItem' text={service.name} metaIcon='chevron_right' />}
        >
            {data?.parameters && <ParameterList service={service} parameters={data.parameters} />}
            {error && <Typography use='headline6'>Error loading parameters</Typography>}
        </CollapsibleList>
    );
};

export default ServiceListElement;
