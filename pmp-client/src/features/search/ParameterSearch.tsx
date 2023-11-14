import { Button, Checkbox, CollapsibleList, SimpleListItem, SimpleMenuSurface, TextField } from "rmwc";
import { ParameterType } from "../parameters/types";
import useServices from "../services/useServices";
import useSelectedServices from "../services/useSelectedServices";
import Filter from "./Filter";
import { Service } from "../services/types";
import { FilterData, Namable } from "./types";




const ParameterSearch = () => {
	const services = useServices();
	const [selectedServices, setSelectedServices] = useSelectedServices();

	const serviceCheckedCriteria = (service: Service) =>
		selectedServices.find((s) => s.address === service.address) !== undefined;


	const handleServiceChecked = (service: Service, isChecked: boolean) => {
		if (isChecked) {
			setSelectedServices([...selectedServices, service]);
		} else {
			setSelectedServices(selectedServices.filter((s) => s.address !== service.address));
		}
	}

	return (
		<>
			<TextField
				outlined
				className="search w-full"
				type="text"
				label="Search"
			/>
			<Filter
				filters={[
					{
						name: "Services",
						data: services,
						onChange: handleServiceChecked,
						checkedCriteria: serviceCheckedCriteria,
					},
					{
						name: "Parameter Type",
						data: Object.values(ParameterType).map((type) => ({ name: type })),
						onChange: (dut: Namable, isChecked: boolean) => { },
						checkedCriteria: () => false,
					},
				] as FilterData<Namable>[]}
			/>
		</>
	);
}

export default ParameterSearch;