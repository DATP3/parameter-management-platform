package dk.nykredit.pmp.core.util;

import java.util.function.Function;

import dk.nykredit.pmp.core.persistence.ParameterEntity;

/**
 * EntityParser interface
 */
public interface EntityParser {

    <T> T parse(ParameterEntity entity);

    void addParser(String type, Function<String, Object> parserMethod);
}
