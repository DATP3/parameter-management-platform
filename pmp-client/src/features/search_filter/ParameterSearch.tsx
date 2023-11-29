import Search from './Search';
import { useParameterFilter } from './useParamererFilter';

const ParameterSearch = () => {
    const [filter, setFilter] = useParameterFilter();

    const handleSearch = (searchQuery: string) => {
        setFilter({ ...filter, searchQuery });
    };

    const value = filter.searchQuery ?? '';

    return <Search value={value} setQuery={handleSearch} hintText='Search parameters' />;
};

export default ParameterSearch;
