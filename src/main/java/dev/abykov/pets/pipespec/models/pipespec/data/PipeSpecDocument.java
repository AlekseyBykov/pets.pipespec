package dev.abykov.pets.pipespec.models.pipespec.data;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PipeSpecDocument {

    private PipeSpecHeader header;
    private List<PipeSpecTable> tables = new ArrayList<>();

    public PipeSpecDocument() {
    }

    public PipeSpecDocument(PipeSpecHeader header) {
        this.header = header;
    }

    public List<PipeSpecTable> findTablesByMarker(String marker) {
        return tables.stream()
                .filter(table -> StringUtils.equals(table.getMarker(), marker))
                .toList();
    }

    public PipeSpecHeader getHeader() {
        return header;
    }

    public void setHeader(PipeSpecHeader header) {
        this.header = header;
    }

    public List<PipeSpecTable> getTables() {
        return tables;
    }

    public void setTables(List<PipeSpecTable> tables) {
        this.tables = tables;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PipeSpecDocument document = (PipeSpecDocument) o;

        return Objects.equals(header, document.header) &&
                Objects.equals(tables, document.tables);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                header,
                tables
        );
    }
}
