import Search from './Search';
import { useAuditFilter } from './useAuditFilter';

const AuditSearch = () => {
    const [filter, setFilter] = useAuditFilter();

    const handleSearch = (searchQuery: string) => {
        setFilter({ ...filter, searchQuery });
    };

    const value = filter.searchQuery ?? '';

    return <Search value={value} setQuery={handleSearch} />;
};

export default AuditSearch;
