import { Button, DataTableCell, DataTableRow, TextField } from "rmwc";
import { Parameter } from "../../features/parameters/types";
import useCommitStore from "../../features/changes/useCommitStore";
import { Service } from "../../features/services/types";
import { ParameterValue } from "../../features/changes/types";
import validateParamChange from "../../features/changes/validateParamChange";
import Input from "./Input";
import "./style.css"

interface ParameterInputProps {
	parameter: Parameter;
	service: Service;
}

const ParameterInput = (props: ParameterInputProps) => {
	const { parameter, service } = props;
	const addParameterChange = useCommitStore((s) => s.addParameterChange);

	const parameterChange = useCommitStore((s) => s.findParameterChange(service, parameter));
	const hasChange = parameterChange !== undefined;
	const value = hasChange ? parameterChange.newValue : parameter.value;
	const isValid = validateParamChange({ parameter, newValue: value });

	const handleParameterChange = (newValue: ParameterValue) => {
		addParameterChange(service, { parameter, newValue });
	};

	return (
		<Input isValid={isValid} value={value} type={parameter.type} onParamChange={handleParameterChange} />
	);
}

export default ParameterInput;