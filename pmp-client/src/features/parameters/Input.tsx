
import { Switch, TextField } from "rmwc";
import { ParameterValue } from "../../features/changes/types";
import { InputTextFieldTypes, TextTypes } from "../../routes/parameters/types";
import { ParameterType } from "./types";

interface InputProps {
	isValid: boolean;
	value: ParameterValue;
	type: string;
	onParamChange: (newValue: ParameterValue) => void;

}

const Input = (props: InputProps) => {
	const { type, isValid, value, onParamChange } = props;

	if (InputTextFieldTypes.includes(type))
		return (<TextField
			invalid={!isValid}
			type={TextTypes.includes(type) ? "text" : "number"}
			outlined
			className={"parameterInput" + (isValid ? "" : " invalid")}
			value={value as string}
			onChange={(e: React.ChangeEvent<HTMLInputElement>) => {
				onParamChange(e.target.value)
			}}
		/>);

	if (type === ParameterType.LOCALDATETIME)
		return (<TextField
			outlined
			invalid={!isValid}
			className="parameterInput"
			type="datetime-local"
			value={value as string}
			onChange={(e: React.ChangeEvent<HTMLInputElement>) => {
				onParamChange(e.target.value);
			}} />);

	if (type === ParameterType.LOCALDATE)
		return (<TextField
			outlined
			invalid={!isValid}
			className="parameterInput"
			type="date"
			value={value as string}
			onChange={(e: React.ChangeEvent<HTMLInputElement>) => {
				console.log(e.target.value);
				onParamChange(e.target.value);
			}} />
		);

	if (type === ParameterType.BOOLEAN) {
		const boolValue = value === "true";
		return <Switch
			label={value as string}
			checked={boolValue}
			onChange={(e: any) => {
				onParamChange(e.target.checked ? "true" : "false");
			}} />
	}


	return <TextField />
}

export default Input;