package dev.abykov.pets.pipespec.serialize.impl;

import dev.abykov.pets.pipespec.models.pipespec.PipeSpecFile;
import dev.abykov.pets.pipespec.models.pipespec.data.PipeSpecDocument;
import dev.abykov.pets.pipespec.models.pipespec.data.PipeSpecHeader;
import dev.abykov.pets.pipespec.models.pipespec.data.PipeSpecTable;
import dev.abykov.pets.pipespec.models.pipespec.field.PipeSpecField;
import dev.abykov.pets.pipespec.models.pipespec.metadata.PipeSpecMetadata;
import dev.abykov.pets.pipespec.serialize.IPipeSpecSerializer;
import dev.abykov.pets.pipespec.util.CustomStringBuilder;
import org.springframework.stereotype.Component;

@Component
public class PipeSpecSerializer implements IPipeSpecSerializer {

    private static final String PIPE_SPEC_DELIMITER = "|";
    private static final String PIPE_SPEC_EOL = "\n";

    @Override
    public String serialize(PipeSpecFile pipespec) {
        var sb = new CustomStringBuilder();

        var metadataString = serializeMetadata(pipespec);
        sb.append(metadataString);

        var documentString = serializeDocuments(pipespec);
        sb.append(documentString);

        return sb.toString();
    }

    private String serializeMetadata(PipeSpecFile pipespec) {
        var sb = new CustomStringBuilder();
        for (PipeSpecMetadata metada : pipespec.getMetadata()) {
            var marker = metada.getMarker();
            sb.append(marker).append(PIPE_SPEC_DELIMITER);

            metada.getFields().stream()
                    .map(PipeSpecField::getValue)
                    .forEach(val -> sb.append(val).append(PIPE_SPEC_DELIMITER));
            sb.append(PIPE_SPEC_EOL);
        }
        return sb.toString();
    }

    private String serializeDocuments(PipeSpecFile pipespec) {
        var sb = new CustomStringBuilder();
        for (PipeSpecDocument document : pipespec.getDocuments()) {

            var headerString = serializeHeader(document);
            sb.append(headerString);

            for (PipeSpecTable table : document.getTables()) {
                var tableString = serializeTable(table);
                sb.append(tableString);
            }
        }
        return sb.toString();
    }

    private String serializeHeader(PipeSpecDocument document) {
        var sb = new CustomStringBuilder();

        PipeSpecHeader header = document.getHeader();

        var marker = header.getMarker();
        sb.append(marker).append(PIPE_SPEC_DELIMITER);

        header.getFields().stream()
                .map(PipeSpecField::getValue)
                .forEach(val -> sb.append(val).append(PIPE_SPEC_DELIMITER));
        sb.append(PIPE_SPEC_EOL);

        return sb.toString();
    }

    private String serializeTable(PipeSpecTable table) {
        var sb = new CustomStringBuilder();

        var marker = table.getMarker();
        sb.append(marker).append(PIPE_SPEC_DELIMITER);

        table.getFields().stream()
                .map(PipeSpecField::getValue)
                .forEach(val -> sb.append(val).append(PIPE_SPEC_DELIMITER));
        sb.append(PIPE_SPEC_EOL);

        return sb.toString();
    }
}
