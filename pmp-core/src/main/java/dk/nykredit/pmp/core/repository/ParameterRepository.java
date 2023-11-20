package dk.nykredit.pmp.core.repository;


import dk.nykredit.pmp.core.persistence.ParameterEntity;

import java.util.List;

public interface ParameterRepository {

    ParameterEntity getValueByName(String name);

    ParameterEntity persistParameterEntity(ParameterEntity entity);

    List<ParameterEntity> getParameters();

    boolean checkIfParameterExists(String name);
}
