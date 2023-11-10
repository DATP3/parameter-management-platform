export type ThemeKey =
    | 'primary'
    | 'secondary'
    | 'error'
    | 'background'
    | 'surface'
    | 'primaryBg'
    | 'secondaryBg'
    | 'textPrimaryOnBackground'
    | 'textSecondaryOnBackground'
    | 'textHintOnBackground'
    | 'textDisabledOnBackground'
    | 'textIconOnBackground'
    | 'textPrimaryOnLight'
    | 'textSecondaryOnLight'
    | 'textHintOnLight'
    | 'textDisabledOnLight'
    | 'textIconOnLight'
    | 'onPrimary'
    | 'onSecondary'
    | 'onError'
    | 'textPrimaryOnDark'
    | 'textSecondaryOnDark'
    | 'textHintOnDark'
    | 'textDisabledOnDark'
    | 'textIconOnDark';

export type ThemeOptions = {
    [key in ThemeKey]?: string;
};
