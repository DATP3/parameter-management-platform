import { Checkbox, CollapsibleList, SimpleListItem } from 'rmwc';
import { FilterData } from './types';
import { ChangeEvent } from 'react';

interface FilterElementProps {
    filter: FilterData;
}

const FilterElement = ({ filter }: FilterElementProps) => {
    return (
        <CollapsibleList
            key={filter.name}
            open={true}
            handle={
                <SimpleListItem
                    text={
                        <>
                            {filter.name}
                            {filter.setAll && (
                                <Checkbox
                                    defaultChecked
                                    onChange={(e: ChangeEvent<HTMLInputElement>) => {
                                        filter.setAll && filter.setAll(e.currentTarget.checked);
                                    }}
                                />
                            )}
                        </>
                    }
                />
            }
        >
            {filter.data.map((data) => (
                <Checkbox
                    key={data.name}
                    label={data.name}
                    checked={filter.checkedCriteria(data)}
                    onChange={(e: ChangeEvent<HTMLInputElement>) => filter.onChange(data, e.currentTarget.checked)}
                />
            ))}
        </CollapsibleList>
    );
};

export default FilterElement;
