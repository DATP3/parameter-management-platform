import { ReactNode } from 'react';
import { Theme } from 'rmwc';

const ThemeMarkerWrapper = ({ children }: { children: ReactNode }) => {
    return (
        <div className='themeMarkerWrapper' style={{ display: 'flex' }}>
            <Theme use={['primaryBg', 'onPrimary']} wrap>
                <div
                    className='themeMarker'
                    style={{ width: '5px', height: 'auto', borderRadius: '5px', marginRight: '10px' }}
                ></div>
            </Theme>
            {children}
        </div>
    );
};

export default ThemeMarkerWrapper;
