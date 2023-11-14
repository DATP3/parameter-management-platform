import { Button, Checkbox, CollapsibleList, SimpleListItem, SimpleMenuSurface } from "rmwc";
import { FilterData } from "./types";
import { ChangeEvent } from "react";

interface FilterProps {
	filters: FilterData[];
}

const Filter = ({ filters }: FilterProps) => {
	return (
		<SimpleMenuSurface handle={<Button outlined>Filters</Button>}>
			{filters.map((filter) =>
				<CollapsibleList
					key={filter.name}
					defaultOpen
					handle={
						<SimpleListItem
							text={filter.name}
							metaIcon="chevron_right"
						/>
					}
				>
					{filter.data.map((data) =>
						<Checkbox
							key={data.name}
							label={data.name}
							checked={filter.checkedCriteria(data)}
							onChange={(e: ChangeEvent<HTMLInputElement>) =>
								filter.onChange(data, e.currentTarget.checked)}
						/>
					)}
				</CollapsibleList>)}
		</SimpleMenuSurface>
	);
}

export default Filter;