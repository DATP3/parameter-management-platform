import useServices from './useServices';
import {
    Checkbox,
    DataTable,
    DataTableBody,
    DataTableCell,
    DataTableContent,
    DataTableHead,
    DataTableHeadCell,
    DataTableRow
} from 'rmwc';
import { ChangeEvent } from 'react';
import useSelectedServices from './useSelectedServices';

const ListofServices = () => {
    const list = useServices();
    const [selectedServices, setSelectedServices] = useSelectedServices();

    return (
        <DataTable>
            <DataTableContent>
                <DataTableHead>
                    <DataTableRow>
                        <DataTableHeadCell>Services</DataTableHeadCell>
                        <DataTableHeadCell hasFormControl alignEnd>
                            <Checkbox
                                checked={selectedServices.length === list.length}
                                onChange={(evt: ChangeEvent<HTMLInputElement>) => {
                                    if (evt.currentTarget.checked) {
                                        setSelectedServices(list);
                                    } else {
                                        setSelectedServices([]);
                                    }
                                }}
                            />
                        </DataTableHeadCell>
                    </DataTableRow>
                </DataTableHead>
                <DataTableBody>
                    {list.map((s) => (
                        <DataTableRow key={s.name}>
                            <DataTableCell>{s.name}</DataTableCell>
                            <DataTableCell hasFormControl>
                                <Checkbox
                                    checked={!!selectedServices.find((service) => s.address === service.address)}
                                    onChange={(evt: ChangeEvent<HTMLInputElement>) => {
                                        if (evt.currentTarget.checked) {
                                            setSelectedServices([...selectedServices, s]);
                                        } else {
                                            setSelectedServices(
                                                selectedServices.filter((service) => s.address !== service.address)
                                            );
                                        }
                                    }}
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
