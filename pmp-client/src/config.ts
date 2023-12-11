/** Redirect url base for AAD authentication. Used for both signin and singout. @default window.location.origin */
export const AAD_REDIRECT_URI_BASE: string =
    (import.meta.env.VITE_AAD_REDIRECT_URI_BASE as string) ?? window.location.origin;

/** AAD client id from azure. Default is from ARE's private account. @default 'f74350bc-51a5-4955-8fac-859d8cca13df' */
export const AAD_CLIENT_ID: string =
    (import.meta.env.VITE_AAD_CLIENT_ID as string) ?? 'f74350bc-51a5-4955-8fac-859d8cca13df';

export const TRACKER_URL: string =
    (import.meta.env.VITE_TRACKER_URL as string) ?? 'http://localhost:8080/pmp-tracker/rest';
