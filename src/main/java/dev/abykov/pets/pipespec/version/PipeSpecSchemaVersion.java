package dev.abykov.pets.pipespec.version;

import dev.abykov.pets.pipespec.exceptions.PipeSpecSchemaNotFoundException;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;

public enum PipeSpecSchemaVersion {

    /* since there are not many schemes, it can be versioned in enum */

    PSPDN180101(
            "PDN",
            "PSPDN180101",
            "PSPDN180101.xml",
            LocalDate.of(2024, Month.JANUARY, 1),
            DateBoundaries.UNLIMITED
    ),

    PSXXX180101(
            "XXX",
            "PSXXX180101",
            "PSXXX180101.xml",
            LocalDate.of(2024, Month.FEBRUARY, 1),
            DateBoundaries.UNLIMITED
    );

    private final String marker;
    private final String version;
    private final String schemaName;

    private final LocalDate src;
    private final LocalDate dst;

    PipeSpecSchemaVersion(
            String marker,
            String varsion,
            String schemaName,
            LocalDate src,
            LocalDate dst
    ) {
        this.marker = marker;
        this.version = varsion;
        this.schemaName = schemaName;
        this.src = src;
        this.dst = dst;
    }

    public static PipeSpecSchemaVersion lookup(String marker) {
        return lookup(marker, LocalDate.now());
    }

    public static PipeSpecSchemaVersion lookup(String marker, LocalDate date) {
        return Arrays.stream(values())
                .filter(schema -> StringUtils.equals(marker, schema.marker))
                .filter(schema -> schema.dst == null || !date.isAfter(schema.dst))
                .findFirst()
                .orElseThrow(
                        () -> new PipeSpecSchemaNotFoundException(
                                String.format(
                                        "Actual scheme not found for date %s and marker %s",
                                        date,
                                        marker
                                ))
                );
    }

    public String getMarker() {
        return marker;
    }

    public String getVersion() {
        return version;
    }

    public LocalDate getSrc() {
        return src;
    }

    public LocalDate getDst() {
        return dst;
    }

    private static class DateBoundaries {
        private static final LocalDate UNLIMITED = null;
    }
}
