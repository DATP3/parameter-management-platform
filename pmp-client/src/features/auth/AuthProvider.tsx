import { IPublicClientApplication, PublicClientApplication } from '@azure/msal-browser';

import { MsalProvider } from '@azure/msal-react';
import { ReactNode } from 'react';
import { authConfig } from './authConfig';

const publicClientApplication: IPublicClientApplication = new PublicClientApplication(authConfig);

/** Wrapper provider for MSAL auth */
const AuthProvider = ({ children }: { children: ReactNode }) => {
    return <MsalProvider instance={publicClientApplication}>{children}</MsalProvider>;
};

export default AuthProvider;
