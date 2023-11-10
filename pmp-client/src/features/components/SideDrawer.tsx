import { IconButton } from 'rmwc';
import { useState } from 'react';

interface SideDrawerProps {
    children: React.ReactNode;
    rtl?: boolean;
}

/**
 * Side drawer component made to replace the RMWC Drawer, as it didn't allow for drawers in both sides of the screen
 *
 * Instead this has to be used in conjunction with a flexbox, allowing the caller to specify placement and dimensions
 */
const SideDrawer = ({ children, rtl = false }: SideDrawerProps) => {
    const [open, setOpen] = useState(true);

    const icon = open === rtl ? 'chevron_right' : 'chevron_left';

    return (
        <div
            className={`flex flex-col h-full max-w-[300px] ${rtl ? 'border-l-[1px]' : 'border-r-[1px]'} ${
                open ? 'w-fit' : 'w-[49px]'
            }`}
        >
            <div className={`flex flex-none w-full ${rtl ? 'flex-row' : 'flex-row-reverse'}`}>
                <IconButton icon={icon} className='flex-none self-end' onClick={() => setOpen(!open)} />
            </div>
            <div className='flex-1 pr-4 pl-4'>{open && children}</div>
        </div>
    );
};

export default SideDrawer;
