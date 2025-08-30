package dev.abykov.pets.pipespec.models.pipespec;

import dev.abykov.pets.pipespec.exceptions.PipeSpecSchemeParseException;
import dev.abykov.pets.pipespec.models.pipespec.data.PipeSpecDocument;
import dev.abykov.pets.pipespec.models.pipespec.metadata.PipeSpecMetadata;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PipeSpecFile {

    private String version;
    private String marker;

    private List<PipeSpecMetadata> metadata = new ArrayList<>();
    private List<PipeSpecDocument> documents = new ArrayList<>();

    public PipeSpecMetadata findMetadataByMarker(String sectionMarker) {
        return metadata.stream()
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

    public List<PipeSpecMetadata> getMetadata() {
        return metadata;
    }

    public void setMetadata(List<PipeSpecMetadata> metadata) {
        this.metadata = metadata;
    }

    public List<PipeSpecDocument> getDocuments() {
        return documents;
    }

    public void setDocuments(List<PipeSpecDocument> documents) {
        this.documents = documents;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PipeSpecFile pipeSpecFile = (PipeSpecFile) o;

        return Objects.equals(version, pipeSpecFile.version) &&
                Objects.equals(marker, pipeSpecFile.marker) &&
                Objects.equals(metadata, pipeSpecFile.metadata) &&
                Objects.equals(documents, pipeSpecFile.documents);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                version,
                marker,
                metadata,
                documents
        );
    }
}
