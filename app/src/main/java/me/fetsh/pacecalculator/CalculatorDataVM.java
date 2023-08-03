package me.fetsh.pacecalculator;

import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import java.util.List;

public class CalculatorDataVM extends ViewModel {
    private final SharedPreferences sharedPreferences;

    private SharedPreferencesData<Pace> pace;
    private SharedPreferencesData<Distance> split;
    private SharedPreferencesData<Integer> nightMode;
    private SharedPreferencesData<Distance> distance;

    private final MutableLiveData<Speed> speed = new MutableLiveData<>();
    private final MutableLiveData<Time> time = new MutableLiveData<>();
    private final MutableLiveData<List<Distance>> splits = new MutableLiveData<>();

    public CalculatorDataVM(SharedPreferences preferences) {
        this.sharedPreferences = preferences;
    }

    public LiveData<List<Distance>> getSplits() {
        if (splits.getValue() == null) {
            updateDistancesWithSplitOrPace(getSplit().getValue(), getPace().getValue().getDistance());
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

    public LiveData<Distance> getSplit() {
        if (split == null) {
            split = new SharedSplit(sharedPreferences);
        }
        return split;
    }

    public LiveData<Integer> getNightMode() {
        if (nightMode == null) {
            nightMode = new SharedNightMode(sharedPreferences);
        }
        return nightMode;
    }

    public void setDistance(Distance distance) {
        sharedPreferences.edit().putString(SharedDistance.DISTANCE_KEY, new Gson().toJson(distance)).apply();
        time.setValue(getPace().getValue().getTime(distance));
        updateDistancesWithSplitOrPace(getSplit().getValue(), getPace().getValue().getDistance());
    }

    public void setPace(Pace newPace) {
        Speed newSpeed = Speed.fromPace(newPace);
        updateSpeedPaceTimeAndSplits(newSpeed, newPace);
    }

    public void setTime(Time time) {
        Pace newPace = getPace().getValue().convertWithDistanceAndTime(getDistance().getValue(), time);
        Speed newSpeed = Speed.fromPace(newPace);
        updateSpeedPaceTimeAndSplits(newSpeed, newPace);
    }
    public void setSpeed(Speed newSpeed) {
        Pace newPace = new Pace(Time.fromSeconds((int) Math.round(3600d / newSpeed.getSpeed()),this.getPace().getValue().getTime().precision), newSpeed.getDistance());
        updateSpeedPaceTimeAndSplits(newSpeed, newPace);
    }

    private void updateSpeedPaceTimeAndSplits(Speed newSpeed, Pace newPace) {
        sharedPreferences.edit().putString(SharedPace.PACE_KEY, newPace.toJson()).apply();
        this.time.setValue(newPace.getTime(getDistance().getValue()));
        this.speed.setValue(newSpeed);
        updateDistancesWithSplitOrPace(getSplit().getValue(), newPace.getDistance());
    }

    public void setSplit(Distance split) {
        sharedPreferences.edit().putString(SharedSplit.SPLIT_KEY, new Gson().toJson(split)).apply();
    }

    public void setNightMode(Integer nightMode) {
        sharedPreferences.edit().putInt(SharedNightMode.NIGHT_MODE_KEY, nightMode).apply();
    }

    public void setPrecision(Precision precision) {
        Pace newPace = getPace().getValue().withPrecision(precision);
        Speed newSpeed = Speed.fromPace(newPace);
        updateSpeedPaceTimeAndSplits(newSpeed, newPace);
    }

    public void updateDistancesWithSplit(Distance split) {
        updateDistancesWithSplitOrPace(split, getPace().getValue().getDistance());
    }

    public void updateDistancesWithSplitOrPace(Distance split, Distance paceSplit) {
        if (split == null) split = paceSplit;
        splits.setValue(Distance.all(new Distance(0, split.getUnit()), getDistance().getValue(), split));
    }

    private static class SharedNightMode extends SharedPreferencesData<Integer> {
        private static final String NIGHT_MODE_KEY = "night_mode_key";
        public SharedNightMode(SharedPreferences sharedPreferences) {
            super(sharedPreferences, NIGHT_MODE_KEY, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
            super.setListener((_p, key) -> {
                if (key.equals(NIGHT_MODE_KEY)) {
                    setValue(getSharedPreferences().getInt(getKey(), getDefaultValue()));
                }
            });
            setValueFromPrefOrDefault();
        }
        @Override
        void setValueFromPrefOrDefault() {
            setValue(getSharedPreferences().getInt(getKey(), getDefaultValue()));
        }
    }

    private static class SharedSplit extends SharedPreferencesData<Distance> {
        private static final String SPLIT_KEY = "split_key";
        public SharedSplit(SharedPreferences sharedPreferences) {
            super(sharedPreferences, SPLIT_KEY, null);
            super.setListener((_p, key) -> {
                if (key.equals(SPLIT_KEY)) {
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
        private static final String PACE_KEY = "pace_key_0308231205";

        public SharedPace(SharedPreferences sharedPreferences) {
            super(sharedPreferences, PACE_KEY, Pace.INITIAL);
            super.setListener((_p, key) -> {
                if (key.equals(PACE_KEY)) {
                    setValue(
                        Pace.fromJson(
                            getSharedPreferences().getString(getKey(), null)
                        )
                    );
                }
            });
            setValueFromPrefOrDefault();
        }

        @Override
        void setValueFromPrefOrDefault() {
            Pace newValue = getSharedPreferences().contains(getKey())
                    ? Pace.fromJson(getSharedPreferences().getString(getKey(), null))
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
