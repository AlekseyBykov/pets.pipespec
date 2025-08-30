package dev.abykov.pets.pipespec.models.pipespec.data;

import dev.abykov.pets.pipespec.models.pipespec.field.PipeSpecField;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PipeSpecTable {

    private String marker;
    private List<PipeSpecField> fields = new ArrayList<>();

    public PipeSpecTable() {
    }

    public PipeSpecTable(String marker, List<PipeSpecField> fields) {
        this.marker = marker;
        this.fields = fields;
    }

    public static PipeSpecTable newTable(List<String> fields, String marker) {
        PipeSpecTable table = new PipeSpecTable();

        int valuePos = 1;
        for (int i = valuePos; i < fields.size(); i++) {
            PipeSpecField field = new PipeSpecField();

            var fieldValue = fields.get(i);
            field.setValue(fieldValue);
            table.getFields().add(field);
        }

        table.setMarker(marker);
        return table;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getFieldValueByName(String name) {
        PipeSpecField field = getFieldByName(name);
        return field != null
                ? field.getValue()
                : null;
    }

    public PipeSpecField getFieldByName(String name) {
        return fields.stream()
                .filter(field -> StringUtils.equals(field.getName(), name))
                .findFirst()
                .orElse(null);
    }

    public void addField(String name, Object value) {
        PipeSpecField field = new PipeSpecField();
        field.setName(name);
        field.setValue(Objects.toString(value, StringUtils.EMPTY));

        fields.add(field);
    }

    public String getMarker() {
        return marker;
    }

    public void setMarker(String marker) {
        this.marker = marker;
    }

    public List<PipeSpecField> getFields() {
        return fields;
    }

    public void setFields(List<PipeSpecField> fields) {
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

        PipeSpecTable pipeSpecTable = (PipeSpecTable) o;

        return Objects.equals(marker, pipeSpecTable.marker) &&
                Objects.equals(fields, pipeSpecTable.fields);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                marker,
                fields
        );
    }

    public static class Builder {
        private String marker;
        private final List<PipeSpecField> fields = new ArrayList<>();

        Builder() {
        }

        public Builder marker(String marker) {
            this.marker = marker;
            return this;
        }

        public Builder field(String name, Object value) {
            PipeSpecField field = new PipeSpecField();
            field.setName(name);
            field.setValue(Objects.toString(value, StringUtils.EMPTY));

            fields.add(field);

            return this;
        }

        public PipeSpecTable build() {
            return new PipeSpecTable(this.marker, this.fields);
        }
    }
}
