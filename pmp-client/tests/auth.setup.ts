import { expect, test as setup } from '@playwright/test';

import { loadEnv } from 'vite';

const env = loadEnv('', process.cwd(), 'TEST');

const authFile = '.auth/user.json';
// Get email from env or vite env
const email = process.env.TEST_USER_EMAIL ?? env.TEST_USER_EMAIL;
const password = process.env.TEST_USER_PASSWORD ?? env.TEST_USER_PASSWORD;

setup('authenticate', async ({ page }) => {
    if (!email || !password) {
        throw new Error('TEST_USER_EMAIL or TEST_USER_PASSWORD not set');
    }

    // trigger auth redirect
    await page.goto('/');
    await page.waitForURL(/login\.microsoftonline\.com/);

    // Fill email
    await page.waitForSelector('input[name="loginfmt"]');
    await page.getByPlaceholder(/Email/).fill(email);
    await page.getByRole('button', { name: 'Next' }).click();

    // Fill password
    await page.waitForSelector('input[name="passwd"]');
    await page.getByPlaceholder(/Password/).fill(password);
    await page.getByRole('button', { name: 'Sign in' }).click();

    // Do not save credentials at msft
    await page.getByRole('button', { name: 'No' }).click();

    expect(await page.title()).toBe('Parameter Management Platform');

    // Wait for credentials to be written to localstorage. Msal does this asynchronously.
    await page.waitForTimeout(2000);

    // Save credentials to file for other tests
    await page.context().storageState({ path: authFile });
});
