package subway.domain.common;

import subway.exception.NameIsBlankException;

import java.util.Objects;

public class Name {

    private String name;

    public Name(final String name) {
        validateName(name);
        this.name = name;
    }

    public void edit(final String name) {
        validateName(name);
        this.name = name;
    }

    private void validateName(final String name) {
        if (name.isBlank()) {
            throw new NameIsBlankException();
        }
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof Name)) return false;
        Name name1 = (Name) o;
        return Objects.equals(name, name1.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
