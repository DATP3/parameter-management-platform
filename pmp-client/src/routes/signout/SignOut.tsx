import { AuthenticatedTemplate, UnauthenticatedTemplate, useIsAuthenticated, useMsal } from '@azure/msal-react';

import { Button } from 'rmwc';
import { Link } from 'react-router-dom';
import { useEffect } from 'react';

/** Signout page */
const SignOut = () => {
    const isAuthenticated = useIsAuthenticated();
    const { instance } = useMsal();

    useEffect(() => {
        if (isAuthenticated) void instance.logoutRedirect();
    }, [instance, isAuthenticated]);

    return (
        <div>
            <UnauthenticatedTemplate>
                <p>Signed out</p>
                <Button raised tag={Link} to='/' label='Log in' />
            </UnauthenticatedTemplate>
            <AuthenticatedTemplate>
                <p>Signing out</p>
                <p>If you are not redirected, click the button below</p>
                <Button label='Sign out manually' onClick={() => void instance.loginRedirect()} />
            </AuthenticatedTemplate>
        </div>
    );
};

export default SignOut;
