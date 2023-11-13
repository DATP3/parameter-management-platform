import { DataTable, DataTableBody, DataTableContent, DataTableHead, DataTableHeadCell, DataTableRow } from "rmwc";
import ParameterListRow from "./ParameterListRow";
import { Parameter } from "../../features/parameters/types";
import { Service } from "../../features/services/types";
import { useState } from "react";
import { SortingCatagory } from "./types";
import { S } from "vitest/dist/reporters-5f784f42.js";
import { set } from "zod";

interface ParameterListProps {
	parameters: Parameter[];
	service: Service;
}

const ParameterList = (props: ParameterListProps) => {
	const { parameters, service } = props;
	const [sortingCatagory, setSortingCatagory] = useState("name");
	const [sortingDirection, setSortingDirection] = useState(1);

	const sort = (parameters: Parameter[], catagory: string) => {
		if (catagory === SortingCatagory.Name)
			return parameters.sort((a, b) => a.name.localeCompare(b.name) * sortingDirection);

		if (catagory === SortingCatagory.Type)
			return parameters.sort((a, b) => a.type.localeCompare(b.type) * sortingDirection);

		if (catagory === SortingCatagory.Value)
			return parameters.sort((a, b) => (a.value as string).localeCompare(b.value as string) * sortingDirection);

		return parameters;
	}

	const sortedParameters = sort(parameters, sortingCatagory);

	return (
		<DataTable className="parameterTable">
			<DataTableContent className="tableHead">
				<DataTableHead >
					<DataTableRow>
						<DataTableHeadCell
							className="headCell"
							sort={sortingCatagory === SortingCatagory.Name ? sortingDirection : 0}
							onSortChange={() => setSortingDirection(sortingDirection * -1)}
							onClick={() => setSortingCatagory(SortingCatagory.Name)}
						>
							Name
						</DataTableHeadCell>
						<DataTableHeadCell
							className="headCell"
							sort={sortingCatagory === SortingCatagory.Type ? sortingDirection : 0}
							onSortChange={() => setSortingDirection(sortingDirection * -1)}
							onClick={() => setSortingCatagory(SortingCatagory.Type)}
						>
							Type
						</DataTableHeadCell>
						<DataTableHeadCell
							className="headCell"
							sort={sortingCatagory === SortingCatagory.Value ? sortingDirection : 0}
							onSortChange={() => setSortingDirection(sortingDirection * -1)}
							onClick={() => setSortingCatagory(SortingCatagory.Value)}
						>
							Value
						</DataTableHeadCell>
					</DataTableRow>
				</DataTableHead>
				<DataTableBody>
					{sortedParameters.map((parameter) => (
						<ParameterListRow service={service} parameter={parameter} />
					))}
				</DataTableBody>
			</DataTableContent>
		</DataTable>

	);
};

export default ParameterList;