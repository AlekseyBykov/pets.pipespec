package dev.abykov.pets.pipespec.models.scheme.metadata;

import dev.abykov.pets.pipespec.exceptions.PipeSpecSchemeParseException;
import dev.abykov.pets.pipespec.models.scheme.IndexedScheme;
import dev.abykov.pets.pipespec.models.scheme.field.PipeSpecSchemeField;

import java.util.ArrayList;
import java.util.List;

public class PipeSpecSchemeMetadata implements IndexedScheme {

    private String marker;
    private List<PipeSpecSchemeField> fields = new ArrayList<>();

    @Override
    public PipeSpecSchemeField findFieldByPosition(int position) {
        return fields.stream()
                .filter(pipespecField -> pipespecField.getPosition() == position)
                .findFirst()
                .orElseThrow(
                        () -> PipeSpecSchemeParseException.missingField(position)
                );
    }

    public void setMarker(String marker) {
        this.marker = marker;
    }

    public String getMarker() {
        return marker;
    }

    public List<PipeSpecSchemeField> getFields() {
        return fields;
    }

    public void setFields(List<PipeSpecSchemeField> fields) {
        this.fields = fields;
    }
}
