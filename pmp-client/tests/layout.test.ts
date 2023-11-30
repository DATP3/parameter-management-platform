import { expect, test } from '@playwright/test';

test('displays current environment in header', async ({ page }) => {
    const environment = 'prod';
    await page.goto(`/${environment}/parameters`);

    await page.waitForURL(/\/prod\/parameters/);

    await expect(page.locator('header').getByRole('button').getByText(environment)).toBeVisible();
});
