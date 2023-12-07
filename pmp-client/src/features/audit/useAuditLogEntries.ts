import { Service } from '../services/types';
import axios from 'axios';
import useEnvironment from '../environment/useEnvironment';
import { useMsal } from '@azure/msal-react';
import { useQueries } from '@tanstack/react-query';
import useServices from '../services/useServices';
import { z } from 'zod';

const paramerterChangeParser = z.object({
    name: z.string(),
    oldValue: z.string(),
    newValue: z.string()
});

const revertParser = z.object({
    referenceHash: z.string(),
    revertType: z.string(), // TODO: Enum
    parameterChanges: paramerterChangeParser.array()
});

const logParser = z.object({
    commits: z.array(
        z.object({
            hash: z.string(),
            pushDate: z.coerce.date(),
            user: z.string(),
            message: z.string(),
            affectedServices: z.array(z.string()),
            changes: z.object({
                reverts: revertParser.array(),
                parameterChanges: paramerterChangeParser.array()
            })
        })
    )
});

export type AuditLogEntryParameterChange = z.infer<typeof paramerterChangeParser>;
export type AuditLogEntryRevert = z.infer<typeof revertParser>;

export interface AuditLogEntryChange {
    service: Service;
    reverts: AuditLogEntryRevert[];
    parameterChanges: AuditLogEntryParameterChange[];
}

export interface AuditLogEntry {
    hash: string;
    pushDate: Date;
    user: string;
    message: string;
    affectedServices: string[];
    changes: AuditLogEntryChange[];
}

const useAuditLogEntries = (queryString: string) => {
    const { data: services = [] } = useServices();
    const { environment } = useEnvironment();
    const { accounts } = useMsal();

    return useQueries({
        queries: services.map((service) => ({
            queryKey: ['auditLogEntries', service.address, queryString],
            queryFn: async () => {
                const token = accounts[0].idToken;
                if (!token) throw new Error('No token');

                // TODO: Use real data. Test is set up to intercept
                // const response = await axios.get(`${service.address}/pmp/log?query=${queryString}`, {
                const response = await axios.get(`http://${service.address}/pmp/log`, {
                    headers: {
                        'pmp-environment': environment,
                        Authorization: `Bearer ${token}`
                    }
                });
                const parsed = await logParser.parseAsync(response.data);

                // Add service to data, as services is not properly synched with the combine function
                // Getting the service from services in combine would result in intermediary renders with undefined services
                // This fix is not ideal, but it works
                return {
                    data: parsed,
                    service
                };
            },
            meta: {
                serviceAddress: service.address
            }
        })),
        combine: (results) => {
            // Wait for all queries to finish
            const isPending = results.filter((result) => result.isPending).length;
            const errors = results.filter((result) => result.error).map((result) => result.error);
            const isError = errors.length;
            if (isPending || isError)
                return {
                    data: undefined,
                    isPending,
                    isError,
                    errors: errors
                };

            // Zip the results together. Uses map as intermediary data structure to lower complexity
            const combinedLogs = results.reduce((map, current) => {
                const service = current.data!.service;
                const data = current.data?.data;
                data!.commits.forEach((commit) => {
                    // Ensure commit is in map
                    const c: AuditLogEntry = map.get(commit.hash) ?? {
                        hash: commit.hash,
                        pushDate: commit.pushDate,
                        user: commit.user,
                        message: commit.message,
                        affectedServices: commit.affectedServices,
                        changes: []
                    };

                    c.changes.push({
                        service,
                        reverts: commit.changes.reverts,
                        parameterChanges: commit.changes.parameterChanges
                    });

                    map.set(commit.hash, c);
                });
                return map;
            }, new Map<string, AuditLogEntry>());

            return {
                data: Array.from(combinedLogs.values()),
                isPending: false,
                isError: undefined
            };
        }
    });
};

export default useAuditLogEntries;
