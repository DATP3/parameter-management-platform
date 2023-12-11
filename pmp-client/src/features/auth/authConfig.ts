import { AAD_CLIENT_ID, AAD_REDIRECT_URI_BASE } from '../../config';
import { Configuration, RedirectRequest } from '@azure/msal-browser';

/**
 * Configuration for MSAL authentication.
 */
export const authConfig: Configuration = {
    auth: {
        clientId: AAD_CLIENT_ID,
        redirectUri: AAD_REDIRECT_URI_BASE,
        postLogoutRedirectUri: `${AAD_REDIRECT_URI_BASE}/signout`,
        navigateToLoginRequestUrl: true
    },
    cache: {
        cacheLocation: 'localStorage'
    }
};

/**
 * Currently only reading user info. We just need AAD to authenticate the user.
 * TODO - Add scopes for API access when we have an API.
 */
export const scopes: string[] = ['User.Read'];

/**
 * Request config for getting user scopes
 */
export const redirectRequest: RedirectRequest = { scopes };
