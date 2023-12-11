import Filter from './Filter';
import { Namable } from './types';
import { ParameterType } from '../parameters/types';
import { useEffect } from 'react';
import { useParameterFilter } from './useParamererFilter';

/** Filter for parameter page */
const ParameterFilter = () => {
    const [filter, setFilter] = useParameterFilter();

    useEffect(() => {
        setAllTypes(true);
        // Disable to only run on mount
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, []);

    const handleTypeChecked = (data: Namable, isChecked: boolean) => {
        const { name: type } = data as { name: ParameterType };
        const currentTypes = filter.types ?? [];

        if (isChecked) {
            setFilter({ ...filter, types: [...currentTypes, type] });
        } else {
            setFilter({ ...filter, types: currentTypes.filter((t) => t !== type) });
        }
    };

    const typeCheckedCriteria = (data: Namable) => {
        const { name: type } = data as { name: ParameterType };
        const currentTypes = filter.types ?? [];

        return currentTypes.find((t) => t === type) !== undefined;
    };

    const setAllTypes = (isSelected: boolean) => {
        if (isSelected) {
            setFilter({ ...filter, types: Object.values(ParameterType) });
        } else {
            setFilter({ ...filter, types: [] });
        }
    };

    return (
        <Filter
            filters={[
                {
                    name: 'Parameter Types',
                    data: Object.values(ParameterType).map((type) => ({ name: type })),
                    setAll: setAllTypes,
                    onChange: handleTypeChecked,
                    checkedCriteria: typeCheckedCriteria
                }
            ]}
        />
    );
};

export default ParameterFilter;
