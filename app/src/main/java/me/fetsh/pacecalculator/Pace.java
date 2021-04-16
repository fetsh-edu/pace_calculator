package me.fetsh.pacecalculator;

import android.os.Parcel;
import android.os.Parcelable;

public class Pace implements Parcelable {
    private final Time time;
    private final Distance distance;

    public static final Pace INITIAL = new Pace(new Time(4, 30), new Distance(1, DistanceUnit.Kilometer, "km"));

    public Pace(Time time, Distance distance) {
        this.time = time;
        this.distance = distance;
    }

    public Pace convertWithDistance(Distance distance){
        if (this.distance.equals(distance)) return this;
        return new Pace(getTime(distance), distance);
    }
    public Pace convertWithDistanceAndTime(Distance distance, Time time) {
        return new Pace(time.divide(distance.divide(this.distance)), this.distance);
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

        return "min/" + distance.getSplitDistanceHeader();
    }

    @Override
    public String toString() {
        return time.toString();
    }


    protected Pace(Parcel in) {
        this.time = in.readParcelable(Time.class.getClassLoader());
        this.distance = in.readParcelable(Distance.class.getClassLoader());
    }

    public static final Creator<Pace> CREATOR = new Creator<Pace>() {
        @Override
        public Pace createFromParcel(Parcel in) {
            return new Pace(in);
        }

        @Override
        public Pace[] newArray(int size) {
            return new Pace[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(time, flags);
        dest.writeParcelable(distance, flags);
    }
}