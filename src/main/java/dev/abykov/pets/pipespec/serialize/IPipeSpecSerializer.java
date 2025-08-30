package dev.abykov.pets.pipespec.serialize;

import dev.abykov.pets.pipespec.models.pipespec.PipeSpecFile;

public interface IPipeSpecSerializer {

    String serialize(PipeSpecFile pipespec);
}
