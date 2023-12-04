package dk.nykredit.pmp.core.service;

import dk.nykredit.pmp.core.persistence.ParameterEntity;
import dk.nykredit.pmp.core.repository.ParameterRepository;
import dk.nykredit.pmp.core.util.EntityParser;

public interface ParameterService {
	<T> T findParameterByName(String name);

	<P> ParameterEntity persistParameter(String name, P value);

	<P> void updateParameter(String name, P value);

	EntityParser getTypeParsers();

	String getParameterTypeName(String parameterName);

	ParameterRepository getRepository();
}
