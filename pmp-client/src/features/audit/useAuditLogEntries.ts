import { Service } from '../services/types';
import axios from 'axios';
import { useQueries } from '@tanstack/react-query';
import useSelectedServices from '../services/useSelectedServices';
import { z } from 'zod';
import useEnvironment from '../environment/useEnvironment';
import { useMsalAuthentication } from '@azure/msal-react';
import { InteractionType } from '@azure/msal-browser';

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
            email: z.string(),
            message: z.string(),
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
    email: string;
    message: string;
    changes: AuditLogEntryChange[];
}

const useAuditLogEntries = (queryString: string) => {
    const [selectedServices] = useSelectedServices();
    const { environment } = useEnvironment();
    const { acquireToken } = useMsalAuthentication(InteractionType.Redirect);

    return useQueries({
        queries: selectedServices.map((service) => ({
            queryKey: ['auditLogEntries', service.address, queryString],
            queryFn: async () => {
                // TODO: Use real data
                // const data = axios.get(`${service.address}/pmp/log?query=${queryString}`);
                const token = await acquireToken();
                if (!token) throw new Error('No token');

                const response = await axios.get(`/mock/auditentries/${service.address}.json`, {
                    headers: {
                        'pmp-environment': environment,
                        Authorization: `Bearer ${token.accessToken}`
                    }
                });
                const parsed = await logParser.parseAsync(response.data);

                // Add service to data, as selectedServices is not properly synched with the combine function
                // Getting the service from selectedServices in combine would result in intermediary renders with undefined services
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
                    data: [],
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
                        email: commit.email,
                        message: commit.message,
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
