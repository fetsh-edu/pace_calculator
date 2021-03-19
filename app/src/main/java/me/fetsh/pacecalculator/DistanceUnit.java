package me.fetsh.pacecalculator;

public enum DistanceUnit {
    Meter(true, "m"), Kilometer(true, "km"), Mile(false, "mi");

    private final boolean isMetric;
    private final String shortName;

    DistanceUnit(boolean isMetric, String shortName) {
        this.isMetric = isMetric;
        this.shortName = shortName;
    }

    public String getShortName() { return shortName; }
    public boolean isMetric() {
        return isMetric;
    }
}
