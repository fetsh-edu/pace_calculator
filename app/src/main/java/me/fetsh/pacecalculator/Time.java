package me.fetsh.pacecalculator;

import android.os.Parcel;
import android.os.Parcelable;

public class Time implements Parcelable {
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
    public int getSeconds(){ return seconds; }

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

    protected Time(Parcel in) {
        seconds = in.readInt();
    }

    public static final Creator<Time> CREATOR = new Creator<Time>() {
        @Override
        public Time createFromParcel(Parcel in) {
            return new Time(in);
        }

        @Override
        public Time[] newArray(int size) {
            return new Time[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(seconds);
    }
}
