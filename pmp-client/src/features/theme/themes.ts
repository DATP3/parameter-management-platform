import { Environment } from '../environment/environment';
import { ThemeOptions } from './types';

export const DEFAULT_THEME: ThemeOptions = {};

export const THEMES: Record<Environment, ThemeOptions> = {
    [Environment.INVALID]: {},
    [Environment.TEST]: {
        primary: '#d9e5d6',
        secondary: '#55828b'
    },
    [Environment.DEV]: {},
    [Environment.PREPROD]: {
        primary: '#f85e00',
        secondary: '#ffee88'
    },
    [Environment.PROD]: {
        primary: '#750d37',
        secondary: '#210124'
    }
};

export const getTheme = (environment: Environment): ThemeOptions => {
    const theme = {
        ...DEFAULT_THEME,
        ...THEMES[environment]
    };

    // Calculate unset values if possible
    // Currently not an exhaustive list of all possible pairs
    // TODO: Add inverses
    theme.primaryBg ??= theme.primary;
    theme.secondaryBg ??= theme.secondary;

    return theme;
};
