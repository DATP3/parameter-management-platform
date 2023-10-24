import { To, useNavigate } from 'react-router-dom';

import { useEffect } from 'react';

/** Redirects to the given URL once mounted */
const Redirecter = ({ to }: { to: To }) => {
    const navigate = useNavigate();
    useEffect(() => {
        navigate(to);
    }, [navigate, to]);
    return null;
};

export default Redirecter;
