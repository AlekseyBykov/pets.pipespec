package dev.abykov.pets.pipespec.validate.pipespec.impl;

import dev.abykov.pets.pipespec.exceptions.PipeSpecFileValidationException;
import dev.abykov.pets.pipespec.models.pipespec.PipeSpecFile;
import dev.abykov.pets.pipespec.models.pipespec.data.PipeSpecDocument;
import dev.abykov.pets.pipespec.models.pipespec.data.PipeSpecHeader;
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
import dev.abykov.pets.pipespec.validate.pipespec.IPipeSpecFileValidator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.IntStream;

@Component
public class PipeSpecFileValidator implements IPipeSpecFileValidator {

    @Override
    public void validate(PipeSpecFile pipespec, PipeSpecSchemeFile scheme) {
        validateMetadata(pipespec, scheme);
        validateData(pipespec, scheme);
    }

    private void validateMetadata(
            PipeSpecFile pipespec,
            PipeSpecSchemeFile scheme
    ) {
        for (PipeSpecSchemeMetadata metadataScheme : scheme.getMetadataSchemes()) {
            var sectionMarker = metadataScheme.getMarker();
            PipeSpecMetadata metadata = pipespec.findMetadataByMarker(sectionMarker);

            List<PipeSpecField> pipespecFields = metadata.getFields();

            validateFieldsNumber(metadataScheme.getFields(), pipespecFields);
            validateFieldValues(metadataScheme, pipespecFields);
        }
    }

    private void validateData(PipeSpecFile pipespec, PipeSpecSchemeFile scheme) {
        PipeSpecSchemeDocument dataScheme = scheme.getData();
        PipeSpecSchemeHeader headerScheme = dataScheme.getHeader();
        pipespec.getDocuments().forEach(document -> {
            validateDocument(document, headerScheme);
            validateNestedTable(document, dataScheme);
        });
    }

    private void validateDocument(
            PipeSpecDocument document,
            PipeSpecSchemeHeader headerScheme
    ) {
        PipeSpecHeader header = document.getHeader();
        List<PipeSpecField> fields = header.getFields();

        IntStream.range(0, fields.size())
                .forEach(i ->
                        validateFieldValue(fields.get(i), headerScheme, i)
                );
    }

    private void validateNestedTable(
            PipeSpecDocument document,
            PipeSpecSchemeDocument dataScheme
    ) {
        for (PipeSpecTable table : document.getTables()) {
            var tableMarker = table.getMarker();
            PipeSpecSchemeTable tableScheme = dataScheme.findTableByMarker(tableMarker);

            List<PipeSpecField> tableFields = table.getFields();
            IntStream.range(0, tableFields.size())
                    .forEach(i ->
                            validateFieldValue(tableFields.get(i), tableScheme, i)
                    );
        }
    }

    private void validateFieldValues(
            PipeSpecSchemeMetadata metadataScheme,
            List<PipeSpecField> pipespecFields
    ) {
        IntStream.range(0, pipespecFields.size())
                .forEach(i ->
                        validateFieldValue(pipespecFields.get(i), metadataScheme, i)
                );
    }

    private void validateFieldValue(
            PipeSpecField field,
            IndexedScheme scheme,
            int fieldPos
    ) {
        PipeSpecSchemeField fieldScheme = scheme.findFieldByPosition(fieldPos + 1);

        checkFieldMandatory(field, fieldScheme);
        checkFieldDataType(field, fieldScheme);
    }

    private void checkFieldMandatory(
            PipeSpecField field,
            PipeSpecSchemeField fieldScheme
    ) {
        var val = field.getValue();
        if (fieldScheme.isMandatory() && StringUtils.isEmpty(val)) {
            throw PipeSpecFileValidationException.mandatoryFieldIsEmpty(fieldScheme.getName());
        }
    }

    private <T, U> void validateFieldsNumber(List<T> tList, List<U> uList) {
        if (tList.size() != uList.size()) {
            throw PipeSpecFileValidationException.wrongFieldsNumber();
        }
    }

    private void checkFieldDataType(
            PipeSpecField field,
            PipeSpecSchemeField fieldScheme
    ) {
        var val = field.getValue();
        if (StringUtils.isNotEmpty(val)) {
            var validator = fieldScheme.getValidator();
            Class<?> clazz = validator.getType();
            if (!val.getClass().equals(clazz)) {
                throw PipeSpecFileValidationException.wrongDataType(fieldScheme.getName());
            }
        }
    }
}
