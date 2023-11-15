import { Button, Checkbox, CollapsibleList, SimpleListItem, SimpleMenuSurface } from "rmwc";
import { FilterData } from "./types";
import { ChangeEvent } from "react";
import FilterElement from "./FilterElement";

interface FilterProps {
	filters: FilterData[];
}

const Filter = ({ filters }: FilterProps) => {
	return (
		<SimpleMenuSurface handle={<Button className={"mt-4"} outlined>Filters</Button>}>
			{filters.map((filter) =>
				<FilterElement key={filter.name} filter={filter}/>)}
		</SimpleMenuSurface>
	);
}

export default Filter;