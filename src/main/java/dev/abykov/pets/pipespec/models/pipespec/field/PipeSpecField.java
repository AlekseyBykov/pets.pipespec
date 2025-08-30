package dev.abykov.pets.pipespec.models.pipespec.field;

import java.util.Objects;

public class PipeSpecField {

    private String name;
    private String value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PipeSpecField pipeSpecField = (PipeSpecField) o;

        return Objects.equals(name, pipeSpecField.name) &&
                Objects.equals(value, pipeSpecField.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                name,
                value
        );
    }
}
