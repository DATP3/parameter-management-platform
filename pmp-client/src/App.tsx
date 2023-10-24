import 'rmwc/dist/styles';

import AuthProvider from './features/auth/AuthProvider';
import { QueryProvider } from './features/network/query';
import RMWCComnponentProvider from './features/theme/RMWCComponentProvider';
import RMWCThemeProvider from './features/theme/RMWCThemeProvider';
import RootRouter from './routes/RootRouter';

/**
 * The root component.
 * Sets up the providers for the application.
 */
function App() {
    return (
        <>
            <AuthProvider>
                <QueryProvider>
                    <RMWCThemeProvider>
                        <RMWCComnponentProvider>
                            <RootRouter />
                        </RMWCComnponentProvider>
                    </RMWCThemeProvider>
                </QueryProvider>
            </AuthProvider>
        </>
    );
}

export default App;
