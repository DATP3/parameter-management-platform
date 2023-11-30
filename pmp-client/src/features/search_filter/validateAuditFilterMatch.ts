import { AuditLogEntry } from '../audit/useAuditLogEntries';
import { AuditFilter } from './types';

const validateAuditFilterMatch = (filter: AuditFilter, entry: AuditLogEntry) => {
    let compareString = `${entry.hash}¤${entry.pushDate.toLocaleString()}¤${entry.email}¤${entry.message}`;

    for (const affectedService of entry.affectedServices) {
        compareString += `¤${affectedService}`;
    }

    for (const change of entry.changes) {
        for (const revert of change.reverts) {
            compareString += `¤${revert.referenceHash}¤${revert.revertType}`;
            for (const parameterChange of revert.parameterChanges) {
                compareString += `¤${parameterChange.name}¤${parameterChange.oldValue}¤${parameterChange.newValue}`;
            }
        }
        for (const parameterChange of change.parameterChanges) {
            compareString += `¤${parameterChange.name}¤${parameterChange.oldValue}¤${parameterChange.newValue}`;
        }
    }

    let isMatching = true;

    if (filter.searchQuery) {
        isMatching = isMatching && compareString.includes(filter.searchQuery);
    }

    let serviceMatch = false;
    const selectedServices = filter.services ?? [];

    for (const selectedService of selectedServices) {
        for (const { service } of entry.changes) {
            serviceMatch = serviceMatch || service.address === selectedService.address;
        }
    }

    isMatching = isMatching && serviceMatch;

    return isMatching;
};

export default validateAuditFilterMatch;
