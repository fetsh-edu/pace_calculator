package me.fetsh.pacecalculator;

public enum DistanceUnit {
    Meter(true), Kilometer(true), Mile(false);

    private final boolean isMetric;

    DistanceUnit(boolean isMetric) {
        this.isMetric = isMetric;
    }

    public boolean isMetric() {
        return isMetric;
    }
}
