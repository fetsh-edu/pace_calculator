package me.fetsh.pacecalculator;

import androidx.annotation.NonNull;

public class Calculator {

    private static final Pace INIT_PACE = Pace.metric(4, 30);
    private static final Distance INIT_DISTANCE = Distance.NamedDistance.getMarathon();
    private final OnDataCalculatedListener listener;

    private Pace pace;
    private Speed speed;
    private Distance distance;
    private Pace time;

    public Calculator(@NonNull OnDataCalculatedListener listener) {
        this.listener = listener;
        this.distance = INIT_DISTANCE;
        calculateWith(INIT_PACE);
    }

    public void calculateWith(Pace pace){
        this.pace = pace;
        this.speed = Speed.fromPace(pace);
        this.time = pace.multipliedBy(distance.getDistance());
        notifyListener();
    }
    public void calculateWithTime(Pace time) {
        this.pace = time.dividedBy(distance.getDistance());
        this.speed = Speed.fromPace(pace);
        this.time = pace.multipliedBy(distance.getDistance());
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

    public Pace getTime() {
        return time;
    }

}
