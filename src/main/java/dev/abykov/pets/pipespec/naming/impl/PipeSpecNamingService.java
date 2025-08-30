package dev.abykov.pets.pipespec.naming.impl;

import dev.abykov.pets.pipespec.models.pipespec.PipeSpecFile;
import dev.abykov.pets.pipespec.models.pipespec.data.PipeSpecDocument;
import dev.abykov.pets.pipespec.models.pipespec.data.PipeSpecTable;
import dev.abykov.pets.pipespec.models.pipespec.field.PipeSpecField;
import dev.abykov.pets.pipespec.models.pipespec.metadata.PipeSpecMetadata;
import dev.abykov.pets.pipespec.models.scheme.IndexedScheme;
import dev.abykov.pets.pipespec.models.scheme.PipeSpecSchemeFile;
import dev.abykov.pets.pipespec.models.scheme.data.PipeSpecSchemeDocument;
import dev.abykov.pets.pipespec.models.scheme.data.PipeSpecSchemeHeader;
import dev.abykov.pets.pipespec.models.scheme.data.PipeSpecSchemeTable;
import dev.abykov.pets.pipespec.models.scheme.field.PipeSpecSchemeField;
import dev.abykov.pets.pipespec.models.scheme.metadata.PipeSpecSchemeMetadata;
import dev.abykov.pets.pipespec.naming.IPipeSpecNamingService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PipeSpecNamingService implements IPipeSpecNamingService {

    @Override
    public void setFieldNames(
            PipeSpecFile pipespecFile,
            PipeSpecSchemeFile pipespecSchema
    ) {
        setMetadataFieldNames(pipespecFile, pipespecSchema);
        setDataFieldNames(pipespecFile, pipespecSchema);
    }

    private void setMetadataFieldNames(
            PipeSpecFile pipespec,
            PipeSpecSchemeFile schema
    ) {
        List<PipeSpecSchemeMetadata> metadataSchemes = schema.getMetadataSchemes();
        for (PipeSpecSchemeMetadata metadataScheme : metadataSchemes) {
            var marker = metadataScheme.getMarker();
            PipeSpecMetadata metadata = pipespec.findMetadataByMarker(marker);

            setFieldNames(metadataScheme, metadata.getFields());
        }
    }

    private void setDataFieldNames(
            PipeSpecFile pipespec,
            PipeSpecSchemeFile schema
    ) {
        PipeSpecSchemeHeader schemeHeader = schema.getData().getHeader();
        for (PipeSpecDocument document : pipespec.getDocuments()) {
            setDocumentHeaderFieldNames(document, schemeHeader);
            setNestedTableFieldNames(document, schema.getData());
        }
    }

    private void setNestedTableFieldNames(
            PipeSpecDocument document,
            PipeSpecSchemeDocument schema
    ) {
        for (PipeSpecTable table : document.getTables()) {
            PipeSpecSchemeTable schemeTable = schema.findTableByMarker(table.getMarker());
            setFieldNames(schemeTable, table.getFields());
        }
    }

    private void setDocumentHeaderFieldNames(
            PipeSpecDocument document,
            PipeSpecSchemeHeader scheme
    ) {
        var fields = document.getHeader().getFields();
        setFieldNames(scheme, fields);
    }

    private void setFieldNames(
            IndexedScheme scheme,
            List<PipeSpecField> fields
    ) {
        for (int i = 0; i < fields.size(); i++) {
            PipeSpecField field = fields.get(i);
            PipeSpecSchemeField schemeField = scheme.findFieldByPosition(i + 1);
            field.setName(schemeField.getName());
        }
    }
}
