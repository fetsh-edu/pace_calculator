package me.fetsh.pacecalculator;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.annotations.JsonAdapter;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Locale;

@JsonAdapter(Time.Deserializer.class)
public class Time implements Parcelable {

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Time.class, new Serializer())
            .registerTypeAdapter(Time.class, new Time.Deserializer())
            .create();

    public static final Time INITIAL = fromSeconds(4 * 60 + 30, Precision.Seconds);

    private final BigDecimal milliseconds;
    public final Precision precision;

    public static Time fromSeconds(int seconds, Precision precision) {
        return new Time(seconds * 1000, precision);
    }
    public static Time fromDeciSeconds(int deciSeconds, Precision precision) {
        return new Time(deciSeconds * 100, precision);
    }
    public static Time fromCentiSeconds(int centiSeconds, Precision precision) {
        return new Time(centiSeconds * 10, precision);
    }
    public static Time fromMilliseconds(int milliseconds, Precision precision) {
        return new Time(milliseconds, precision);
    }
    private Time(int milliseconds, Precision precision) {
        this.milliseconds = new BigDecimal(milliseconds).movePointLeft(3);
        this.precision = precision;
    }
    private Time(BigDecimal bigDecimal, Precision precision) {
        this.milliseconds = bigDecimal.setScale(3, RoundingMode.HALF_UP);
        this.precision = precision;
    }

    public static Time fromJson(String json) {
        return gson.fromJson(json, Time.class);
    }

    public String toJson() { return gson.toJson(this); }
    public JsonElement toJsonTree() {
        return gson.toJsonTree(this);
    }

    private static class Serializer implements JsonSerializer<Time> {

        @Override
        public JsonElement serialize(Time time, Type type,
                                     JsonSerializationContext jsonSerializationContext) {

            JsonObject paceJsonObj = new JsonObject();

            paceJsonObj.addProperty("milliseconds", time.milliseconds.toPlainString());
            paceJsonObj.addProperty("precision", time.precision.ordinal());

            return paceJsonObj;
        }
    }

    public static class Deserializer implements JsonDeserializer<Time> {

        @Override
        public Time deserialize(JsonElement json, Type type,
                                JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {

            JsonObject jsonObject = json.getAsJsonObject();

            JsonElement jsonTime = jsonObject.get("milliseconds");
            BigDecimal time;
            if (jsonTime == null) {
                time = Time.INITIAL.getMilliseconds();
            } else {
                time = new BigDecimal(jsonTime.getAsString());
            }
            JsonElement jsonPrecision = jsonObject.get("precision");
            Precision precision;
            if (jsonPrecision == null) {
                precision = Precision.Seconds;
            } else {
                precision = Precision.values()[jsonPrecision.getAsInt()];
            }
            return new Time(time, precision);
        }
    }

    public Time withPrecision(Precision precision) {
        return new Time(this.milliseconds, precision);
    }

    private BigDecimal precised() {
        BigDecimal newMillis;
        switch (this.precision) {
            case Seconds:
                newMillis = this.milliseconds.setScale(0, RoundingMode.HALF_UP).setScale(3, RoundingMode.UNNECESSARY);
                break;
            case Deciseconds:
                newMillis = this.milliseconds.setScale(1, RoundingMode.HALF_UP).setScale(3, RoundingMode.UNNECESSARY);
                break;
            case Centiseconds:
                newMillis = this.milliseconds.setScale(2, RoundingMode.HALF_UP).setScale(3, RoundingMode.UNNECESSARY);
                break;
            case Milliseconds:
                newMillis = this.milliseconds;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + this.precision);
        }
        return newMillis;
    }

    public BigDecimal getMilliseconds() { return milliseconds; }

    public int getSeconds() { return precised().intValue(); }
    public int getMinutes() { return getSeconds() / 60; }
    public int getHours() { return getSeconds() / 3600; }
    public int getMinutesPart() { return (getSeconds() % 3600) / 60; }
    public int getSecondsPart() { return getSeconds() % 60; }

    public int getDecisecondsPart() {
        return precised().setScale(1, RoundingMode.HALF_UP).
                subtract(precised().setScale(0, RoundingMode.FLOOR)).
                movePointRight(1).
                intValue();
    }

    public int getCentisecondsPart() {
        return precised().setScale(2, RoundingMode.HALF_UP).
                subtract(precised().setScale(0, RoundingMode.FLOOR)).
                movePointRight(2).
                intValue();
    }

    public int getMillisecondsPart() {
        return precised().
                subtract(precised().setScale(0, RoundingMode.FLOOR)).
                movePointRight(3).
                intValue();
    }

    public Time multiply(double multiplicand) {
        return new Time(
            precised().multiply(new BigDecimal(multiplicand)),
            this.precision
        );
    }

    public Time divide(double divisor) {
        return new Time(
            precised().divide(new BigDecimal(divisor), RoundingMode.HALF_UP),
            this.precision
        );
//        return new Time((int) Math.round(getMilliseconds() / divisor));
    }

    @Override
    public String toString() {
        String afterPoint = "";
        switch (precision) {
            case Seconds:
                afterPoint = "";
                break;
            case Deciseconds:
                afterPoint = "." + getDecisecondsPart();
                break;
            case Centiseconds:
                afterPoint = String.format(Locale.US, ".%02d", getCentisecondsPart());
                break;
            case Milliseconds:
                afterPoint = String.format(Locale.US, ".%03d", getMillisecondsPart());
        }
        int hours = getHours();
        int minutes = getMinutesPart();
        int lSeconds = getSecondsPart();
        if (hours > 0) {
            return String.format(Locale.US, "%d:%02d:%02d", hours, minutes, lSeconds) + afterPoint;
        } else {
            return String.format(Locale.US, "%d:%02d", minutes, lSeconds) + afterPoint;
        }
    }

    protected Time(Parcel in) {
        milliseconds = new BigDecimal(in.readString());
        precision = Precision.values()[in.readInt()];
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
        dest.writeString(milliseconds.toPlainString());
        dest.writeInt(precision.ordinal());
    }
}
