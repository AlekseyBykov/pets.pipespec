package dev.abykov.pets.pipespec.parsers.pipespec;

import dev.abykov.pets.pipespec.models.pipespec.PipeSpecFile;

import java.io.File;
import java.io.InputStream;

public interface IPipeSpecFileParser {

    PipeSpecFile parse(File file, String documentMarker);

    PipeSpecFile parse(InputStream inputStream, String documentMarker);
}
