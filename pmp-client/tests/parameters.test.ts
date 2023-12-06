import { clearChanges, getInputByvalue, getSegment } from './helpers';
import { expect, test } from '@playwright/test';

import { mockAllRoutes } from './mockroutes';

const environment = 'prod';
const service = 'service2';

const initialParameterValue = 'admin@newexample.org';
const newParameterValue = 'test@playright.dev';

test.describe('Parameter page', () => {
    test.beforeEach(async ({ page }) => {
        await mockAllRoutes(page);
    });

    test('Add and view parameter change', async ({ page }) => {
        // Setup page with env and service
        await page.goto(`/${environment}/parameters`);
        const seviceSegment = getSegment(page, 'service');
        await seviceSegment.getByText(service).click();
        await clearChanges(page);

        const mainSegment = getSegment(page, 'main');
        await expect(getInputByvalue(mainSegment, initialParameterValue)).toBeVisible();

        await getInputByvalue(mainSegment, initialParameterValue).fill(newParameterValue);
        await expect(getInputByvalue(mainSegment, initialParameterValue)).toHaveCount(0);
        await expect(getInputByvalue(mainSegment, newParameterValue)).toBeVisible();

        const changesSegment = getSegment(page, 'changes');
        await expect(getInputByvalue(changesSegment, initialParameterValue)).toBeVisible();
        await expect(getInputByvalue(changesSegment, newParameterValue)).toBeVisible();
    });
});
