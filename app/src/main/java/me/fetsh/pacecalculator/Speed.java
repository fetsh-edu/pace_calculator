package me.fetsh.pacecalculator;

import java.util.Locale;

public class Speed {
    private final double speed;
    private final UnitSystem unitSystem;

    public Speed(double speed, UnitSystem unitSystem) {
        this.speed = speed;
        this.unitSystem = unitSystem;
    }

    public static Speed fromPace(Pace pace) {
        return new Speed(60d/(pace.getMinutesPart()*60+pace.getSecondsPart())*60, pace.getUnitSystem());
    }

    @Override
    public String toString() {
        return String.format(Locale.US,"%.2f", speed);
    }

    public UnitSystem getUnitSystem() {
        return unitSystem;
    }
}
