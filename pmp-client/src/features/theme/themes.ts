import { ThemeOptions } from './types';

export const DEFAULT_THEME: ThemeOptions = {};

export const THEMES: Record<string, ThemeOptions> = {
    ['invalid']: {},
    ['test']: {
        primary: '#55828b',
        secondary: '#d9e5d6'
    },
    ['dev']: {},
    ['preprod']: {
        primary: '#f85e00',
        secondary: '#ffee88'
    },
    ['prod']: {
        primary: '#750d37',
        secondary: '#210124'
    }
};

/** Gets the theme object corresponding to the given environment */
export const getTheme = (environment?: string): ThemeOptions => {
    const env = environment?.toLowerCase();
    let pickTheme: string;
    if (env === undefined) {
        pickTheme = 'invalid';
    } else if (env.startsWith('test')) {
        pickTheme = 'test';
    } else if (env.startsWith('dev')) {
        pickTheme = 'dev';
    } else if (env.startsWith('preprod')) {
        pickTheme = 'preprod';
    } else if (env.startsWith('prod')) {
        pickTheme = 'prod';
    } else {
        pickTheme = 'invalid';
    }

    const theme = {
        ...DEFAULT_THEME,
        ...THEMES[pickTheme]
    };

    // Calculate unset values if possible
    // Currently not an exhaustive list of all possible pairs
    // TODO: Add inverses
    theme.primaryBg ??= theme.primary;
    theme.secondaryBg ??= theme.secondary;

    return theme;
};
