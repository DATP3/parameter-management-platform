package dk.nykredit.pmp.core.commit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import dk.nykredit.pmp.core.remote.json.ObjectMapperFactoryImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@TestInstance(Lifecycle.PER_CLASS)
public class TestChangeDeserializer {

	ObjectMapper mapper;

	@BeforeAll
	public void setupMapper() {
		mapper = new ObjectMapperFactoryImpl().getObjectMapper();
	}

	@Test
	public void canDeserializeParameterChange() throws JsonProcessingException {
		ParameterChange expectedChange = new ParameterChange();
		expectedChange.setName("test");
		expectedChange.setNewValue("0");
		expectedChange.setOldValue("1");
		expectedChange.setType("integer");

		String json = mapper.writeValueAsString(expectedChange);
		Change change = mapper.readValue(json, Change.class);
		assertEquals(expectedChange, change);
	}
}
