import { Outlet, RouterProvider, createBrowserRouter } from 'react-router-dom';

import AuditPage from './audit/AuditPage';
import { CommitStoreProvider } from '../features/changes/commitStoreProvider';
import ErrorPage from './ErrorPage';
import { InteractionType } from '@azure/msal-browser';
import Layout from './Layout';
import { MsalAuthenticationTemplate } from '@azure/msal-react';
import ParametersPage from './parameters/ParametersPage';
import RMWCThemeProvider from '../features/theme/RMWCThemeProvider';
import Redirecter from '../features/routing/Redirecter';
import { SelectedServiceProvider } from '../features/services/SelectedServiceProvider';
import SignOut from './signout/SignOut';
import { redirectRequest } from '../features/auth/authConfig';

/**
 * The root router for the application.
 *
 * Currently handles all routing. Adding more routers will likely not be necessary.
 */
const router = createBrowserRouter([
    {
        path: '/',
        errorElement: <ErrorPage />,
        children: [
            {
                path: '/signout',
                element: <SignOut />
            },
            {
                /** All paths from here are protected by AAD authentication */
                path: '/',
                element: (
                    <>
                        <MsalAuthenticationTemplate
                            interactionType={InteractionType.Redirect}
                            authenticationRequest={redirectRequest}
                        >
                            <SelectedServiceProvider>
                                <CommitStoreProvider>
                                    <RMWCThemeProvider>
                                        <Outlet />
                                    </RMWCThemeProvider>
                                </CommitStoreProvider>
                            </SelectedServiceProvider>
                        </MsalAuthenticationTemplate>
                    </>
                ),
                children: [
                    {
                        path: '/',
                        /**
                         * Not using loader to redirect here.
                         * It was causing a redirect loop with microsoft when the user was not authenticated
                         * Likely because the loader was being called before the user was fully authenticated
                         */
                        element: <Redirecter to={`/invalid/parameters`} />
                    },
                    {
                        path: '/:environment/',
                        element: <Layout />,
                        loader: ({ request, params }) => {
                            if (!params.environment) {
                                // This should never happen. Can only occour if route is misconfigured
                                throw new Error('Matched environment route with no environment param');
                            }

                            // None of the below routes will match if environment is not followed by another path segment,
                            if (/^(https?):\/\/[^/]+\/[^/]+\/?$/.test(request.url)) {
                                throw new Response('', {
                                    status: 404,
                                    statusText: 'Not Found'
                                });
                            }

                            return null;
                        },
                        children: [
                            {
                                path: 'parameters',
                                element: <ParametersPage />
                            },
                            {
                                path: 'audit',
                                element: <AuditPage />
                            }
                        ]
                    }
                ]
            }
        ]
    }
]);

const RootRouter = () => <RouterProvider router={router} />;

export default RootRouter;
