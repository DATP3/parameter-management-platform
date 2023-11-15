import { useEffect } from "react";
import useSelectedServices from "../services/useSelectedServices";
import useServices from "../services/useServices";
import { useParameterFilter } from "./useParamererFilter";
import { Service } from "../services/types";
import { FilterData, Namable } from "./types";
import { ParameterType } from "../parameters/types";
import Filter from "./Filter";
import useEnvironment from "../environment/useEnvironment";

const ParameterFilter = () => {
	const services = useServices();
	const [selectedServices, setSelectedServices] = useSelectedServices();
	const [filter, setFilter] = useParameterFilter();

	useEffect(() => {
		setAllTypes(true);
	}, []);

	const serviceCheckedCriteria = (service: Service) =>
		selectedServices.find((s) => s.address === service.address) !== undefined;


	const handleServiceChecked = (service: Service, isChecked: boolean) => {
		if (isChecked) {
			setSelectedServices([...selectedServices, service]);
		} else {
			setSelectedServices(selectedServices.filter((s) => s.address !== service.address));
		}
	}

	const handleTypeChecked = (data: Namable, isChecked: boolean) => {
		const { name: type } = data as { name: ParameterType };
		const currentTypes = filter.types ?? [];

		if (isChecked) {
			setFilter({ ...filter, types: [...currentTypes, type] });
		} else {
			setFilter({ ...filter, types: currentTypes.filter((t) => t !== type) });
		}
	}

	const typeCheckedCriteria = (data: Namable) => {
		const { name: type } = data as { name: ParameterType };
		const currentTypes = filter.types ?? [];

		return currentTypes.find((t) => t === type) !== undefined;
	}

	const setAllTypes = (isSelected: boolean) => {
		if (isSelected) {
			setFilter({ ...filter, types: Object.values(ParameterType) });
		} else {
			setFilter({ ...filter, types: [] });
		}
	}

	const setAllServices = (isSelected: boolean) => {
		if (isSelected) {
			setSelectedServices(services);
		} else {
			setSelectedServices([]);
		}

	}


	return (
		<Filter
			filters={[
				{
					name: "Services",
					data: services,
					setAll: setAllServices,
					onChange: handleServiceChecked,
					checkedCriteria: serviceCheckedCriteria,
				},
				{
					name: "Parameter Types",
					data: Object.values(ParameterType).map((type) => ({ name: type })),
					setAll: setAllTypes,
					onChange: handleTypeChecked,
					checkedCriteria: typeCheckedCriteria,
				},
			] as FilterData[]}
		/>

	);
}

export default ParameterFilter;