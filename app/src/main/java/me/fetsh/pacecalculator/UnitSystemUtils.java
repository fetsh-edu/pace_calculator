package me.fetsh.pacecalculator;

public class UnitSystemUtils {

    public static int getPaceHeader(UnitSystem unitSystem) {
        switch (unitSystem) {
            case Imperial: return R.string.min_per_mile;
            case Metric: return R.string.min_per_km;
            default: return -1;
        }
    }

    public static int getSpeedHeader(UnitSystem unitSystem) {
        switch (unitSystem) {
            case Imperial: return R.string.miles_per_hour;
            case Metric: return R.string.km_per_hour;
            default: return -1;
        }
    }
}
