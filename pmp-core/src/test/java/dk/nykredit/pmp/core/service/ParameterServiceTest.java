package dk.nykredit.pmp.core.service;

import dk.nykredit.pmp.core.persistence.ParameterEntity;
import dk.nykredit.pmp.core.repository.ParameterRepository;
import dk.nykredit.pmp.core.util.EntityParser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

/**
 * Integration test of the functionality of PMP
 */
@ExtendWith(MockitoExtension.class)
class ParameterServiceTest {

    @Mock
    ParameterRepository parameterRepository;
    @Mock
    EntityParser entityParser;
    @InjectMocks
    private ParameterServiceImpl parameterService;

    @Test
    void testPMP() {
        String test_param_1 = "test_param_1";
        parameterService.persistParameter(test_param_1, 2);


        ParameterEntity entity = new ParameterEntity();
        entity.setName(test_param_1);
        entity.setType("Integer");
        entity.setId("123");
        entity.setPValue(2);

        Mockito.when(parameterRepository.getValueByName(ArgumentMatchers.anyString())).thenReturn(entity);
        Mockito.when(entityParser.parse(any(ParameterEntity.class))).thenReturn(2);

        int result = parameterService.findParameterByName(test_param_1);

        assertEquals(2, result);
    }

}
