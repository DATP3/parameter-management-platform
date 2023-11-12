import { ThemeProvider } from 'rmwc';
import { getTheme } from './themes';
import useEnvironment from '../environment/useEnvironment';

/**
 * Adds theming to the app, depending on the current environment
 *
 * TODO: Add themeing
 */
const RMWCThemeProvider = ({ children }: { children: React.ReactNode }) => {
    const { environment } = useEnvironment();
    const theme = getTheme(environment);
    return <ThemeProvider options={theme}>{children}</ThemeProvider>;
};

export default RMWCThemeProvider;
