package me.fetsh.pacecalculator;

import androidx.annotation.NonNull;

public class Calculator {

    private static final Pace INIT_PACE = new Pace(new Time(4, 30), new Distance(1, DistanceUnit.Kilometer, "km"));
    private static final Distance INIT_DISTANCE = Distance.marathon();
    private final OnDataCalculatedListener listener;

    private Pace pace;
    private Speed speed;
    private Distance distance;
    private Time time;

    public Calculator(@NonNull OnDataCalculatedListener listener) {
        this.listener = listener;
        this.distance = INIT_DISTANCE;
        calculateWith(INIT_PACE);
    }

    public void calculateWith(Pace pace){
        this.pace = pace;
        this.speed = Speed.fromPace(pace);
        this.time = pace.getTime(distance);
        notifyListener();
    }
    public void calculateWithTime(Time time) {
        pace = new Pace(time.divide(distance.divide(pace.getDistance())), pace.getDistance());
        speed = Speed.fromPace(pace);
        this.time = pace.getTime(distance);
        notifyListener();
    }
    public void calculateWithSpeed(Speed speed) {
        this.speed = speed;
        pace = new Pace(new Time((int) Math.round(3600d / speed.getSpeed())), speed.getDistance());
        this.time = pace.getTime(distance);
        notifyListener();
    }
    private void notifyListener() {
        listener.onDataCalculated(this);
    }

    public Pace getPace() {
        return pace;
    }

    public Speed getSpeed() {
        return speed;
    }

    public Distance getDistance() {
        return distance;
    }

    public Time getTime() {
        return time;
    }
}
