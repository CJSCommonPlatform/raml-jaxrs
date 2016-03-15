package uk.gov.justice.raml.jaxrs.maven;

import org.raml.emitter.RamlEmitter;
import org.raml.model.Raml;
import uk.gov.justice.raml.core.Generator;
import uk.gov.justice.raml.core.GeneratorConfig;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static java.util.Arrays.asList;

/**
 * Generator for testing - the RAML and configuration are dumped to a JSON file.
 */
public class DummyGenerator implements Generator {

    @Override
    public void run(Raml raml, GeneratorConfig generatorConfig) {

        Path outputPath = Paths.get(generatorConfig.getOutputDirectory().toString(), "example.json");
        try {
            Files.createDirectories(outputPath.getParent());
            OutputStream outputStream = Files.newOutputStream(outputPath);
            JsonWriter writer = Json.createWriter(outputStream);
            writer.writeObject(Json.createObjectBuilder()
                    .add("raml", buildArray(asList(new RamlEmitter().dump(raml).split("\n"))))
                            .add("configuration", Json.createObjectBuilder()
                                    .add("basePackageName", generatorConfig.getBasePackageName())
                                    .add("sourceDirectory", generatorConfig.getSourceDirectory().toString())
                                    .add("outputDirectory", generatorConfig.getOutputDirectory().toString())
                            )
                            .build());
            writer.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private JsonArray buildArray(final List<String> items) {
        JsonArrayBuilder builder = Json.createArrayBuilder();
        for(String item : items) {
            builder = builder.add(item);
        }
        return builder.build();
    }
}