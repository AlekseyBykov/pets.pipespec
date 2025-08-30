package dev.abykov.pets.pipespec.models.scheme;

import dev.abykov.pets.pipespec.models.scheme.field.PipeSpecSchemeField;

public interface IndexedScheme {

    PipeSpecSchemeField findFieldByPosition(int position);
}
