package dk.nykredit.pmp.core.util;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;

import javax.enterprise.context.ApplicationScoped;

import dk.nykredit.pmp.core.persistence.ParameterEntity;
import dk.nykredit.pmp.core.repository.exception.TypeCastingFailedException;

/**
 * EntityParser implementation used by PMP to retrieve and validate objects in
 * DB
 */
@ApplicationScoped
public class EntityParserImpl implements EntityParser {

	private static final Map<String, Function<String, Object>> typeParsers = new HashMap<>();

	static {
		typeParsers.put("boolean", Boolean::parseBoolean);
		typeParsers.put("byte", Byte::parseByte);
		typeParsers.put("short", Short::parseShort);
		typeParsers.put("integer", Integer::parseInt);
		typeParsers.put("long", Long::parseLong);
		typeParsers.put("float", Float::parseFloat);
		typeParsers.put("double", Double::parseDouble);
		typeParsers.put("character", str -> str.charAt(0));
		typeParsers.put("string", v -> v);
		typeParsers.put("bigdecimal", BigDecimal::new);
		typeParsers.put("localdate", LocalDate::parse);
		typeParsers.put("localdatetime", LocalDateTime::parse);
	}

	@Override
	public <T> T parse(ParameterEntity entity) {
		String type = entity.getType().toLowerCase(Locale.ROOT);
		String value = String.valueOf(entity.getPValue());

		if (typeParsers.containsKey(type)) {
			return (T) typeParsers.get(type).apply(value);
		} else {
			throw new TypeCastingFailedException(
					String.format("Failed to cast parameter [Name: %s, Type: %s, Value: %s]",
							entity.getName(), entity.getType(), entity.getPValue()));
		}
	}

	@Override
	public <T> T parse(String value, String type) {
		String typeLower = type.toLowerCase(Locale.ROOT);
		if (typeParsers.containsKey(typeLower)) {
			return (T) typeParsers.get(typeLower).apply(value);
		} else {
			throw new TypeCastingFailedException(
					String.format("Failed to cast parameter [Type: %s, Value: %s]",
							type, value));
		}
	}

	@Override
	public void addParser(String name, Function<String, Object> parserMethod) {
		typeParsers.put(name, parserMethod);
	}

}
