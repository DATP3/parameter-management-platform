import { Button } from 'rmwc';
import { Environment } from '../../features/environment/environment';
import QueryExample from './QueryExample';
import useCommitStore from '../../features/changes/useCommitStore';

/**
 * Page for displaying and editing parameters.
 */
const ParametersPage = () => {
    const addParameterChange = useCommitStore((s) => s.addParameterChange);

    const handleClick = () => {
        const now = Date.now();
        addParameterChange(
            {
                name: 'ServiceName1',
                address: 'ServiceAddress1',
                environment: Environment.TEST
            },
            {
                parameter: {
                    id: `paramId1`,
                    name: `nameKey1-${now}`,
                    value: `test-${now}`,
                    type: 'string'
                },
                newValue: `test-${now}`
            }
        );
    };

    return (
        <div>
            <h1>Parameters</h1>
            <Button raised onClick={handleClick}>
                Add parameter change
            </Button>
            <QueryExample />
        </div>
    );
};

export default ParametersPage;
