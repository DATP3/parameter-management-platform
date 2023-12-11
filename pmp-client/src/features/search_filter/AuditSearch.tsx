import Search from './Search';
import { useAuditFilter } from './useAuditFilter';

/** Searchbar for audit page. Updates context */
const AuditSearch = () => {
    const [filter, setFilter] = useAuditFilter();

    const handleSearch = (searchQuery: string) => {
        setFilter({ ...filter, searchQuery });
    };

    const value = filter.searchQuery ?? '';

    return <Search value={value} setQuery={handleSearch} hintText='Search commits' />;
};

export default AuditSearch;
