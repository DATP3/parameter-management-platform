import { Change, ParameterChange } from './types';
import { compareChanges, isParameterChange } from './commitStoreHelpers';

import { Parameter } from '../parameters/types';
import { Service } from '../services/types';
import { createStore } from 'zustand';
import { persist } from 'zustand/middleware';

/**
 * Internal state of the commit store
 */
interface State {
    changes: Change[];
    __past: Change[][];
    __future: Change[][];
}

/**
 * Allowed actions on the commit store
 */
interface Actions {
    addChange: (change: Change) => void;
    removeChange: (change: Change) => void;
    findParameterChange: (service: Service, parameter: Parameter) => ParameterChange | undefined;
    findChange: (change: Change) => Change | undefined;
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
    changes: [],
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
                    const change = get()
                        .changes.filter(isParameterChange)
                        .find((c) => c.service.name === service.name && c.parameter.name === parameter.name);
                    return change;
                },
                findChange: (change) => {
                    return get().changes.find((c) => compareChanges(c, change) === 0);
                },
                addChange: (change) => {
                    const s = get();
                    // Ignore value when comparing, to remove existing parameter change, if one exists
                    if (s.changes.some((c) => compareChanges(c, change, { ignoreValue: true }) === 0)) {
                        s.removeChange(change);
                    }

                    set((s) => {
                        const changes = [...s.changes, change];
                        return {
                            changes,
                            __past: [...s.__past, s.changes],
                            __future: []
                        };
                    });
                },
                removeChange: (change) => {
                    set((s) => ({
                        changes: s.changes.filter((c) => compareChanges(c, change) !== 0),
                        __past: [...s.__past, s.changes],
                        __future: []
                    }));
                },
                undo: () => {
                    if (get().__past.length === 0) return;

                    set((s) => ({
                        changes: s.__past[s.__past.length - 1],
                        __past: s.__past.slice(0, s.__past.length - 1),
                        __future: [s.changes, ...s.__future]
                    }));
                },
                redo: () => {
                    if (get().__future.length === 0) return;

                    set((s) => ({
                        changes: s.__future[0],
                        __past: [...s.__past, s.changes],
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
