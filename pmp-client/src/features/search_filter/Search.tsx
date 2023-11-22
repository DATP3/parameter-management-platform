import { TextField } from 'rmwc';

interface SearchProps {
    value: string;
    setQuery: (query: string) => void;
}

const Search = ({ value, setQuery }: SearchProps) => {
    return (
        <TextField
            outlined
            className='search w-full'
            type='text'
            label='Search'
            value={value}
            onChange={(e: React.ChangeEvent<HTMLInputElement>) => {
                setQuery(e.target.value);
            }}
        />
    );
};

export default Search;
