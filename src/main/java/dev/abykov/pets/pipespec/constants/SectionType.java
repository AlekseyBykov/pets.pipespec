package dev.abykov.pets.pipespec.constants;

public enum SectionType {

    HDR("HDR"),
    SRC("SRC"),
    DST("DST"),
    CLASS("CLASS");

    private final String literal;

    SectionType(String literal) {
        this.literal = literal;
    }

    public String getLiteral() {
        return literal;
    }
}
