import { Button, Menu, MenuItem, MenuSurfaceAnchor } from 'rmwc';
import { VALID_ENVIRONMENTS, toReadableEnvironment } from './environment';

import useEnvironment from './useEnvironment';
import useSetEnvironment_UNSAFE from './useSetEnvironment_UNSAFE';
import { useState } from 'react';

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

    const handleSelect = (e: RMWCMenuSelectEvent) => {
        const environment = VALID_ENVIRONMENTS[e.detail.index];
        setEnvironment(environment);
    };

    return (
        <>
            <MenuSurfaceAnchor>
                <Button
                    theme={['onSecondary', 'secondaryBg']}
                    unelevated
                    label={toReadableEnvironment(environment)}
                    onClick={() => setMenuOpen(true)}
                />
                <Menu
                    open={menuOpen}
                    onClose={() => setMenuOpen(false)}
                    anchorCorner='bottomStart'
                    onSelect={handleSelect}
                >
                    {VALID_ENVIRONMENTS.map((e) => (
                        <MenuItem key={e}>{toReadableEnvironment(e)}</MenuItem>
                    ))}
                </Menu>
            </MenuSurfaceAnchor>
        </>
    );
};

export default EnvironmentSelect;
