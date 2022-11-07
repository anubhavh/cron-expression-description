package cronExpression.enums;

public enum OutputFieldName {
    MINUTE ("minute", 0, 59),
    HOUR ("hour", 0, 23),
    DAY_OF_MONTH ("day of month", 1, 31),
    MONTH ("month", 1, 12),
    DAY_OF_WEEK ("day of week", 0, 6);

    private final String name;
    private final int begin;
    private final int end;

    OutputFieldName(String name, int begin, int end) {
        this.name = name;
        this.begin = begin;
        this.end = end;
    }

    public String getName() {
        return this.name;
    }

    public int getBegin() {
        return this.begin;
    }

    public int getEnd() {
        return this.end;
    }
}
