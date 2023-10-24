import { Configuration, RedirectRequest } from '@azure/msal-browser';

/**
 * Configuration for MSAL authentication.
 * TODO - Move to environment variables.
 */
export const authConfig: Configuration = {
    auth: {
        clientId: 'f74350bc-51a5-4955-8fac-859d8cca13df',
        redirectUri: 'http://localhost:5173/',
        postLogoutRedirectUri: 'http://localhost:5173/signout',
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
const scopes: string[] = ['User.Read'];

/**
 * Request config for getting user scopes
 */
export const redirectRequest: RedirectRequest = { scopes };
