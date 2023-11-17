import { ReactNode } from "react";
import { Theme } from "rmwc";


const PrimaryBackgroundWrapper = ({ children }: { children: ReactNode }) => {
	return (
		<Theme use={['primaryBg', 'onPrimary']} wrap>
			{children}
		</Theme>
	)
}

export default PrimaryBackgroundWrapper;