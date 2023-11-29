import { TextField } from 'rmwc';

interface SearchProps {
    value: string;
    hintText?: string;
    setQuery: (query: string) => void;
}

const Search = ({ value, setQuery, hintText }: SearchProps) => {
    return (
        <TextField
            outlined
            className='search w-full'
            type='text'
            label={hintText ?? 'Search'}
            value={value}
            onChange={(e: React.ChangeEvent<HTMLInputElement>) => {
                setQuery(e.target.value);
            }}
        />
    );
};

export default Search;
