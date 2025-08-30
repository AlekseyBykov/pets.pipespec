package dev.abykov.pets.pipespec;

import dev.abykov.pets.pipespec.test_utils.ResourceFileLoader;
import dev.abykov.pets.pipespec.validate.xsd.impl.XSDValidator;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class PipeSpecSchemesValidationTest {

    private static final String PIPESPEC_ROOT_PATH = "dev/abykov/pets/pipespec/pipespec_schemes";
    private static final String XSD_SCHEME_ROOT_PATH = "dev/abykov/pets/pipespec/xsd";

    @Test
    public void testValidatePipeSpecSchemeAgainstXsdScheme() throws Exception {
        File[] schemes = ResourceFileLoader.loadFiles(PIPESPEC_ROOT_PATH);
        File xsd = ResourceFileLoader.loadFile(
                XSD_SCHEME_ROOT_PATH,
                "PipeSpecSchemes.xsd"
        );

        XSDValidator validator = XSDValidator.newXSDValidator();
        Stream.of(schemes)
                .forEach(scheme -> validator.validate(scheme, xsd));

        assertTrue(
                CollectionUtils.isEmpty(
                        validator.getErrorMessages()
                )
        );
    }
}
