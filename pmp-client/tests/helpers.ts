import { Locator, Page } from '@playwright/test';

export const clearChanges = async (page: Page) => {
    const changesSegment = getSegment(page, 'changes');
    const deleteAllButton = changesSegment.getByText('Delete all');

    if ((await deleteAllButton.getAttribute('disabled')) == null) {
        return;
    }

    await deleteAllButton.click();
};

export const getSegment = (page: Page, segment: 'service' | 'changes' | 'main') => {
    return page.getByTestId(`${segment}-segment`);
};

export const getInputByvalue = (locator: Page | Locator, value: string) => {
    return locator.locator(`input[value="${value}"]`);
};

export const getBaseUrl = (page: Page) => {
    return page.url().split('/').slice(0, 3).join('/');
};
