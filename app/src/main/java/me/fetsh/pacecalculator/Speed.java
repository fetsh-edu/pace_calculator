package me.fetsh.pacecalculator;

import java.util.Locale;

public class Speed {
    private final double speed;
    private final Distance distance;

    public Speed(double speed, Distance distance) {
        this.speed = speed;
        this.distance = distance;
    }

    public static Speed fromPace(Pace pace) {
        return new Speed(60d/(pace.getMinutesPart()*60+pace.getSecondsPart())*60, pace.getDistance());
    }

    @Override
    public String toString() {
        return String.format(Locale.US,"%.2f", speed);
    }

    public Distance getDistance() {
        return distance;
    }

    public double getSpeed() {
        return speed;
    }
}
