package dk.nykredit.pmp.core.service;

import dk.nykredit.pmp.core.persistence.ParameterEntity;
import dk.nykredit.pmp.core.repository.ParameterRepository;
import dk.nykredit.pmp.core.repository.exception.TypeCastingFailedException;
import dk.nykredit.pmp.core.util.EntityParser;

import javax.inject.Inject;
import java.util.Objects;

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

    @Override
    public <P> void updateParameter(String name, P value) {
        // If parameter does not exist, persist it and return
        if (!repository.checkIfParameterExists(name)) {
            persistParameter(name, value);
            return;
        }
        ParameterEntity entity = repository.getValueByName(name);
        // If parameter type does not match type, throw exception
        if (!Objects.equals(entity.getType(), value.getClass().getSimpleName())) {
            throw new TypeCastingFailedException(
                    "Expected type: " + entity.getType() + ". Got type: " + value.getClass().getSimpleName());
        }
        // Update parameter value
        entity.setPValue(value);
        // Persist parameter
        repository.persistParameterEntity(entity);
    }

    @Override
    public String getParameterTypeName(String parameterName) {
        // Get parameter type name
        ParameterEntity entity = repository.getValueByName(parameterName);

        // If parameter does not exist, return null
        if (entity == null) {
            return null;
        }

        // Return parameter type name
        return entity.getType();
    }

    @Override
    public ParameterRepository getRepository() {
        return repository;
    }
}
