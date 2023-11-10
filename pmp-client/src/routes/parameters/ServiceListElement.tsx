import { CollapsibleList, SimpleListItem } from "rmwc";
import ParameterList from "./ParameterList";
import { useEffect, useState } from "react";
import { Parameter, ParameterType } from "../../features/parameters/types";
import { Service } from "../../features/services/types";

const parametersMock: Parameter[] = [
	{
		id: "1",
		name: "key1",
		type: "integer",
		value: "2",
	},
	{
		id: "2",
		name: "key2",
		type: "string",
		value: "value2erfwsdhfdiweuidsnviuwendiukweds",
	},
	{
		id: "3",
		name: "key3",
		type: ParameterType.LOCALDATE,
		value: "2020-10-20",
	},
	{
		id: "4",
		name: "key4",
		type: ParameterType.BOOLEAN,
		value: "true",
	},
	{
		id: "5",
		name: "key5",
		type: ParameterType.CHARACTER,
		value: "t",
	},
	{
		id: "6",
		name: "key6",
		type: ParameterType.LOCALDATETIME,
		value: "2023-11-16T12:13",
	},
	{
		id: "7",
		name: "key7",
		type: ParameterType.FLOAT,
		value: "0.3",
	},
];

interface ServiceListElementProps {
	service: Service;
}

const ServiceListElement = (props: ServiceListElementProps) => {
	const { service } = props;
	const [parameters, setParameters] = useState<Parameter[]>([]);

	useEffect(() => {
		// Use fetch instead of mock
		setParameters(parametersMock);
	}, []);

	// const parameters = fetchParameters(service);

	const hasParms = parameters.length > 0;


	return (
		<CollapsibleList
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