/**
 * Groups an array of items by a key returned by the given function.
 * @param arr The array to group.
 * @param fn The function to get the key to group by.
 * @returns An record object with the keys being the group keys and the values being the items in the group.
 */
export const groupBy = <T>(arr: T[], fn: (item: T) => string): Record<string, T[]> => {
    return arr.reduce<Record<string, T[]>>((prev, curr) => {
        const groupKey = fn(curr);
        const group = prev[groupKey] || [];
        group.push(curr);
        return { ...prev, [groupKey]: group };
    }, {});
};
