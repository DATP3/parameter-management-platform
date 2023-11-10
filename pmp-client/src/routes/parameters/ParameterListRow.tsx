import { Button, DataTableCell, DataTableRow, TextField } from "rmwc";
import "./style.css"
import { Parameter } from "../../features/parameters/types";
import useCommitStore from "../../features/changes/useCommitStore";
import { Service } from "../../features/services/types";
import { ParameterValue } from "../../features/changes/types";
import validateParamChange from "../../features/changes/validateParamChange";
import Input from "./Input";

interface ParameterListRowProps {
	parameter: Parameter;
	service: Service;
}

const ParameterListRow = (props: ParameterListRowProps) => {
	const { parameter, service } = props;
	const addParameterChange = useCommitStore((s) => s.addParameterChange);
	const removeParameterChange = useCommitStore((s) => s.removeParameterChange);

	const parameterChange = useCommitStore((s) => s.findParameterChange(service, parameter));
	const hasChange = parameterChange !== undefined;
	const value = hasChange ? parameterChange.newValue : parameter.value;

	const isValid = validateParamChange({parameter, newValue: value});

	const handleParamerterChangeReset = () => {
		removeParameterChange(service, parameterChange!);
		console.log(parameterChange)
	};

	const handleParameterChange = (newValue: ParameterValue) => {
		addParameterChange(service, { parameter, newValue });
	};

	return (
		<DataTableRow className="tableRow">
			<DataTableCell>{parameter.name}</DataTableCell>
			<DataTableCell>{parameter.type}</DataTableCell>
			<DataTableCell>
				{/* <TextField
					invalid={!isValid}
					// prefix={isValid ? "" : "type not " + parameter.type}
					outlined
					className={"parameterInput" + (isValid ? "" : " invalid")}
					value={value as string}
					onChange={(e: React.ChangeEvent<HTMLInputElement>) => {
						handleParameterChange(e.target.value)
					}}
				/> */}
				<Input isValid={isValid} value={value} type={parameter.type} onParamChange={handleParameterChange} />
			</DataTableCell>
			<DataTableCell alignEnd>
				<Button
					className={!hasChange ? "hidden" : ""}
					outlined icon="restart_alt"
					label="reset"
					onClick={handleParamerterChangeReset}
				/>
			</DataTableCell>
		</DataTableRow>
	);
}

export default ParameterListRow;