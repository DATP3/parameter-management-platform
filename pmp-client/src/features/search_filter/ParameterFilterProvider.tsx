import { ReactNode, useState } from "react";
import { parameterFilterContext } from "./parameterFilterContext";
import { ParameterFilter } from "./types";

const ParameterFilterProvider = ({ children }: { children: ReactNode }) => {
	const [paramFilter, setParamFilter] = useState<ParameterFilter>({});

	return (
		<parameterFilterContext.Provider value={[paramFilter, setParamFilter]}>
			{children}
		</parameterFilterContext.Provider>);
};


export default ParameterFilterProvider;
