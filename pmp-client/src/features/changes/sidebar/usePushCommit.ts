import { useEffect, useState } from 'react';

import { CommitBody } from '../types';
import { Service } from '../../services/types';
import axios from 'axios';
import { scopes } from '../../auth/authConfig';
import useEnvironment from '../../environment/useEnvironment';
import { useMsal } from '@azure/msal-react';
import { useMutation } from '@tanstack/react-query';
import useServices from '../../services/useServices';

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

            console.log('Pushing', commit, 'to', service, 'with token', token);

            const res = await axios.post(service.address, commit, {
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

const usePushCommit = (commit: CommitBody) => {
    const [requestState, setRequestState] = useState<RequestState>('loading');
    const { data: services } = useServices();
    const { mutateAsync } = usePushCommitSingleService();

    useEffect(() => {
        const fun = async () => {
            if (!services) {
                return;
            }
            const promises = services?.map((service) => {
                return mutateAsync({ service, commit });
            });

            const results = await Promise.allSettled(promises);
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
