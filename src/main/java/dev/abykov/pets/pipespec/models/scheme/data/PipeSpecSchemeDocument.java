package dev.abykov.pets.pipespec.models.scheme.data;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PipeSpecSchemeDocument {

    private PipeSpecSchemeHeader header;
    private List<PipeSpecSchemeTable> tables = new ArrayList<>();

    public PipeSpecSchemeTable findTableByMarker(String marker) {
        return tables.stream()
                .filter(table -> StringUtils.equals(table.getMarker(), marker))
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }

    public PipeSpecSchemeHeader getHeader() {
        return header;
    }

    public void setHeader(PipeSpecSchemeHeader header) {
        this.header = header;
    }

    public List<PipeSpecSchemeTable> getTables() {
        return tables;
    }

    public void setTables(List<PipeSpecSchemeTable> tables) {
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

        PipeSpecSchemeDocument that = (PipeSpecSchemeDocument) o;

        return Objects.equals(header, that.header) &&
                Objects.equals(tables, that.tables);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                header,
                tables
        );
    }
}
