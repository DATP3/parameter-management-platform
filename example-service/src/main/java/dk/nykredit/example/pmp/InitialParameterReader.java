package dk.nykredit.example.pmp;

import dk.nykredit.pmp.core.persistence.ParameterEntity;
import dk.nykredit.pmp.core.util.EntityParser;

import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class InitialParameterReader {

    private static final String PARAMETERS_FILE = "initial-params.csv";

    @Inject
    private EntityParser parser;

    public List<ParameterEntity> readParameters() throws IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String path = classLoader.getResource(PARAMETERS_FILE).getPath();
        try (Stream<String> lines = Files.lines(Path.of(path))) {
            return lines.map(Parameter::fromLine).map(p -> {
                ParameterEntity entity = new ParameterEntity();
                entity.setName(p.parameterName);
                entity.setType(p.type);
                entity.setPValue(parser.parse(p.value, p.type));
                return entity;
            }).collect(Collectors.toList());
        }
    }

    public static class Parameter {
        public String parameterName;
        public String value;
        public String type;

        public static Parameter fromLine(String line) {
            String[] split = line.split(",", 3);

            Parameter p = new Parameter();
            p.parameterName = split[0];
            p.type = split[1];
            p.value = split[2];
            return p;
        }
    }
}
