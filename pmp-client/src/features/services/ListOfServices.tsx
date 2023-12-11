import {
    Checkbox,
    CircularProgress,
    DataTable,
    DataTableBody,
    DataTableCell,
    DataTableContent,
    DataTableHead,
    DataTableHeadCell,
    DataTableRow,
    Typography
} from 'rmwc';

import useSelectedServices from './useSelectedServices';
import useServices from './useServices';

/** List radios with services to pick which are active */
const ListofServices = () => {
    const { data: services, isPending, error } = useServices();
    const [selectedServices, setSelectedServices] = useSelectedServices();

    if (isPending)
        return (
            <>
                <Typography use='headline6'>Waiting on services</Typography> <CircularProgress />
            </>
        );

    if (error) return <Typography use='headline6'>Error loading services</Typography>;

    const sortedServices = services.sort((s1, s2) => s1.name.localeCompare(s2.name));

    return (
        <DataTable>
            <DataTableContent>
                <DataTableHead>
                    <DataTableRow
                        className='cursor-pointer'
                        onClick={() => {
                            if (selectedServices.length === services.length) {
                                setSelectedServices([]);
                            } else {
                                setSelectedServices(services);
                            }
                        }}
                    >
                        <DataTableHeadCell>All services</DataTableHeadCell>
                        <DataTableHeadCell hasFormControl alignEnd>
                            <Checkbox checked={selectedServices.length === services.length} />
                        </DataTableHeadCell>
                    </DataTableRow>
                </DataTableHead>
                <DataTableBody>
                    {sortedServices.map((s) => (
                        <DataTableRow
                            className='cursor-pointer'
                            key={s.name}
                            onClick={() => {
                                if (selectedServices.find((service) => s.address === service.address)) {
                                    setSelectedServices(
                                        selectedServices.filter((service) => s.address !== service.address)
                                    );
                                } else {
                                    setSelectedServices([...selectedServices, s]);
                                }
                            }}
                        >
                            <DataTableCell>{s.name}</DataTableCell>
                            <DataTableCell hasFormControl>
                                <Checkbox
                                    checked={!!selectedServices.find((service) => s.address === service.address)}
                                />
                            </DataTableCell>
                        </DataTableRow>
                    ))}
                </DataTableBody>
            </DataTableContent>
        </DataTable>
    );
};

export default ListofServices;
