package me.fetsh.pacecalculator;

public class Pace {
    private final Time time;
    private final Distance distance;

    public Pace(Time time, Distance distance) {
        this.time = time;
        this.distance = distance;
    }

    public Pace convertWithDistance(Distance distance){
        if (this.distance.equals(distance)) return this;
        return new Pace(getTime(distance), distance);
    }

    public Time getTime() {
        return time;
    }

    public Time getTime(Distance distance) {
        return time.multiply(distance.divide(this.distance));
    }

    public Distance getDistance() {
        return distance;
    }

    public int getMinutes() { return time.getMinutes(); }
    public int getHours() { return time.getHours(); }
    public int getMinutesPart() { return time.getMinutesPart(); }
    public int getSecondsPart() { return time.getSecondsPart(); }
    public int getSeconds(){ return time.getSeconds(); }

    public String getPostfixString() {
        String result = "min/" + distance.getPostfixString();

        return result;
    }

    @Override
    public String toString() {
        return time.toString();
    }
}