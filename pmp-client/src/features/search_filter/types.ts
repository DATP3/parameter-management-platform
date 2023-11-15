import { ParameterType } from '../parameters/types';

export interface FilterData<T extends Namable = Namable> {
	name: string;
    data: T[];
    setAll?: (isSelected: boolean) => void;
    checkedCriteria: (data: T) => boolean;
	onChange: (data: T, isChecked: boolean) => void;
}

export interface Namable {
    name: string;
}

export interface ParameterFilter {
    types?: ParameterType[];
    searchQuery?: string;
}

export type ParameterFilterContextValue = [ParameterFilter, React.Dispatch<React.SetStateAction<ParameterFilter>>];
