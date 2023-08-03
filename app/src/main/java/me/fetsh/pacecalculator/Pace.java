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

import java.lang.reflect.Type;

public class Pace implements Parcelable {
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Pace.class, new Serializer())
            .registerTypeAdapter(Pace.class, new Deserializer())
            .create();

    private final Time time;
    private final Distance distance;

    public static final Pace INITIAL = new Pace(Time.INITIAL,
        new Distance(1, DistanceUnit.Kilometer, "km")
    );

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

    public Pace withPrecision(Precision precision) {
        return new Pace(this.time.withPrecision(precision), this.distance);
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

    public String toJson() {
        return gson.toJson(this);
    }
    public static Pace fromJson(String json) {
        return gson.fromJson(json, Pace.class);
    }

    private static class Serializer implements JsonSerializer<Pace> {

        @Override
        public JsonElement serialize(
                Pace pace,
                Type type,
                JsonSerializationContext jsonSerializationContext
        ) {
            JsonObject paceJsonObj = new JsonObject();
            paceJsonObj.add("time", pace.getTime().toJsonTree());
            paceJsonObj.add("distance", new Gson().toJsonTree(pace.getDistance()));
            return paceJsonObj;
        }
    }
    private static class Deserializer implements JsonDeserializer<Pace> {

        @Override
        public Pace deserialize(JsonElement json, Type type,
                                     JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {

            JsonObject jsonObject = json.getAsJsonObject();
            JsonElement jsonTime = jsonObject.get("time");
            Time time = Time.fromJson(jsonTime.toString());
            JsonElement jsonDistance = jsonObject.get("distance");

            return new Pace(time, new Gson().fromJson(jsonDistance.toString(), Distance.class)
            );
        }
    }
}