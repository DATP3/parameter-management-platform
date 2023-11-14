
export interface FilterData<T extends Namable = Namable> {
	name: string;
	data: T[];
	checkedCriteria: (data: T) => boolean;
	onChange: (data: T, isChecked: boolean) => void;
}

export interface FilterOption {
	name: string;
	checked: boolean;
}

export interface Namable {
	name: string;
}