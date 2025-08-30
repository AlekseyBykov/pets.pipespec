package dev.abykov.pets.pipespec.parsers.pipespec.impl;

import dev.abykov.pets.pipespec.constants.SectionType;
import dev.abykov.pets.pipespec.exceptions.PipeSpecFileParseException;
import dev.abykov.pets.pipespec.models.pipespec.PipeSpecFile;
import dev.abykov.pets.pipespec.models.pipespec.data.PipeSpecDocument;
import dev.abykov.pets.pipespec.models.pipespec.data.PipeSpecHeader;
import dev.abykov.pets.pipespec.models.pipespec.data.PipeSpecTable;
import dev.abykov.pets.pipespec.models.pipespec.metadata.PipeSpecMetadata;
import dev.abykov.pets.pipespec.parsers.pipespec.IPipeSpecFileParser;
import dev.abykov.pets.pipespec.util.PipeSpecSplitter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

@Component
public class PipeSpecFileParser implements IPipeSpecFileParser {

    private static final String PIPESPEC_ENCODING = "UTF-8";

    @Override
    public PipeSpecFile parse(File file, String documentMarker) {
        try (InputStream is = new FileInputStream(file)) {
            return parse(is, documentMarker);
        } catch (Exception e) {
            throw new PipeSpecFileParseException(e.getMessage());
        }
    }

    @Override
    public PipeSpecFile parse(
            InputStream inputStream,
            String documentMarker
    ) {
        PipeSpecFile pipespec = new PipeSpecFile();

        PipeSpecDocument currentDocument = null;

        pipespec.setMarker(documentMarker);

        int markerPos = 0;
        int versionPos = 1;

        Scanner scanner = newScanner(inputStream);
        while (scanner.hasNextLine()) {
            var section = scanner.nextLine();
            var sectionFields = Arrays.asList(PipeSpecSplitter.split(section));
            var rowMarker = sectionFields.get(markerPos);

            if (StringUtils.equals(SectionType.HDR.getLiteral(), rowMarker)) {
                pipespec.setVersion(sectionFields.get(versionPos));
            }

            if (Arrays.asList(SectionType.HDR.getLiteral(), SectionType.SRC.getLiteral(),
                    SectionType.DST.getLiteral(), SectionType.CLASS.getLiteral()
            ).contains(rowMarker)) {
                PipeSpecMetadata metadata = PipeSpecMetadata.newMetadata(sectionFields, rowMarker);
                pipespec.getMetadata().add(metadata);

            } else if (StringUtils.equals(documentMarker, rowMarker)) {
                currentDocument = new PipeSpecDocument();
                addPipescepHeader(currentDocument, sectionFields, documentMarker);

                pipespec.getDocuments().add(currentDocument);
            } else {
                if (currentDocument == null) {
                    currentDocument = new PipeSpecDocument();
                    pipespec.getDocuments().add(currentDocument);
                }
                PipeSpecTable table = PipeSpecTable.newTable(sectionFields, rowMarker);
                currentDocument.getTables().add(table);
            }
        }
        return pipespec;
    }

    private Scanner newScanner(InputStream pipespecInputStream) {
        return new Scanner(pipespecInputStream, Charset.forName(PIPESPEC_ENCODING));
    }

    private void addPipescepHeader(
            PipeSpecDocument document,
            List<String> fields,
            String marker
    ) {
        PipeSpecHeader header = PipeSpecHeader.newHeader(fields, marker);
        document.setHeader(header);
    }
}
