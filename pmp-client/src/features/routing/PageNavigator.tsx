import { Link, useLocation } from 'react-router-dom';

import { Button } from 'rmwc';
import useEnvironment from '../environment/useEnvironment';

/** Renders a list of navigational buttons */
const PageNavigator = () => {
    const { environment } = useEnvironment();
    const { pathname } = useLocation();

    const pages: { label: string; path: string }[] = [
        { label: 'show parameters', path: 'parameters' },
        { label: 'show commit history', path: 'audit' }
    ];

    return (
        <div className=''>
            {pages.map((page) => (
                <Button
                    className='ml-5'
                    outlined
                    key={page.label}
                    tag={Link}
                    to={`/${environment}/${page.path}`}
                    label={page.label}
                    unelevated={pathname.includes(page.path)}
                />
            ))}
        </div>
    );
};

export default PageNavigator;
