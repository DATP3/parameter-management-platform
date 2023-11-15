package dk.nykredit.pmp.core.service;



import dk.nykredit.pmp.core.persistence.ParameterEntity;
import dk.nykredit.pmp.core.repository.ParameterRepository;
import dk.nykredit.pmp.core.util.EntityParser;

import javax.inject.Inject;


public class ParameterServiceImpl implements ParameterService {

    @Inject
    private ParameterRepository repository;
    @Inject
    private EntityParser entityParser;

    public EntityParser getTypeParsers() {
        return entityParser;
    }

    @Override
    public <T> T findParameterByName(String name) {
        ParameterEntity entity = repository.getValueByName(name);

        if (entity == null) {
            return null;
        }

        return entityParser.parse(entity);
    }

    @Override
    public <P> ParameterEntity persistParameter(String name, P value) {
        System.out.println("persisting");
        if (repository.checkIfParameterExists(name)) {
            System.out.println("Parameter exists");
            return null;
        }

        System.out.println("Parameter does not exist");

        ParameterEntity entity = new ParameterEntity();
        entity.setName(name);
        entity.setPValue(value);
        entity.setType(value.getClass().getSimpleName());

        return repository.persistParameterEntity(entity);
    }
}
