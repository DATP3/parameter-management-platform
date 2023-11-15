import ServiceListElement from './ServiceListElement';
import useSelectedServices from '../../features/services/useSelectedServices';

const ServiceList = () => {
	const [services] = useSelectedServices();
	const sortedServices = services.sort((a, b) => a.name.localeCompare(b.name));

	return (
		<>
			{sortedServices.map((service) => (
				<ServiceListElement key={service.address} service={service} />
			))}
		</>
	);
};

export default ServiceList;
