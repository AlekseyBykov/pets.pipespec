package dev.abykov.pets.pipespec.exceptions;

public class PipeSpecFileValidationException extends RuntimeException {

    private PipeSpecFileValidationException(String message) {
        super(message);
    }

    public static PipeSpecFileValidationException missingSection(String sectionMarker) {
        return new PipeSpecFileValidationException(
                String.format("Section %s defined in the schema was not found in the PipeSpec file", sectionMarker)
        );
    }

    public static PipeSpecFileValidationException wrongFieldsNumber() {
        return new PipeSpecFileValidationException(
                "The number of fields in the PipeSpec file does not match the schema"
        );
    }

    public static PipeSpecFileValidationException wrongDataType(String fieldName) {
        return new PipeSpecFileValidationException(
                String.format("Data type of field %s does not match the schema", fieldName)
        );
    }

    public static PipeSpecFileValidationException mandatoryFieldIsEmpty(String fieldName) {
        return new PipeSpecFileValidationException(
                String.format("Field %s is mandatory but empty", fieldName)
        );
    }
}
