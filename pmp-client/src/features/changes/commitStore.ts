import { ParameterChange, Revert, ServiceChanges } from './types';

import { Parameter } from '../parameters/types';
import { Service } from '../services/types';
import { areRevertsEqual } from './commitStoreHelpers';
import { createStore } from 'zustand';
import { persist } from 'zustand/middleware';

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
                    const oldServiceChange = get().serviceChanges.find((c) => c.service.address === service.address);

                    // If the new value is the same as the original value, remove the change
                    if (change.newValue === change.parameter.value) {
                        const oldParameterChange = oldServiceChange?.parameterChanges.find(
                            (p) => p.parameter.id === change.parameter.id
                        );
                        if (oldParameterChange) get().removeParameterChange(service, oldParameterChange);
                        return;
                    }

                    const newParameterChanges =
                        oldServiceChange?.parameterChanges.filter((p) => p.parameter.id !== change.parameter.id) ?? [];

                    const newServiceChange = {
                        service,
                        parameterChanges: [...newParameterChanges, change],
                        reverts: oldServiceChange?.reverts ?? []
                    };

                    set((s) => ({
                        serviceChanges: [...s.serviceChanges.filter((c) => c !== oldServiceChange), newServiceChange],
                        __past: [...s.__past, s.serviceChanges],
                        __future: []
                    }));
                },
                addRevert: (service, revert) => {
                    const s = get();
                    const oldServiceChange = s.serviceChanges.find((c) => c.service.address === service.address);
                    if (oldServiceChange?.reverts.some((r) => areRevertsEqual(r, revert))) return;

                    const newServiceChange = {
                        service,
                        parameterChanges: oldServiceChange?.parameterChanges ?? [],
                        reverts: [...(oldServiceChange?.reverts ?? []), revert]
                    };

                    set({
                        serviceChanges: [...s.serviceChanges.filter((c) => c !== oldServiceChange), newServiceChange],
                        __past: [...s.__past, s.serviceChanges],
                        __future: []
                    });
                },
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
                removeRevert: (service, revert) => {
                    set((s) => ({
                        serviceChanges: s.serviceChanges.map((c) => {
                            if (c.service.address !== service.address) return c;

                            return {
                                ...c,
                                reverts: c.reverts.filter((r) => !areRevertsEqual(r, revert))
                            };
                        }),
                        __past: [...s.__past, s.serviceChanges],
                        __future: []
                    }));
                },
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
