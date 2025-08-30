package dev.abykov.pets.pipespec.parsers.scheme;

import dev.abykov.pets.pipespec.models.scheme.PipeSpecSchemeFile;

import java.io.File;
import java.io.InputStream;

public interface IPipeSpecSchemeParser {

    PipeSpecSchemeFile parse(File file);

    PipeSpecSchemeFile parse(InputStream inputStream);
}
