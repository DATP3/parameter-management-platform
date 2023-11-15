import { Button, CircularProgress, Menu, MenuItem, MenuSurfaceAnchor, Typography } from 'rmwc';

import useEnvironment from './useEnvironment';
import useSetEnvironment_UNSAFE from './useSetEnvironment_UNSAFE';
import { useState } from 'react';
import useEnvironmentQuery from './useEnvironmentQuery';

/* Not exhaustive. Generated from observed runtime behavior. */
interface RMWCMenuSelectEvent {
    detail: {
        index: number;
    };
}

const EnvironmentSelect = () => {
    const [menuOpen, setMenuOpen] = useState(false);
    const { environment } = useEnvironment();
    const setEnvironment = useSetEnvironment_UNSAFE();
    const { data: environments, isPending, error } = useEnvironmentQuery();

    if (isPending)
        return (
            <>
                <Typography use='headline6'>Waiting on environments</Typography> <CircularProgress />
            </>
        );

    if (error) return <Typography use='headline6'>Error getting environments</Typography>;

    const handleSelect = (e: RMWCMenuSelectEvent) => {
        const environment = environments[e.detail.index];
        setEnvironment(environment);
    };

    return (
        <>
            <MenuSurfaceAnchor>
                <Button
                    theme={['onSecondary', 'secondaryBg']}
                    unelevated
                    label={environment}
                    onClick={() => setMenuOpen(true)}
                />
                <Menu
                    open={menuOpen}
                    onClose={() => setMenuOpen(false)}
                    anchorCorner='bottomStart'
                    onSelect={handleSelect}
                >
                    {environments.map((e) => (
                        <MenuItem key={e.environment}>{e.environment}</MenuItem>
                    ))}
                </Menu>
            </MenuSurfaceAnchor>
        </>
    );
};

export default EnvironmentSelect;
