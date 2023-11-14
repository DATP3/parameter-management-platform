import { Button, Checkbox, CollapsibleList, MenuItem, Radio, SimpleListItem, SimpleMenu, SimpleMenuSurface, TextField } from "rmwc";
import { ParameterType } from "../parameters/types";

const Search = () => {
	return (
		<>
			<TextField
				outlined
				className="search w-full"
				type="text"
				label="Search"
			/>
			<SimpleMenuSurface handle={<Button outlined>Filters</Button>}>
				<CollapsibleList
					handle={
						<SimpleListItem
							text="Types"
							metaIcon="chevron_right"
						/>
					}
				>
					<Checkbox label="Name" />
					<Checkbox label="Name" />
					<Checkbox label="Name" />
					<Checkbox label="Name" />
					<Checkbox label="Name" />
					<Checkbox label="Name" />
					<Checkbox label="Name" />
					<Checkbox label="Name" />
					<Checkbox label="Name" />
					<Checkbox label="Name" />
				</CollapsibleList>
				<CollapsibleList
					handle={
						<SimpleListItem
							text="Pizza"
							graphic="local_pizza"
							metaIcon="chevron_right"
						/>
					}
				>
					<Checkbox label="Pizza" />
				</CollapsibleList>
			</SimpleMenuSurface>
		</>
	);
}

export default Search;