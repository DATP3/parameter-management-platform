package dk.nykredit.pmp.core.service;


import javax.inject.Inject;

import dk.nykredit.pmp.core.persistence.ParameterEntity;
import dk.nykredit.pmp.core.repository.ParameterRepository;
import dk.nykredit.pmp.core.util.EntityParser;


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


        if (repository.checkIfParameterExists(name)) {
            return null;
        }

        ParameterEntity entity = new ParameterEntity();
        entity.setName(name);
        entity.setPValue(value);
        entity.setType(value.getClass().getSimpleName());

        return repository.persistParameterEntity(entity);
    }
}
