package dev.abykov.pets.pipespec.exceptions;

public class PipeSpecSchemeParseException extends RuntimeException {

    private PipeSpecSchemeParseException(String message) {
        super(message);
    }

    public static PipeSpecSchemeParseException missingField(int position) {
        return new PipeSpecSchemeParseException(
                String.format("Validation error: PipeSpec schema field not found at position %s", position)
        );
    }

    public static PipeSpecSchemeParseException missingSection(String sectionMarker) {
        return new PipeSpecSchemeParseException(
                String.format("Validation error: PipeSpec schema section not found for marker %s", sectionMarker)
        );
    }
}
