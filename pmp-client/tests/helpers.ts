import { Locator, Page } from '@playwright/test';

/** Empties the change list */
export const clearChanges = async (page: Page) => {
    const changesSegment = getSegment(page, 'changes');
    const deleteAllButton = changesSegment.getByText('Delete all');

    if ((await deleteAllButton.getAttribute('disabled')) == null) {
        return;
    }

    await deleteAllButton.click();
};

/** get one of 3 major page segments. These are targeted with data-test-id */
export const getSegment = (page: Page, segment: 'service' | 'changes' | 'main') => {
    return page.getByTestId(`${segment}-segment`);
};

/** Gets an input based on its current value. May require previous configuration of the input field */
export const getInputByvalue = (locator: Page | Locator, value: string) => {
    return locator.locator(`input[value="${value}"]`);
};
