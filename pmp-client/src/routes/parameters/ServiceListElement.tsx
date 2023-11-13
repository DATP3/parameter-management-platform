import { CollapsibleList, SimpleListItem } from "rmwc";
import ParameterList from "./ParameterList";
import { Service } from "../../features/services/types";
import useParameterQuery from "../../features/parameters/useParameterQuery";

interface ServiceListElementProps {
	service: Service;
}

const ServiceListElement = ({ service }: ServiceListElementProps) => {
	const { data: parameters } = useParameterQuery(service);
	const hasParms = parameters.length > 0;

	return (
		<CollapsibleList
			defaultOpen
			handle={
				<SimpleListItem
					className="serviceListItem"
					text={service.name}
					metaIcon="chevron_right"
				/>
			}
		>
			{hasParms && <ParameterList service={service} parameters={parameters} />}
		</CollapsibleList>
	);
};


export default ServiceListElement;