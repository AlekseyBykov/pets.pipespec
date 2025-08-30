package dev.abykov.pets.pipespec.models.scheme;

import dev.abykov.pets.pipespec.exceptions.PipeSpecSchemeParseException;
import dev.abykov.pets.pipespec.models.scheme.data.PipeSpecSchemeDocument;
import dev.abykov.pets.pipespec.models.scheme.metadata.PipeSpecSchemeMetadata;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PipeSpecSchemeFile {

    private String version;
    private String marker;

    private List<PipeSpecSchemeMetadata> metadataSchemes = new ArrayList<>();
    private PipeSpecSchemeDocument data;

    public PipeSpecSchemeMetadata findMetadataSectionByMarker(String sectionMarker) {
        return metadataSchemes.stream()
                .filter(scheme -> StringUtils.equals(scheme.getMarker(), sectionMarker))
                .findFirst()
                .orElseThrow(
                        () -> PipeSpecSchemeParseException.missingSection(sectionMarker)
                );
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getMarker() {
        return marker;
    }

    public void setMarker(String marker) {
        this.marker = marker;
    }

    public List<PipeSpecSchemeMetadata> getMetadataSchemes() {
        return metadataSchemes;
    }

    public void setMetadataSchemes(List<PipeSpecSchemeMetadata> metadataSchemes) {
        this.metadataSchemes = metadataSchemes;
    }

    public PipeSpecSchemeDocument getData() {
        return data;
    }

    public void setData(PipeSpecSchemeDocument data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PipeSpecSchemeFile that = (PipeSpecSchemeFile) o;

        return Objects.equals(version, that.version) &&
                Objects.equals(marker, that.marker) &&
                Objects.equals(metadataSchemes, that.metadataSchemes) &&
                Objects.equals(data, that.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                version,
                marker,
                metadataSchemes,
                data
        );
    }
}
