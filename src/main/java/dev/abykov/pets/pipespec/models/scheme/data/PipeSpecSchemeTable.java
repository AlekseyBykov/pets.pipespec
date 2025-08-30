package dev.abykov.pets.pipespec.models.scheme.data;

import dev.abykov.pets.pipespec.models.scheme.IndexedScheme;
import dev.abykov.pets.pipespec.models.scheme.field.PipeSpecSchemeField;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PipeSpecSchemeTable implements IndexedScheme {

    private String marker;
    private List<PipeSpecSchemeField> fields = new ArrayList<>();

    @Override
    public PipeSpecSchemeField findFieldByPosition(int position) {
        return fields.stream()
                .filter(pipespecField -> pipespecField.getPosition() == position)
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }

    public String getMarker() {
        return marker;
    }

    public void setMarker(String marker) {
        this.marker = marker;
    }

    public List<PipeSpecSchemeField> getFields() {
        return fields;
    }

    public void setFields(List<PipeSpecSchemeField> fields) {
        this.fields = fields;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PipeSpecSchemeTable that = (PipeSpecSchemeTable) o;

        return Objects.equals(marker, that.marker) &&
                Objects.equals(fields, that.fields);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                marker,
                fields
        );
    }
}
