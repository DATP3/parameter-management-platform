/** Uniformly formats dates. For use whenever a date is displayed */
export const formatDate = (date: Date): string => {
    return `${date.getDate()}.${date.getMonth() + 1}.${date.getFullYear()}`;
};
