import { InputTextFieldTypes, ParameterType, TextTypes } from "./types";
import { Switch, TextField } from "rmwc";

import { ParameterValue } from "../changes/types";

interface InputProps {
	isValid: boolean;
	value: ParameterValue;
	type: ParameterType;
	onParamChange: (newValue: ParameterValue) => void;
	disabled?: boolean;
}

const Input = (props: InputProps) => {
	const { type, isValid, disabled, value, onParamChange } = props;

	if (InputTextFieldTypes.includes(type))
		return (
			<TextField
				invalid={!isValid}
				disabled={disabled}
				type={TextTypes.includes(type) ? "text" : "number"}
				outlined
				className={"parameterInput" + (isValid ? " " : " invalid ")}
				value={value as string}
				onChange={(e: React.ChangeEvent<HTMLInputElement>) => {
					onParamChange(e.target.value)
				}}
			/>);


	if (type === ParameterType.LOCALDATETIME)
		return (
			<TextField
				outlined
				invalid={!isValid}
				disabled={disabled}
				className="parameterInput"
				type="datetime-local"
				value={value as string}
				onChange={(e: React.ChangeEvent<HTMLInputElement>) => {
					onParamChange(e.target.value);
				}}
			/>);

	if (type === ParameterType.LOCALDATE)
		return (
			<TextField
				outlined
				invalid={!isValid}
				disabled={disabled}
				className="parameterInput"
				type="date"
				value={value as string}
				onChange={(e: React.ChangeEvent<HTMLInputElement>) => {
					console.log(e.target.value);
					onParamChange(e.target.value);
				}}
			/>);

	if (type === ParameterType.BOOLEAN) {
		const boolValue = value === "true";
		return (
			<Switch
				label={value as string}
				checked={boolValue}
				disabled={disabled}
				onChange={(e: React.ChangeEvent<HTMLInputElement>) => {
					onParamChange(e.target.checked ? "true" : "false");
				}}
			/>);
	}

	//String input as defalut
	return <TextField
		invalid={!isValid}
		disabled={disabled}
		type={"text"}
		outlined
		className={"parameterInput" + (isValid ? " " : " invalid ")}
		value={value as string}
		onChange={(e: React.ChangeEvent<HTMLInputElement>) => {
			onParamChange(e.target.value)
		}}
	/>
}

export default Input;