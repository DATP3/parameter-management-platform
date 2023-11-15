import { Button, DataTableCell, DataTableRow } from 'rmwc';

import { Parameter } from '../../features/parameters/types';
import { ParameterValue } from '../../features/changes/types';
import { Service } from '../../features/services/types';
import useCommitStore from '../../features/changes/useCommitStore';
import validateParamChange from '../../features/changes/validateParamChange';
import Input from '../../features/parameters/Input';
import { useParameterFilter } from '../../features/search_filter/useParamererFilter';
import validateParameterFilterMatch from '../../features/search_filter/validateParameterFilterMatch';

interface ParameterListRowProps {
	parameter: Parameter;
	service: Service;
}

const ParameterListRow = (props: ParameterListRowProps) => {
	const { parameter, service } = props;
	const addParameterChange = useCommitStore((s) => s.addParameterChange);
	const removeParameterChange = useCommitStore((s) => s.removeParameterChange);
	const [filter] = useParameterFilter();

	const parameterChange = useCommitStore((s) => s.findParameterChange(service, parameter));
	const hasChange = parameterChange !== undefined;
	const value = hasChange ? parameterChange.newValue : parameter.value;

	const isValid = validateParamChange({ parameter, newValue: value });

	const handleParamerterChangeReset = () => {
		removeParameterChange(service, parameterChange!);
		console.log(parameterChange);
	};

	const handleParameterChange = (newValue: ParameterValue) => {
		addParameterChange(service, { parameter, newValue });
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
