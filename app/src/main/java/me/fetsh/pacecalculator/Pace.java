package me.fetsh.pacecalculator;

public class Pace {
    private final int seconds;

    public Pace(int min, int sec) {
        this(min * 60 + sec);
    }

    public Pace(int seconds) {
        this.seconds = seconds;
    }

    public Pace multipliedBy(double multiplicand) {
        return new Pace((int) Math.ceil(getSeconds() * multiplicand));
    }

    public int getHours() {
        return getSeconds() / 3600;
    }

    public int getMinutes() {
        return getSeconds() / 60;
    }

    public int getMinutesPart() {
        return (getSeconds() % 3600) / 60;
    }

    public int getSecondsPart() {
        return getSeconds() % 60;
    }

    public int getSeconds() {
        return seconds;
    }

    @Override
    public String toString() {
        int hours = getHours();
        int minutes = getMinutesPart();
        int lSeconds = getSecondsPart();
        if (hours > 0) {
            return String.format("%d:%02d:%02d", hours, minutes, lSeconds);
        } else {
            return String.format("%d:%02d", minutes, lSeconds);
        }
    }
}
