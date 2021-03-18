package me.fetsh.pacecalculator;

public class Time {
    private final int seconds;

    public Time(int hours, int min, int sec) {
        this(hours*60*60 + min*60 + sec);
    }

    public Time(int min, int sec) {
        this(min * 60 + sec);
    }

    public Time(int seconds) {
        this.seconds = seconds;
    }
    public int getMinutes() {
        return getSeconds() / 60;
    }
    public int getHours() {
        return getSeconds() / 3600;
    }
    public int getMinutesPart() {
        return (getSeconds() % 3600) / 60;
    }
    public int getSecondsPart() {
        return getSeconds() % 60;
    }
    public int getSeconds(){ return seconds; };

    public Time multiply(double multiplicand) {
        return new Time((int) Math.round(getSeconds() * multiplicand));
    }
    public Time divide(double divisor) {
        return new Time((int) Math.round(getSeconds() / divisor));
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
