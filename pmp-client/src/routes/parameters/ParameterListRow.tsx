import { Button, DataTableCell, DataTableRow } from 'rmwc';

import Input from '../../features/parameters/Input';
import { Parameter } from '../../features/parameters/types';
import { ParameterValue } from '../../features/changes/types';
import { Service } from '../../features/services/types';
import useCommitStore from '../../features/changes/useCommitStore';
import { useEffect } from 'react';
import { useParameterFilter } from '../../features/search_filter/useParamererFilter';
import validateParamChange from '../../features/changes/validateParamChange';
import validateParameterFilterMatch from '../../features/search_filter/validateParameterFilterMatch';

interface ParameterListRowProps {
    parameter: Parameter;
    service: Service;
}

const ParameterListRow = ({ parameter, service }: ParameterListRowProps) => {
    const addChange = useCommitStore((s) => s.addChange);
    const removeChange = useCommitStore((s) => s.removeChange);
    const [filter] = useParameterFilter();

    const parameterChange = useCommitStore((s) => s.findParameterChange(service, parameter));
    const hasChange = parameterChange !== undefined;
    const value = hasChange ? parameterChange.newValue : parameter.value;

    useEffect(() => {
        console.log('[ParameterListRow] parameter:', parameter);
    }, [parameter]);

    useEffect(() => {
        console.log('[ParameterListRow] parameterChange:', parameterChange);
    }, [parameterChange]);

    useEffect(() => {
        console.log('[ParameterListRow] value:', value);
    }, [value]);

    const isValid = !hasChange || validateParamChange(parameterChange);

    const handleParamerterChangeReset = () => {
        removeChange(parameterChange!);
    };

    const handleParameterChange = (newValue: ParameterValue) => {
        addChange({ parameter, service, newValue });
    };

    if (!validateParameterFilterMatch(filter, parameter)) return null;

    return (
        <DataTableRow className='tableRow'>
            <DataTableCell>{parameter.name}</DataTableCell>
            <DataTableCell>{parameter.type}</DataTableCell>
            <DataTableCell>
                <Input isValid={isValid} value={value} type={parameter.type} onParamChange={handleParameterChange} />
            </DataTableCell>
            <DataTableCell alignEnd>
                <Button
                    className={!hasChange ? 'hidden' : ''}
                    outlined
                    icon='restart_alt'
                    label='reset'
                    onClick={handleParamerterChangeReset}
                />
            </DataTableCell>
        </DataTableRow>
    );
};

export default ParameterListRow;
