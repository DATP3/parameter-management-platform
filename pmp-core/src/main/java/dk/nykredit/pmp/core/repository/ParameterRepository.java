package dk.nykredit.pmp.core.repository;

import dk.nykredit.pmp.core.persistence.ParameterEntity;

import java.util.List;

public interface ParameterRepository {

    ParameterEntity getValueByName(String name);

    ParameterEntity persistParameterEntity(ParameterEntity entity);

    /**
     * Get all parameters from service
     */
    List<ParameterEntity> getParameters();

    boolean checkIfParameterExists(String name);

    /**
     * For testing purposes: Start transaction
     */
    void startTransaction();

    /**
     * For testing purposes: End transaction
     */
    void endTransaction();
}
