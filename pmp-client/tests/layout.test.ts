import { expect, test } from '@playwright/test';

import { mockEnvironmentRoute } from './mockroutes';

test('displays current environment in header', async ({ page }) => {
    // Mock env api
    await mockEnvironmentRoute(page);

    const environment = 'prod';
    await page.goto(`/${environment}/parameters`);

    await page.waitForURL(/\/prod\/parameters/);

    // Find env in header
    await expect(page.locator('header').getByRole('button').getByText(environment)).toBeVisible();
});
