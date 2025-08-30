package dev.abykov.pets.pipespec.parsers.scheme.impl;

import dev.abykov.pets.pipespec.models.scheme.PipeSpecSchemeFile;
import dev.abykov.pets.pipespec.models.scheme.data.PipeSpecSchemeDocument;
import dev.abykov.pets.pipespec.models.scheme.data.PipeSpecSchemeHeader;
import dev.abykov.pets.pipespec.models.scheme.data.PipeSpecSchemeTable;
import dev.abykov.pets.pipespec.models.scheme.field.PipeSpecSchemeField;
import dev.abykov.pets.pipespec.models.scheme.field.PipeSpecSchemeFieldValidator;
import dev.abykov.pets.pipespec.models.scheme.metadata.PipeSpecSchemeMetadata;
import dev.abykov.pets.pipespec.parsers.scheme.IPipeSpecSchemeParser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

@Component
public class PipeSpecSchemeParser extends DefaultHandler implements IPipeSpecSchemeParser {

    private PipeSpecSchemeFile scheme;
    private PipeSpecSchemeMetadata metadata;

    private PipeSpecSchemeDocument document;
    private PipeSpecSchemeHeader header;
    private PipeSpecSchemeTable table;

    private PipeSpecSchemeFieldValidator validator;
    private PipeSpecSchemeField field;

    private boolean insideMetadata;
    private boolean insideDataHeader;
    private boolean insideNestedTable;

    @Override
    public PipeSpecSchemeFile parse(File file) {
        try (InputStream is = new FileInputStream(file)) {
            return parse(is);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public PipeSpecSchemeFile parse(InputStream inputStream) {
        scheme = new PipeSpecSchemeFile();

        try {
            newSaxParser().parse(inputStream, this);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return scheme;
    }

    @Override
    public void startElement(
            String uri,
            String localName,
            String qName,
            Attributes attributes
    ) {
        if (StringUtils.equals(qName, "ps_spec")) {
            var version = attributes.getValue("version");
            scheme.setVersion(version);

            var marker = attributes.getValue("marker");
            scheme.setMarker(marker);
        }

        if (StringUtils.equals(qName, "section")) {
            insideMetadata = true;

            metadata = new PipeSpecSchemeMetadata();
            metadata.setMarker(attributes.getValue("marker"));
        }

        if (StringUtils.equals(qName, "data")) {
            document = new PipeSpecSchemeDocument();
        }

        if (StringUtils.equals(qName, "header")) {
            insideDataHeader = true;
            header = new PipeSpecSchemeHeader();
        }

        if (StringUtils.equals(qName, "table")) {
            insideNestedTable = true;

            table = new PipeSpecSchemeTable();
            table.setMarker(attributes.getValue("marker"));
        }

        if (StringUtils.equals(qName, "validator")) {
            validator = new PipeSpecSchemeFieldValidator();

            var type = attributes.getValue("type");
            validator.setType(type);

            Double length = Double.parseDouble(attributes.getValue("length"));
            validator.setLength(length);

            var condition = attributes.getValue("condition");
            validator.setCondition(condition);
        }

        if (StringUtils.equals(qName, "field")) {
            startFieldsHarvesting(attributes);
        }
    }

    @Override
    public void endElement(
            String uri,
            String localName,
            String qName
    ) {
        if (StringUtils.equals(qName, "validator")) {
            field.setValidator(validator);
        }

        if (StringUtils.equals(qName, "field")) {
            endFieldsHarvesting();
        }

        if (StringUtils.equals(qName, "section")) {
            insideMetadata = false;
            scheme.getMetadataSchemes().add(metadata);
        }

        if (StringUtils.equals(qName, "header")) {
            insideDataHeader = false;
            document.setHeader(header);
        }

        if (StringUtils.equals(qName, "table")) {
            insideNestedTable = false;
            document.getTables().add(table);
        }

        if (StringUtils.equals(qName, "data")) {
            scheme.setData(document);
        }
    }

    private void startFieldsHarvesting(Attributes attributes) {
        if (List.of(insideMetadata, insideDataHeader, insideNestedTable)
                .contains(Boolean.TRUE)) {
            grabField(attributes);
        }
    }

    private void endFieldsHarvesting() {
        if (insideMetadata) {
            metadata.getFields().add(field);
        }

        if (insideDataHeader) {
            header.getFields().add(field);
        }

        if (insideNestedTable) {
            table.getFields().add(field);
        }
    }

    private void grabField(Attributes attributes) {
        field = new PipeSpecSchemeField();

        var name = attributes.getValue("name");
        field.setName(name);

        var position = Integer.parseInt(attributes.getValue("position"));
        field.setPosition(position);

        var mandatory = Boolean.parseBoolean(attributes.getValue("mandatory"));
        field.setMandatory(mandatory);
    }

    private SAXParser newSaxParser()
            throws ParserConfigurationException, SAXException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setValidating(true);

        return factory.newSAXParser();
    }
}
