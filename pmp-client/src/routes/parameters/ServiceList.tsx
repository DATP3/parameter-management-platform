import ServiceListElement from './ServiceListElement';
import { Typography } from 'rmwc';
import useSelectedServices from '../../features/services/useSelectedServices';

/** Lists all services. Each service then fetches its parameters */
const ServiceList = () => {
    const [services] = useSelectedServices();
    const sortedServices = services.sort((a, b) => a.name.localeCompare(b.name));

    return (
        <>
            {sortedServices.map((service) => (
                <ServiceListElement key={service.address} service={service} />
            ))}
            {sortedServices.length === 0 && (
                <div className='mt-2'>
                    <Typography use='subtitle1'>Choose a service from the list in the left drawer</Typography>
                </div>
            )}
        </>
    );
};

export default ServiceList;
