package io.cdap.wrangler.api.parser;

public class TimeDuration extends Token {
    private final double value;
    private final String unit;

    public TimeDuration(String value) {
        super(value);
        this.unit = value.replaceAll("[0-9.]", "").toLowerCase();
        this.value = Double.parseDouble(value.replaceAll("[^0-9.]", ""));
    }

    public long getMilliseconds() {
        switch (unit) {
            case "s": return (long) (value * 1000);
            case "ns": return (long) (value / 1_000_000);
            default: return (long) value;
        }
    }
}