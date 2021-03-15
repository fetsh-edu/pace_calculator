package me.fetsh.pacecalculator;

import java.util.Locale;

public class Speed {
    private final double speed;


    public Speed(double speed) {
        this.speed = speed;
    }

    public static Speed fromPace(Pace pace) {
        // pace 3:45
        return new Speed(60d/(pace.getMinutesPart()*60+pace.getSecondsPart())*60);
    }

    @Override
    public String toString() {
        return String.format(Locale.US,"%.2f", speed);
    }
}
