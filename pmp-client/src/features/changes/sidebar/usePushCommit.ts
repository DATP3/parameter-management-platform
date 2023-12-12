import { CommitBody, ParameterChange } from '../types';
import { useEffect, useState } from 'react';
import { useMutation, useQueryClient } from '@tanstack/react-query';

import { Service } from '../../services/types';
import axios from 'axios';
import { isParameterChange } from '../commitStoreHelpers';
import { scopes } from '../../auth/authConfig';
import useEnvironment from '../../environment/useEnvironment';
import { useMsal } from '@azure/msal-react';
import useServices from '../../services/useServices';

const adaptCommit = (commit: CommitBody) => {
    const flattenParameterChange = (c: ParameterChange) => {
        return {
            newValue: c.newValue,
            service: c.service,
            ...c.parameter
        };
    };

    const adaptedCommit = {
        ...commit,
        changes: commit.changes.map((c) => (isParameterChange(c) ? flattenParameterChange(c) : c))
    };

    return adaptedCommit;
};

interface SingleServiceMutationVariables {
    commit: CommitBody;
    service: Service;
}

const usePushCommitSingleService = () => {
    const { instance, accounts } = useMsal();
    const { environment } = useEnvironment();

    return useMutation({
        mutationFn: async ({ commit, service }: SingleServiceMutationVariables) => {
            const token = (await instance.acquireTokenSilent({ account: accounts[0], scopes })).accessToken;

            const adaptedData = adaptCommit(commit);

            const res = await axios.post(`${service.address}/pmp/commit`, adaptedData, {
                headers: {
                    Authorization: `Bearer ${token}`,
                    'pmp-environment': environment
                }
            });

            return res;
        }
    });
};

type RequestState = 'loading' | 'success' | 'error' | 'partial';

/**
 * Pushes a commit to all connected services.
 * @param commit The commit to push
 * @returns a derived request state for all requests. 'loading' as long as any request is not finished.
 */
const usePushCommit = (commit: CommitBody) => {
    const [requestState, setRequestState] = useState<RequestState>('loading');
    const { data: services } = useServices();
    const { mutateAsync } = usePushCommitSingleService();
    const queryClient = useQueryClient();

    useEffect(() => {
        const fun = async () => {
            if (!services) {
                return;
            }
            const promises = services?.map((service) => {
                return mutateAsync({ service, commit });
            });

            const results = await Promise.allSettled(promises);

            // Refetch any data from services which successfully recieved the changes
            results.forEach((res, i) => {
                if (res.status === 'fulfilled') {
                    void queryClient.invalidateQueries({
                        predicate: (query) =>
                            query.queryKey.includes(services[i].name) || query.queryKey.includes('auditLogEntries')
                    });
                }
            });

            if (results.every((res) => res.status === 'fulfilled')) {
                setRequestState('success');
            } else if (results.every((res) => res.status === 'rejected')) {
                setRequestState('error');
            } else {
                setRequestState('partial');
            }
        };
        void fun();
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, []);

    return {
        requestState
    };
};

export default usePushCommit;
