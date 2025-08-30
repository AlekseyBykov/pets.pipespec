# pets.pipespec

**Delimited Text File Parser with XML-driven Schema Validation**

This project provides a parser and validator for structured text files with delimiter-separated values (pipe `|`).
The parsing rules and validation logic are defined by an XML schema, which describes the file structure, sections,
fields, types, and constraints.

## **Architecture**

This project follows a classical **ETL (Extract-Transform-Load)** pattern used in system integration:

- **Extract** — read a flat, delimiter-separated file (`|`) provided by an external system.
- **Transform** — validate the content against an XML-driven schema and map values into a structured object model.
- **Load** — the parsed and validated data can be further processed, stored, or integrated into other systems.

The module ensures that external flat files are validated and consistently transformed into domain objects,
making integration tasks reliable and maintainable.

## **Features**

- Parse delimited text files according to an XML specification
- Map values to a structured object model:
    - `PipeSpecFile` → contains documents
    - `PipeSpecDocument` → contains header and tables
    - `PipeSpecMetadata` (sections like HDR, SRC, DST, CLASS)
    - `PipeSpecTable` (rows of business data)
- Validate values against schema constraints:
    - type (`string`, `integer`, `date`, `guid`, `double`)
    - length
    - required/optional fields
- Serialize object model back into plain text format
- Unit tests included (`JUnit 5`, `Spring Test`)

## **Example**

### XML Schema (fragment)
```xml
<section marker="HDR" mandatory="true" description="File header">
    <fields>
        <field name="H1" position="1" mandatory="true" description="Format version">
            <validator type="string" length="10" condition="ltoreq"/>
        </field>
        <field name="H2" position="2" mandatory="false" description="Producer system name">
            <validator type="string" length="50" condition="ltoreq"/>
        </field>
    </fields>
</section>
```

### Input file
```text
HDR|PSPDN180101|123|6.01||
SRC|1|12319101|321|
DST|0200|123|
CLASS|0||
PDLINE|1|6F9619FF-8B86-D011-B42D-00C04FC964F1|ZR|12|015|12.05.2021|56|4501011121|450101001|88700000|18301171200001001112|10||5000.00|888|||
```
### Java usage
```java
MultipartFile file = ResourceFileLoader.loadMultipartFile(
        "dev/abykov/pets/pipespec/pipespec_files",
        "PSPDN180101.EX1"
);

PipeSpecProcessor processor = new PipeSpecProcessor(
        new PipeSpecSchemeParser(),
        new PipeSpecFileParser(),
        new PipeSpecFileValidator(),
        new PipeSpecNamingService(),
        new PipeSpecSerializer()
);

PipeSpecFile parsed = processor.process(file, "PDN", "PSPDN180101.xml");
```

## **Tech stack**

- Java 21
- Spring Framework (context, web, test)
- Apache Commons Lang3 & Collections4
- JUnit 5

## **Motivation**

The parser was designed for integration tasks where external systems provide structured data files in plain text format.
Instead of hardcoding the parsing logic, an XML-driven schema allows flexible definition of fields and validation rules.

## License
MIT License
