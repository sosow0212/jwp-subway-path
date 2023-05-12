package subway.entity;

import java.util.Objects;

public class LineEntity {

    private final Long lineId;
    private final long lineNumber;
    private final String name;
    private final String color;

    public LineEntity(final Long lineId, final long lineNumber, final String name, final String color) {
        this.lineId = lineId;
        this.lineNumber = lineNumber;
        this.name = name;
        this.color = color;
    }

    public Long getLineId() {
        return lineId;
    }

    public long getLineNumber() {
        return lineNumber;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof LineEntity)) return false;
        LineEntity that = (LineEntity) o;
        return Objects.equals(lineId, that.lineId) && Objects.equals(name, that.name) && Objects.equals(color, that.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lineId, name, color);
    }
}
