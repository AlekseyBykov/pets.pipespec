package dev.abykov.pets.pipespec.validate.pipespec;

import dev.abykov.pets.pipespec.models.pipespec.PipeSpecFile;
import dev.abykov.pets.pipespec.models.scheme.PipeSpecSchemeFile;

public interface IPipeSpecFileValidator {

    void validate(PipeSpecFile pipespec, PipeSpecSchemeFile scheme);
}
