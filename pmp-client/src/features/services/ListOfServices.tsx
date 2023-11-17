import {
	Checkbox,
	CircularProgress,
	DataTable,
	DataTableBody,
	DataTableCell,
	DataTableContent,
	DataTableHead,
	DataTableHeadCell,
	DataTableRow,
	Typography
} from 'rmwc';

import { ChangeEvent } from 'react';
import useSelectedServices from './useSelectedServices';
import useServices from './useServices';

const ListofServices = () => {
	const { data: list, isPending, error } = useServices();
	const [selectedServices, setSelectedServices] = useSelectedServices();

	if (isPending)
		return (
			<>
				<Typography use='headline6'>Waiting on services</Typography> <CircularProgress />
			</>
		);

	if (error) return <Typography use='headline6'>Error loading services</Typography>;

	const sortedServices = list.sort((s1, s2) => s1.name.localeCompare(s2.name));

	return (
		<DataTable>
			<DataTableContent>
				<DataTableHead>
					<DataTableRow>
						<DataTableHeadCell>Services</DataTableHeadCell>
						<DataTableHeadCell hasFormControl alignEnd>
							<Checkbox
								checked={selectedServices.length === list.length}
								onChange={(evt: ChangeEvent<HTMLInputElement>) => {
									if (evt.currentTarget.checked) {
										setSelectedServices(list);
									} else {
										setSelectedServices([]);
									}
								}}
							/>
						</DataTableHeadCell>
					</DataTableRow>
				</DataTableHead>
				<DataTableBody>
					{sortedServices.map((s) => (
						<DataTableRow key={s.name}>
							<DataTableCell>{s.name}</DataTableCell>
							<DataTableCell hasFormControl>
								<Checkbox
									checked={!!selectedServices.find((service) => s.address === service.address)}
									onChange={(evt: ChangeEvent<HTMLInputElement>) => {
										if (evt.currentTarget.checked) {
											setSelectedServices([...selectedServices, s]);
										} else {
											setSelectedServices(
												selectedServices.filter((service) => s.address !== service.address)
											);
										}
									}}
								/>
							</DataTableCell>
						</DataTableRow>
					))}
				</DataTableBody>
			</DataTableContent>
		</DataTable>
	);
};

export default ListofServices;
