import { DataTable, DataTableBody, DataTableContent, DataTableHead, DataTableHeadCell, DataTableRow } from 'rmwc';
import ParameterListRow from './ParameterListRow';
import { Parameter } from '../../features/parameters/types';
import { Service } from '../../features/services/types';

interface ParameterListProps {
    parameters: Parameter[];
    service: Service;
}

const ParameterList = (props: ParameterListProps) => {
    const { parameters, service } = props;

    return (
        <DataTable className='parameterTable'>
            <DataTableContent className='tableHead'>
                <DataTableHead>
                    <DataTableRow>
                        <DataTableHeadCell className='headCell'>Name</DataTableHeadCell>
                        <DataTableHeadCell className='headCell'>Type</DataTableHeadCell>
                        <DataTableHeadCell className='headCell'>Value</DataTableHeadCell>
                    </DataTableRow>
                </DataTableHead>
                <DataTableBody>
                    {parameters.map((parameter) => (
                        <ParameterListRow service={service} parameter={parameter} />
                    ))}
                </DataTableBody>
            </DataTableContent>
        </DataTable>
    );
};

export default ParameterList;
