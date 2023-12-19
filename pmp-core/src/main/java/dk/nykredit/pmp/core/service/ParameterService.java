package dk.nykredit.pmp.core.service;

import dk.nykredit.pmp.core.persistence.ParameterEntity;
import dk.nykredit.pmp.core.repository.ParameterRepository;
import dk.nykredit.pmp.core.util.EntityParser;

public interface ParameterService {
    <T> T findParameterByName(String name);

    <P> ParameterEntity persistParameter(String name, P value);

    /**
     * Persist parameter entity
     *
     * @name parameter name
     * @value parameter value
     */
    <P> void updateParameter(String name, P value);

    /**
     * Get type parsers
     * 
     * @return EntityParser
     */
    EntityParser getTypeParsers();

    /**
     * Get parameter type name
     * 
     * @param parameterName parameter name
     * @return parameter type name
     */
    String getParameterTypeName(String parameterName);

    /**
     * Get parameter repository
     * 
     * @return ParameterRepository
     */
    ParameterRepository getRepository();
}
