import 'rmwc/dist/styles';

import AuthProvider from './features/auth/AuthProvider';
import { QueryProvider } from './features/network/query';
import RMWCComnponentProvider from './features/theme/RMWCComponentProvider';
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
                    <RMWCComnponentProvider>
                        <RootRouter />
                    </RMWCComnponentProvider>
                </QueryProvider>
            </AuthProvider>
        </>
    );
}

export default App;
