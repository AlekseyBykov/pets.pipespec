package dev.abykov.pets.pipespec;

import dev.abykov.pets.pipespec.models.pipespec.PipeSpecFile;
import dev.abykov.pets.pipespec.models.scheme.PipeSpecSchemeFile;
import dev.abykov.pets.pipespec.naming.IPipeSpecNamingService;
import dev.abykov.pets.pipespec.parsers.pipespec.IPipeSpecFileParser;
import dev.abykov.pets.pipespec.parsers.scheme.IPipeSpecSchemeParser;
import dev.abykov.pets.pipespec.serialize.IPipeSpecSerializer;
import dev.abykov.pets.pipespec.validate.pipespec.IPipeSpecFileValidator;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

@Component
public class PipeSpecProcessor implements IPipeSpecProcessor {

    private static final String RESOURCE_PATH = "/dev/abykov/pets/pipespec/pipespec_schemes";

    private final IPipeSpecSchemeParser schemeParser;
    private final IPipeSpecFileParser fileParser;
    private final IPipeSpecFileValidator validator;
    private final IPipeSpecNamingService namingService;
    private final IPipeSpecSerializer serializer;

    public PipeSpecProcessor(
            IPipeSpecSchemeParser schemeParser,
            IPipeSpecFileParser fileParser,
            IPipeSpecFileValidator validator,
            IPipeSpecNamingService namingService,
            IPipeSpecSerializer serializer
    ) {
        this.schemeParser = schemeParser;
        this.fileParser = fileParser;
        this.validator = validator;
        this.namingService = namingService;
        this.serializer = serializer;
    }

    @Override
    public PipeSpecFile process(MultipartFile file, String documentMarker, String schemeName) {
        try (
                var schemeInputStream = loadFile(schemeName);
                var pipespecInputStream = file.getInputStream()
        ) {
            PipeSpecSchemeFile schemeFile = schemeParser.parse(schemeInputStream);
            PipeSpecFile pipeSpecFile = fileParser.parse(pipespecInputStream, documentMarker);

            validator.validate(pipeSpecFile, schemeFile);

            namingService.setFieldNames(pipeSpecFile, schemeFile);

            return pipeSpecFile;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toPlainText(PipeSpecFile pipespec) {
        return toString(pipespec);
    }

    @Override
    public byte[] toByteArray(PipeSpecFile pipespec, Charset charset) {
        return toString(pipespec).getBytes(charset);
    }

    private String toString(PipeSpecFile pipespec) {
        return serializer.serialize(pipespec);
    }

    private InputStream loadFile(String fileName) {
        return getClass().getResourceAsStream(
                String.format("%s/%s", RESOURCE_PATH, fileName)
        );
    }
}
