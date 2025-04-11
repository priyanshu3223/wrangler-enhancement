package io.cdap.wrangler.api.parser;

public class ByteSize extends Token {
    private final double value;
    private final String unit;

    public ByteSize(String value) {
        super(value);
        this.unit = value.replaceAll("[0-9.]", "").toUpperCase();
        this.value = Double.parseDouble(value.replaceAll("[^0-9.]", ""));
    }

    public long getBytes() {
        switch (unit) {
            case "KB": return (long) (value * 1024);
            case "MB": return (long) (value * 1024 * 1024);
            case "GB": return (long) (value * 1024 * 1024 * 1024);
            default: return (long) value;
        }
    }
}