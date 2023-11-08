import useCommitStore from '../useCommitStore';

/**
 * Displays a list of changes made in the current commit.
 */
const ChangeList = () => {
    const changes = useCommitStore((s) => s.serviceChanges);

    return (
        <div>
            {/** List services */}
            {changes.map((serviceChange) => (
                <div key={serviceChange.service.address}>
                    <p>{serviceChange.service.name}</p>

                    {/** List reverts */}
                    {serviceChange.reverts.map((revert) => (
                        <div key={revert.commitReference}>
                            <p>{revert.commitReference}</p>
                        </div>
                    ))}

                    {/** Write each parameter change */}
                    {serviceChange.parameterChanges.map((parameterChange) => (
                        <div key={parameterChange.parameter.id} className='bg-gray-500'>
                            <p>{parameterChange.parameter.name}</p>
                            <br />
                            <p>{parameterChange.parameter.value.toString()}</p>
                            <p>{parameterChange.newValue.toString()}</p>
                        </div>
                    ))}
                </div>
            ))}
        </div>
    );
};

export default ChangeList;
