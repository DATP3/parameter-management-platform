import { DataTable, DataTableBody, DataTableContent, DataTableHead, DataTableHeadCell, DataTableRow } from 'rmwc';
import ParameterListRow from './ParameterListRow';
import { Parameter, SortingCatagory } from '../../features/parameters/types';
import { Service } from '../../features/services/types';
import { useState } from 'react';
import ThemeMarkerWrapper from '../../features/components/ThemeMarkerWrapper';

interface ParameterListProps {
	parameters: Parameter[];
	service: Service;
}

const ParameterList = (props: ParameterListProps) => {
	const { parameters, service } = props;
	const [sortingCatagory, setSortingCatagory] = useState<SortingCatagory>(SortingCatagory.NAME);
	const [sortingDirection, setSortingDirection] = useState(1);

	const sort = (parameters: Parameter[], catagory: SortingCatagory, sortingDirection: number) => {
		if (catagory === SortingCatagory.NAME)
			return parameters.sort((a, b) => a.name.localeCompare(b.name) * sortingDirection);

		if (catagory === SortingCatagory.TYPE)
			return parameters.sort((a, b) => a.type.localeCompare(b.type) * sortingDirection);

		if (catagory === SortingCatagory.VALUE)
			return parameters.sort((a, b) => (a.value as string).localeCompare(b.value as string) * sortingDirection);

		return parameters;
	};

	const sortedParameters = sort(parameters, sortingCatagory, sortingDirection);

	return (
		<ThemeMarkerWrapper>
			<DataTable className='parameterTable'>
				<DataTableContent>
					<DataTableHead className='tableHead'>
						<DataTableRow>
							<DataTableHeadCell
								className='headCell'
								sort={sortingCatagory === SortingCatagory.NAME ? sortingDirection : 0}
								onSortChange={() => setSortingDirection(sortingDirection * -1)}
								onClick={() => setSortingCatagory(SortingCatagory.NAME)}
							>
								Name
							</DataTableHeadCell>
							<DataTableHeadCell
								className='headCell'
								sort={sortingCatagory === SortingCatagory.TYPE ? sortingDirection : 0}
								onSortChange={() => setSortingDirection(sortingDirection * -1)}
								onClick={() => setSortingCatagory(SortingCatagory.TYPE)}
							>
								Type
							</DataTableHeadCell>
							<DataTableHeadCell
								className='headCell'
								sort={sortingCatagory === SortingCatagory.VALUE ? sortingDirection : 0}
								onSortChange={() => setSortingDirection(sortingDirection * -1)}
								onClick={() => setSortingCatagory(SortingCatagory.VALUE)}
							>
								Value
							</DataTableHeadCell>
							<DataTableHeadCell
								className='headCell'
								onSortChange={() => setSortingDirection(sortingDirection * -1)}
								onClick={() => setSortingCatagory(SortingCatagory.VALUE)}
							>
							</DataTableHeadCell>
						</DataTableRow>
					</DataTableHead>
					<DataTableBody>
						{sortedParameters.map((parameter) =>
							<ParameterListRow key={parameter.id} service={service} parameter={parameter} />
						)}
					</DataTableBody>
				</DataTableContent>
			</DataTable>
		</ThemeMarkerWrapper >

	);
};

export default ParameterList;
