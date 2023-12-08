import { Page } from '@playwright/test';
import axios from 'axios';

const routeToPublicMock = async (page: Page, from: string, to: string) => {
    await page.route(from, async (route) => {
        const base = 'http://localhost:4173';
        const response = await axios.get<unknown[]>(`${base}/mock${to}`);
        await route.fulfill({
            status: 200,
            body: JSON.stringify(response.data)
        });
    });
};

export const mockAllRoutes = async (page: Page) => {
    await mockEnvironmentRoute(page);
    await mockServicesRoute(page);
    await mockParametersRoute(page);
    await mockAuditLogRoute(page);
};

export const mockEnvironmentRoute = async (page: Page) => {
    await routeToPublicMock(page, '*/**/environment', '/environments.json');
};

export const mockServicesRoute = async (page: Page) => {
    await routeToPublicMock(page, '*/**/services', '/services/prod.json');
};

export const mockParametersRoute = async (page: Page) => {
    await routeToPublicMock(page, '*/**/pmp/parameters', '/parameters/2.2.2.2.json');
};

export const mockAuditLogRoute = async (page: Page) => {
    await routeToPublicMock(page, '*/**/pmp/log*', '/auditentries/2.2.2.2.json');
};
