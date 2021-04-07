package me.fetsh.pacecalculator;

import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;

import java.util.List;

public class CalculatorDataVM extends ViewModel {
    private final SharedPreferences sharedPreferences;

    private SharedPreferencesData<Pace> pace;
    private SharedPreferencesData<Distance> distance;
    private final MutableLiveData<Speed> speed = new MutableLiveData<>();
    private final MutableLiveData<Time> time = new MutableLiveData<>();
    private final MutableLiveData<List<Distance>> splits = new MutableLiveData<>();

    public CalculatorDataVM(SharedPreferences preferences) {
        this.sharedPreferences = preferences;
    }

    public LiveData<List<Distance>> getSplits() {
        if (splits.getValue() == null) {
            splits.setValue(
                    Distance.all(new Distance(0, getPace().getValue().getDistance().getUnit()), getDistance().getValue(), getPace().getValue().getDistance())
            );
        }
        return splits;
    }

    public LiveData<Speed> getSpeed() {
        if (speed.getValue() == null) {
            speed.setValue(Speed.fromPace(getPace().getValue()));
        }
        return speed;
    }

    public LiveData<Time> getTime() {
        if (time.getValue() == null) {
            time.setValue(getPace().getValue().getTime(getDistance().getValue()));
        }
        return time;
    }

    public LiveData<Pace> getPace() {
        if (pace == null) {
            pace = new SharedPace(sharedPreferences);
        }
        return pace;
    }

    public LiveData<Distance> getDistance() {
        if (distance == null) {
            distance = new SharedDistance(sharedPreferences);
        }
        return distance;
    }

    void setPace(Pace pace) {
        sharedPreferences.edit().putString(SharedPace.PACE_KEY, new Gson().toJson(pace)).apply();
        speed.setValue(Speed.fromPace(pace));
        time.setValue(pace.getTime(getDistance().getValue()));
        splits.setValue(Distance.all(new Distance(0, pace.getDistance().getUnit()), getDistance().getValue(), pace.getDistance()));
    }

    void setDistance(Distance distance) {
        sharedPreferences.edit().putString(SharedDistance.DISTANCE_KEY, new Gson().toJson(distance)).apply();
        time.setValue(getPace().getValue().getTime(distance));
        splits.setValue(Distance.all(new Distance(0, getPace().getValue().getDistance().getUnit()), getDistance().getValue(), getPace().getValue().getDistance()));
    }

    void setTime(Time time) {
        Pace newPace = getPace().getValue().convertWithDistanceAndTime(getDistance().getValue(), time);
        sharedPreferences.edit().putString(SharedPace.PACE_KEY, new Gson().toJson(newPace)).apply();
        speed.setValue(Speed.fromPace(newPace));
        this.time.setValue(newPace.getTime(getDistance().getValue()));
        splits.setValue(Distance.all(new Distance(0, newPace.getDistance().getUnit()), getDistance().getValue(), newPace.getDistance()));
    }
    void setSpeed(Speed speed) {
        this.speed.setValue(speed);
        Pace newPace = new Pace(new Time((int) Math.round(3600d / speed.getSpeed())), speed.getDistance());
        sharedPreferences.edit().putString(SharedPace.PACE_KEY, new Gson().toJson(newPace)).apply();
        this.time.setValue(newPace.getTime(getDistance().getValue()));
        splits.setValue(Distance.all(new Distance(0, newPace.getDistance().getUnit()), getDistance().getValue(), newPace.getDistance()));
    }


    private static class SharedDistance extends SharedPreferencesData<Distance> {
        private static final String DISTANCE_KEY = "distance_key";
        public SharedDistance(SharedPreferences sharedPreferences) {
            super(sharedPreferences, DISTANCE_KEY, Distance.marathon());
            super.setListener((_p, key) -> {
                if (key.equals(DISTANCE_KEY)) {
                    setValue(new Gson().fromJson(getSharedPreferences().getString(getKey(), null), Distance.class));
                }
            });
            setValueFromPrefOrDefault();
        }
        @Override
        void setValueFromPrefOrDefault() {
            Distance newValue = getSharedPreferences().contains(getKey())
                    ? new Gson().fromJson(getSharedPreferences().getString(getKey(), null), Distance.class)
                    : getDefaultValue();
            setValue(newValue);
        }
    }

    private static class SharedPace extends SharedPreferencesData<Pace> {
        private static final String PACE_KEY = "pace_key";

        public SharedPace(SharedPreferences sharedPreferences) {
            super(sharedPreferences, PACE_KEY, Pace.INITIAL);
            super.setListener((_p, key) -> {
                if (key.equals(PACE_KEY)) {
                    setValue(new Gson().fromJson(getSharedPreferences().getString(getKey(), null), Pace.class));
                }
            });
            setValueFromPrefOrDefault();
        }

        @Override
        void setValueFromPrefOrDefault() {
            Pace newValue = getSharedPreferences().contains(getKey())
                    ? new Gson().fromJson(getSharedPreferences().getString(getKey(), null), Pace.class)
                    : getDefaultValue();
            setValue(newValue);
        }
    }

    private static class SharedPreferencesData<T> extends MutableLiveData<T> {
        private final String key;
        private final SharedPreferences sharedPreferences;
        private final T defaultValue;
        private SharedPreferences.OnSharedPreferenceChangeListener mListener;

        public SharedPreferencesData(SharedPreferences sharedPreferences, String key, T defaultValue) {
            this.sharedPreferences = sharedPreferences;
            this.key = key;
            this.defaultValue = defaultValue;
        }

        public void setListener(SharedPreferences.OnSharedPreferenceChangeListener mListener) {
            this.mListener = mListener;
        }

        void setValueFromPrefOrDefault() {}

        @Override
        protected void onActive() {
            super.onActive();
            setValueFromPrefOrDefault();
            getSharedPreferences().registerOnSharedPreferenceChangeListener(mListener);
        }

        @Override
        protected void onInactive() {
            super.onInactive();
            getSharedPreferences().unregisterOnSharedPreferenceChangeListener(mListener);
        }

        public String getKey() {
            return key;
        }

        public SharedPreferences getSharedPreferences() {
            return sharedPreferences;
        }

        public T getDefaultValue() {
            return defaultValue;
        }
    }
}
