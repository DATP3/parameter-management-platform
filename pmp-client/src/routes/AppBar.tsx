import {
    Button,
    Menu,
    MenuItem,
    MenuSurfaceAnchor,
    TopAppBar,
    TopAppBarFixedAdjust,
    TopAppBarRow,
    TopAppBarSection,
    TopAppBarTitle
} from 'rmwc';

import EnvironmentSelect from '../features/environment/EnvironmentSelect';
import { Link } from 'react-router-dom';
import useIsEnvironmentValid from '../features/environment/useIsEnvironmentValid';
import { useMsal } from '@azure/msal-react';
import { useState } from 'react';

const AccountSection = () => {
    const [open, setOpen] = useState(false);
    const { accounts } = useMsal();

    return (
        <>
            <TopAppBarSection alignEnd>
                <MenuSurfaceAnchor>
                    <Button
                        theme={'onPrimary'}
                        label={accounts[0].name ?? accounts[0].username}
                        trailingIcon='account_circle'
                        style={{ width: '100%' }}
                        onClick={() => setOpen(true)}
                    />
                    <Menu open={open} onClose={() => setOpen(false)} anchorCorner='bottomStart'>
                        <MenuItem tag={Link} to='/signout'>
                            Sign Out
                        </MenuItem>
                    </Menu>
                </MenuSurfaceAnchor>
            </TopAppBarSection>
        </>
    );
};

/**
 * The top app bar in the main application layout.
 *
 * Handles switching between environments.
 */
const AppBar = () => {
    const isValid = useIsEnvironmentValid();

    return (
        <>
            <TopAppBar fixed>
                <TopAppBarRow>
                    <TopAppBarSection alignStart>
                        {isValid ? <EnvironmentSelect /> : <TopAppBarTitle>No Environment</TopAppBarTitle>}
                    </TopAppBarSection>
                    <AccountSection />
                </TopAppBarRow>
            </TopAppBar>
            <TopAppBarFixedAdjust />
        </>
    );
};

export default AppBar;
