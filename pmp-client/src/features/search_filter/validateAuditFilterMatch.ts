import { AuditLogEntry } from '../audit/useAuditLogEntries';
import { AuditFilter } from './types';

const validateAuditFilterMatch = (filter: AuditFilter, entry: AuditLogEntry) => {
    let compareString = `${entry.hash}¤${entry.pushDate.toLocaleString()}¤${entry.email}¤${entry.message}`;

    for (const affectedService of entry.affectedServices) {
        compareString += `¤${affectedService}`;
    }

    let isMatching = true;

    if (filter.searchQuery) {
        isMatching = isMatching && compareString.includes(filter.searchQuery);
    }
    return isMatching;
};

export default validateAuditFilterMatch;
