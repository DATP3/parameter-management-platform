import { clearChanges, getSegment } from './helpers';
import { expect, test } from '@playwright/test';

const environment = 'prod';
const service = 'service2';

const hash = /1a2b3c4d5e6f/;

test.describe('Audit page', () => {
    test('Add and view commit revert', async ({ page }) => {
        // Setup page with env and service
        await page.goto(`/${environment}/audit`);
        const seviceSegment = getSegment(page, 'service');
        await seviceSegment.getByText(service).click();

        // Ensaure no changes are present
        await clearChanges(page);

        // Open dialog
        const mainSegment = getSegment(page, 'main');
        await mainSegment.getByRole('cell', { name: hash }).click();
        // await mainSegment.getByText(hash).click();

        // Add revert
        await mainSegment.getByRole('button', { name: 'Revert Commit' }).click();

        // Close dialog
        await page.keyboard.press('Escape');

        // Verify revert has been added and information menu can be opened
        const changesSegment = getSegment(page, 'changes');
        await changesSegment.getByRole('button', { name: 'info' }).click();
        await expect(page.getByRole('menu')).toBeVisible();
        await expect(page.getByRole('menu').getByText(hash)).toBeVisible();
    });
});
