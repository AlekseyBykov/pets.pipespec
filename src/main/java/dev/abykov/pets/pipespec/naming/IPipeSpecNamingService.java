package dev.abykov.pets.pipespec.naming;

import dev.abykov.pets.pipespec.models.pipespec.PipeSpecFile;
import dev.abykov.pets.pipespec.models.scheme.PipeSpecSchemeFile;

public interface IPipeSpecNamingService {

    void setFieldNames(PipeSpecFile pipespecFile, PipeSpecSchemeFile pipespecSchema);
}
