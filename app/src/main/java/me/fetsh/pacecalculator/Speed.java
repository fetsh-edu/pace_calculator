package me.fetsh.pacecalculator;

import java.util.Locale;

public class Speed {
    private final double speed;
    private final Distance distance;
    public static final double INIT_SPEED = 14.06;

    public Speed(double speed, Distance distance) {
        this.speed = speed;
        this.distance = distance;
    }

    public static Speed fromPace(Pace pace) {
        Pace newPace = pace.convertWithDistance(pace.getDistance().setAmount(1));
        return new Speed(3600d/newPace.getSeconds(), newPace.getDistance());
    }

    @Override
    public String toString() {
        return String.format(Locale.US,"%.2f", speed);
    }
    public String getPostfixString() {
        if (distance.getUnit().isMetric()) {
            return "km/h";
        } else {
            return "mph";
        }
    }

    public Distance getDistance() {
        return distance;
    }

    public double getSpeed() {
        return speed;
    }
}
