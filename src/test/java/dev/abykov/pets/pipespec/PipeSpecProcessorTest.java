package dev.abykov.pets.pipespec;

import dev.abykov.pets.pipespec.constants.SectionType;
import dev.abykov.pets.pipespec.models.pipespec.PipeSpecFile;
import dev.abykov.pets.pipespec.models.pipespec.data.PipeSpecDocument;
import dev.abykov.pets.pipespec.models.pipespec.data.PipeSpecHeader;
import dev.abykov.pets.pipespec.models.pipespec.data.PipeSpecTable;
import dev.abykov.pets.pipespec.models.pipespec.field.PipeSpecField;
import dev.abykov.pets.pipespec.models.pipespec.metadata.PipeSpecMetadata;
import dev.abykov.pets.pipespec.naming.impl.PipeSpecNamingService;
import dev.abykov.pets.pipespec.parsers.pipespec.impl.PipeSpecFileParser;
import dev.abykov.pets.pipespec.parsers.scheme.impl.PipeSpecSchemeParser;
import dev.abykov.pets.pipespec.serialize.impl.PipeSpecSerializer;
import dev.abykov.pets.pipespec.test_utils.ResourceFileLoader;
import dev.abykov.pets.pipespec.validate.pipespec.impl.PipeSpecFileValidator;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PipeSpecProcessorTest {

    private static final String PIPESPEC_ROOT_PATH = "dev/abykov/pets/pipespec/pipespec_files";

    private static final String PIPESPEC_SCHEME_NAME = "PSPDN180101.xml";
    private static final String PIPESPEC_FILE_NAME = "PSPDN180101.EX1";

    private static IPipeSpecProcessor pipespecProcessor;

    @BeforeAll
    public static void init() {
        pipespecProcessor = new PipeSpecProcessor(
                new PipeSpecSchemeParser(),
                new PipeSpecFileParser(),
                new PipeSpecFileValidator(),
                new PipeSpecNamingService(),
                new PipeSpecSerializer()
        );
    }

    @Test
    public void testParsePipespecTextFileAgainstXmlScheme() throws IOException {
        MultipartFile file = ResourceFileLoader.loadMultipartFile(
                PIPESPEC_ROOT_PATH,
                PIPESPEC_FILE_NAME
        );

        PipeSpecFile pipespec = pipespecProcessor.process(file, "PDN", PIPESPEC_SCHEME_NAME);

        testMarker(pipespec);
        testVersion(pipespec);

        testDocumentsCount(pipespec);
        testTablesCount(pipespec);

        testMetadata(pipespec);
    }

    private void testMarker(PipeSpecFile pipespec) {
        assertEquals("PDN", pipespec.getMarker());
    }

    private void testVersion(PipeSpecFile pipespec) {
        assertEquals("PSPDN180101", pipespec.getVersion());
    }

    private void testDocumentsCount(PipeSpecFile pipespec) {
        List<PipeSpecDocument> documents = pipespec.getDocuments();
        assertEquals(1, documents.size());
    }

    private void testTablesCount(PipeSpecFile pipespec) {
        List<PipeSpecDocument> documents = pipespec.getDocuments();

        PipeSpecDocument firstDocument = documents.getFirst();
        List<PipeSpecTable> firstDocumentTables = firstDocument.getTables();
        assertEquals(1, firstDocumentTables.size());

        PipeSpecDocument lastDocument = documents.getLast();
        List<PipeSpecTable> lastDocumentTables = lastDocument.getTables();
        assertEquals(1, lastDocumentTables.size());
    }

    private void testMetadata(PipeSpecFile pipespec) {
        testHdrSection(pipespec);
        testSrcSection(pipespec);
        testDstSection(pipespec);
        testClassSection(pipespec);
    }

    private void testHdrSection(PipeSpecFile pipespec) {
        PipeSpecMetadata hdr = pipespec.findMetadataByMarker(SectionType.HDR.getLiteral());
        assertEquals(hdr.getMarker(), SectionType.HDR.getLiteral());

        List<PipeSpecField> fields = hdr.getFields();
        assertEquals(4, fields.size());

        assertEquals("PSPDN180101", hdr.getFieldValueByName("H1"));
        assertEquals("123", hdr.getFieldValueByName("H2"));
        assertEquals("6.01", hdr.getFieldValueByName("H3"));
        assertEquals(StringUtils.EMPTY, hdr.getFieldValueByName("H4"));
    }

    private void testSrcSection(PipeSpecFile pipespec) {
        PipeSpecMetadata src = pipespec.findMetadataByMarker(SectionType.SRC.getLiteral());
        assertEquals(src.getMarker(), SectionType.SRC.getLiteral());

        List<PipeSpecField> fields = src.getFields();
        assertEquals(3, fields.size());

        assertEquals("1", src.getFieldValueByName("S1"));
        assertEquals("12319101", src.getFieldValueByName("S2"));
        assertEquals("321", src.getFieldValueByName("S3"));
    }

    private void testDstSection(PipeSpecFile pipespec) {
        PipeSpecMetadata dst = pipespec.findMetadataByMarker(SectionType.DST.getLiteral());
        assertEquals(dst.getMarker(), SectionType.DST.getLiteral());

        List<PipeSpecField> fields = dst.getFields();
        assertEquals(2, fields.size());

        assertEquals("0200", dst.getFieldValueByName("D1"));
        assertEquals("123", dst.getFieldValueByName("D2"));
    }

    private void testClassSection(PipeSpecFile pipespec) {
        PipeSpecMetadata cls = pipespec.findMetadataByMarker(SectionType.CLASS.getLiteral());
        assertEquals(cls.getMarker(), SectionType.CLASS.getLiteral());

        List<PipeSpecField> fields = cls.getFields();
        assertEquals(2, fields.size());

        assertEquals("0", cls.getFieldValueByName("C1"));
        assertEquals(StringUtils.EMPTY, cls.getFieldValueByName("C2"));
    }

    @Test
    public void testConvertPipespecToPlainStringAgainstXmlScheme() {
        PipeSpecFile pipeSpecFile = new PipeSpecFile();

        pipeSpecFile.setMarker("PDN");
        pipeSpecFile.setVersion("PSPDN180101");

        // HDR
        pipeSpecFile.getMetadata().add(
                PipeSpecMetadata.builder()
                        .marker("HDR")
                        .field("H1", "PSPDN180101")
                        .field("H2", "123")
                        .field("H3", "6.01В")
                        .field("H4", "")
                        .build()
        );
        // SRC
        pipeSpecFile.getMetadata().add(
                PipeSpecMetadata.builder()
                        .marker("SRC")
                        .field("S1", "1")
                        .field("S2", "12319101")
                        .field("S3", "321")
                        .build()
        );
        // DST
        pipeSpecFile.getMetadata().add(
                PipeSpecMetadata.builder()
                        .marker("DST")
                        .field("D1", "0200")
                        .field("D2", "123")
                        .build()
        );
        // CLASS
        pipeSpecFile.getMetadata().add(
                PipeSpecMetadata.builder()
                        .marker("CLASS")
                        .field("C1", "0")
                        .field("C2", "")
                        .build()
        );

        PipeSpecTable firstPdLineTable = newPipeSpecTable();

        PipeSpecDocument firstDocument = newPipeSpecDocument();
        firstDocument.getTables().add(firstPdLineTable);

        pipeSpecFile.getDocuments().add(firstDocument);

        var plain = pipespecProcessor.toPlainText(pipeSpecFile);

        String[] lines = plain.split("\\R");
        assertTrue(lines.length >= 5, "plain text should contain metadata lines and at least one table line");

        assertEquals("HDR|PSPDN180101|123|6.01В||", lines[0]);
        assertEquals("SRC|1|12319101|321|", lines[1]);
        assertEquals("DST|0200|123|", lines[2]);
        assertEquals("CLASS|0||", lines[3]);

        assertTrue(plain.contains("PDLINE|"), "PDLINE row should be present");
    }

    private PipeSpecDocument newPipeSpecDocument() {
        return new PipeSpecDocument(newPipespecHeader());
    }

    private PipeSpecHeader newPipespecHeader() {
        return PipeSpecHeader.builder()
                .marker("PDN")
                .field("HD1", "")
                .field("HD2", "15")
                .field("HD3", "12.06.2021")
                .field("HD4", "12-23")
                .field("HD5", "02119101")
                .field("HD6", "03111911010")
                .field("HD7", "123")
                .field("HD8", "101")
                .field("HD9", "111")
                .field("HD10", "222")
                .field("HD11", "12345678")
                .field("HD12", "")
                .field("HD13", "333")
                .field("HD14", "0200")
                .field("HD15", "1")
                .field("HD16", "")
                .field("HD17", "11.06.2021")
                .field("HD18", "444")
                .field("HD19", "4501011121")
                .field("HD20", "450101001")
                .field("HD21", "")
                .field("HD22", "")
                .field("HD23", "555")
                .field("HD24", "66")
                .field("HD25", "77")
                .field("HD26", "88")
                .field("HD27", "999-54-23")
                .field("HD28", "11.06.2021")
                .field("HD29", "")
                .field("HD30", "")
                .field("HD31", "")
                .field("HD32", "")
                .field("HD33", "")
                .field("HD34", "")
                .build();
    }

    private PipeSpecTable newPipeSpecTable() {
        return PipeSpecTable.builder()
                .marker("PDLINE")
                .field("T1", "1")
                .field("T2", "6F9619FF-8B86-D011-B42D-00C04FC964F1")
                .field("T3", "ZR")
                .field("T4", "12")
                .field("T5", "015")
                .field("T6", "12.05.2021")
                .field("T7", "56")
                .field("T8", "4501011121")
                .field("T9", "450101001")
                .field("T10", "88700000")
                .field("T11", "18301171200001001112")
                .field("T12", "10")
                .field("T13", "")
                .field("T14", "5000.00")
                .field("T15", "888")
                .field("T16", "")
                .field("T17", "")
                .field("T18", "")
                .build();
    }
}
