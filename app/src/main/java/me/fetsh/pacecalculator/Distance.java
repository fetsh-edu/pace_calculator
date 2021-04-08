package me.fetsh.pacecalculator;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class Distance implements Comparable<Distance>, Parcelable {

    private final BigDecimal amount;
    private final DistanceUnit unit;
    private String name;

    private static final DecimalFormat defaultFormatter;

    static {
        defaultFormatter = new DecimalFormat("#.#", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
        defaultFormatter.setMaximumFractionDigits(4);
    }

    public Distance(double amount, DistanceUnit unit) {
        this(amount, unit, null);
    }

    public Distance(@NonNull double amount, @NonNull DistanceUnit unit, @Nullable String name) {
        this.amount = new BigDecimal(amount);
        this.unit = unit;
        this.name = name;
    }

    public static Distance marathon() {
        return new Distance(42.195, DistanceUnit.Kilometer, "Marathon");
    }
    public static Distance halfMarathon() {
        return new Distance(21.0975, DistanceUnit.Kilometer, "1/2 Marathon");
    }
    public static Distance fromMillis(int millimeters, DistanceUnit unit) {
        switch (unit) {
            case Mile: return new Distance(BigDecimal.valueOf(millimeters).divide(BigDecimal.valueOf(1609344), MathContext.DECIMAL64).doubleValue(), unit);
            case Meter: return new Distance(BigDecimal.valueOf(millimeters).divide(BigDecimal.valueOf(1000), MathContext.DECIMAL64).doubleValue(), unit);
            case Kilometer: return new Distance(BigDecimal.valueOf(millimeters).divide(BigDecimal.valueOf(1000000), MathContext.DECIMAL64).doubleValue(), unit);
            default: throw new IllegalArgumentException("Illegal distance unit: " + unit.name());
        }
    }

    static List<Distance> allNamed() {
        ArrayList<Distance> distances = new ArrayList<>();
        distances.add(new Distance(5, DistanceUnit.Kilometer, "5 km"));
        distances.add(new Distance(10, DistanceUnit.Kilometer, "10 km"));
        distances.add(new Distance(10, DistanceUnit.Mile, "10 miles"));
        distances.add(Distance.halfMarathon());
        distances.add(Distance.marathon());
        return distances;
    }

    static List<Distance> splits() {
        ArrayList<Distance> distances = new ArrayList<>();
        distances.add(new Distance(200, DistanceUnit.Meter));
        distances.add(new Distance(400, DistanceUnit.Meter));
        distances.add(new Distance(1, DistanceUnit.Kilometer));
        distances.add(new Distance(1, DistanceUnit.Mile));
        distances.add(new Distance(5, DistanceUnit.Kilometer));
        distances.add(new Distance(5, DistanceUnit.Mile));
        return distances;
    }

    static List<Distance> all(Distance start, Distance cap, Distance step) {
        ArrayList<Distance> distances = new ArrayList<>();
        for (
                Distance i = start;
                i.lessThan(cap);
                i = i.add(step)
        ) {
            distances.add(i);
        }
        if (cap.greaterThan(Distance.halfMarathon())) distances.add(Distance.halfMarathon());
        if (cap.greaterThan(Distance.marathon())) distances.add(Distance.marathon());
        distances.add(cap);
        Collections.sort(distances);
        return distances;
    }

    public double getAmount() {
        return amount.doubleValue();
    }
    public Distance setAmount(double amount) {
        return new Distance(amount, unit);
    }

    public String getName() {
        return name;
    }

    public DistanceUnit getUnit() {
        return unit;
    }

    private Distance add(Distance step) {
        if (this.unit == step.unit) {
            return new Distance(this.amount.add(step.amount).doubleValue(), this.unit);
        } else {
            return Distance.fromMillis(getMillimeters() + step.getMillimeters(), this.unit);
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    private int getMillimeters() {
        switch (unit) {
            case Mile: return amount.multiply(BigDecimal.valueOf(1609344)).intValue();
            case Meter: return amount.multiply(BigDecimal.valueOf(1000)).intValue();
            case Kilometer: return amount.multiply(BigDecimal.valueOf(1000000)).intValue();
            default: throw new IllegalArgumentException("Illegal distance unit: " + unit.name());
        }
    }

    public boolean greaterThan(Distance arg) {
        return (this.compareTo(arg) > 0);
    }

    public boolean lessThan(Distance arg) {
        return (this.compareTo(arg) < 0);
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if(obj == null) return false;
        if (!(obj instanceof Distance)) return false;
        Distance other = (Distance) obj;
        return other.unit == this.unit && other.amount.equals(this.amount);
    }

    @Override
    public int compareTo(Distance o) {
        return Integer.compare(getMillimeters(), o.getMillimeters());
    }

    @Override
    public String toString() {
        if (name != null) {
            return name;
        } else {
            return defaultFormatter.format(amount);
        }
    }

    public String getSplitDistanceHeader() {
        if (getAmount() == 1) {
            return getUnit().getShortName();
        } else {
            return "" + defaultFormatter.format(amount) + " " + getUnit().getShortName();
        }
    }

    public String getFullName() {
        if (name != null) {
            return name;
        } else {
            return "" + defaultFormatter.format(amount) + " " + getUnit().getShortName();
        }
    }

    public double divide(Distance distance) {
        if (this.unit == distance.unit) {
            return this.amount.divide(distance.amount, MathContext.DECIMAL64).doubleValue();
        } else {
            return BigDecimal.valueOf(getMillimeters()).divide(BigDecimal.valueOf(distance.getMillimeters()), MathContext.DECIMAL64).doubleValue();
        }
    }

    protected Distance(Parcel in) {
        amount = BigDecimal.valueOf(in.readDouble());
        unit = DistanceUnit.valueOf(in.readString());
        name = in.readString();
    }

    public static final Creator<Distance> CREATOR = new Creator<Distance>() {
        @Override
        public Distance createFromParcel(Parcel in) {
            return new Distance(in);
        }

        @Override
        public Distance[] newArray(int size) {
            return new Distance[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(amount.doubleValue());
        dest.writeString(unit.toString());
        dest.writeString(name);
    }
}