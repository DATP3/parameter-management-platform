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
import useEnvironment from '../features/environment/useEnvironment';
import useIsEnvironmentValid from '../features/environment/useIsEnvironmentValid';
import { useMsal } from '@azure/msal-react';
import { useState } from 'react';

const NavigationSection = () => {
    const { environment } = useEnvironment();

    const pages: { label: string, path: string}[] = [
        { label: 'parameters', path: 'parameters'},
        { label: 'commit history', path: 'audit'}];

    return (
        <TopAppBarSection alignStart>
            {pages.map((page) => (
                <Button key={page.label} theme={'onPrimary'} tag={Link} to={`/${environment}/${page.path}`} label={page.label} />
            ))}
        </TopAppBarSection>
    );
};

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
 *
 * TODO: Environment logic should be moved to a separate component.
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
                    {isValid && <NavigationSection />}
                    <AccountSection />
                </TopAppBarRow>
            </TopAppBar>
            <TopAppBarFixedAdjust />
        </>
    );
};

export default AppBar;
