import { ParameterChange, Revert, ServiceChanges } from './types';

import { Service } from '../services/types';
import { createStore } from 'zustand';
import { persist } from 'zustand/middleware';
import { Parameter } from '../parameters/types';

/**
 * Internal state of the commit store
 */
interface State {
    serviceChanges: ServiceChanges[];
    __past: ServiceChanges[][];
    __future: ServiceChanges[][];
}

/**
 * Allowed actions on the commit store
 */
interface Actions {
    addParameterChange: (service: Service, change: ParameterChange) => void;
    addRevert: (service: Service, revert: Revert) => void;
    removeParameterChange: (service: Service, change: ParameterChange) => void;
    removeRevert: (service: Service, revert: Revert) => void;
    findParameterChange: (service: Service, parameter: Parameter) => ParameterChange | undefined;
    undo: () => void;
    redo: () => void;
    clear: () => void;
}

/**
 * The type of the store returned by createCommitStore.
 */
export type CommitStore = ReturnType<typeof createCommitStore>;

/**
 * The type of the state of the store returned by createCommitStore.
 */
export type CommitStoreState = State & Actions;

const initialState: State = {
    serviceChanges: [],
    __past: [],
    __future: []
};

/**
 * Creates a new commit store.
 *
 * @param storageKey Key used for local storage
 */
// Mutators in this store are messy but work. This is an issue, but not a priority.
export const createCommitStore = (storageKey: string) => {
    return createStore<CommitStoreState>()(
        persist(
            (set, get) => ({
                ...initialState,
                findParameterChange: (service, parameter) => {
                    const serviceParameterChanges = get().serviceChanges.find(
                        (sc) => sc.service.address === service.address
                    )?.parameterChanges;
                    return serviceParameterChanges?.find((pc) => pc.parameter.id === parameter.id);
                },
                addParameterChange: (service, change) => {
                    // Assume service is unique on address
                    console.log(change);

                    const oldServiceChange = get().serviceChanges.find((c) => c.service.address === service.address);
                    let filteredParameterChanges = oldServiceChange?.parameterChanges.filter(
                        (p) => p.parameter.id !== change.parameter.id
                    );
                    filteredParameterChanges ??= [];

                    const newServiceChange = {
                        service,
                        parameterChanges: [...filteredParameterChanges, change],
                        reverts: oldServiceChange?.reverts ?? []
                    };

                    set((s) => ({
                        serviceChanges: [...s.serviceChanges.filter((c) => c !== oldServiceChange), newServiceChange],
                        __past: [...s.__past, s.serviceChanges],
                        __future: []
                    }));

                    if (change.newValue == change.parameter.value) {
                        get().removeParameterChange(service, change);
                    }
                },
                // TODO: Implement
                addRevert: (_service, _revert) => {},
                removeParameterChange: (service, change) => {
                    // If someone has a prettier way of doing this, please tell me
                    const s = get();
                    const serviceChange = s.serviceChanges.find((c) => c.service.address === service.address);
                    if (!serviceChange) return;

                    const newParameterChanges = serviceChange?.parameterChanges.filter((c) => c !== change);
                    if (newParameterChanges.length === serviceChange?.parameterChanges.length) return;

                    const newServiceChange = {
                        ...serviceChange,
                        parameterChanges: newParameterChanges
                    };

                    const newServiceChanges = s.serviceChanges.filter((c) => c !== serviceChange);
                    if (newServiceChange.parameterChanges.length > 0) {
                        newServiceChanges.push(newServiceChange);
                    }

                    set((s) => ({
                        serviceChanges: newServiceChanges,
                        __past: [...s.__past, s.serviceChanges],
                        __future: []
                    }));
                },
                // TODO: Implement
                removeRevert: (_service, _revert) => {},
                undo: () => {
                    if (get().__past.length === 0) return;

                    set((s) => ({
                        serviceChanges: s.__past[s.__past.length - 1],
                        __past: s.__past.slice(0, s.__past.length - 1),
                        __future: [s.serviceChanges, ...s.__future]
                    }));
                },
                redo: () => {
                    if (get().__future.length === 0) return;

                    set((s) => ({
                        serviceChanges: s.__future[0],
                        __past: [...s.__past, s.serviceChanges],
                        __future: s.__future.slice(1)
                    }));
                },
                clear: () => set(initialState)
            }),
            {
                name: storageKey
            }
        )
    );
};
