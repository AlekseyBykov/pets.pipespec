package dev.abykov.pets.pipespec.validate.xsd;

import java.io.File;

public interface IXSDValidator {

    void validate(File xml, File xsd);

    boolean isValidScheme(File xml, File xsd) throws Exception;
}
