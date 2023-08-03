package me.fetsh.pacecalculator;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;

import androidx.annotation.NonNull;

import java.util.Arrays;
import java.util.Locale;

public class PacePicker extends AlertDialog {

    public interface OnPaceSetListener {
        void onPaceSet(Pace pace);
    }
    private final OnPaceSetListener onPaceSetListener;

    private final NumberPicker minutesPicker;
    private final NumberPicker secondsPicker;
    private final NumberPicker unitSystemPicker;
    private final EditText millisPicker;
    private final Distance[] distances = {
            new Distance(1, DistanceUnit.Kilometer, "km"),
            new Distance(1, DistanceUnit.Mile, "mile")
    };
    private Pace currentPace;

    public PacePicker(@NonNull Context context, OnPaceSetListener onPaceSetListener) {
        super(context, R.style.Theme_PaceCalculator_AlertDialog);

        this.onPaceSetListener = onPaceSetListener;

        final LayoutInflater inflater = LayoutInflater.from(context);
        final View rootPickerView = inflater.inflate(R.layout.pace_picker, null);
        rootPickerView.setSaveFromParentEnabled(false);
        setView(rootPickerView);
        setButton(BUTTON_POSITIVE, context.getString(R.string.ok), this::onPositiveButton);
        setButton(BUTTON_NEGATIVE, context.getString(R.string.cancel), this::onNegativeButton);
        setTitle(R.string.set_pace);


        minutesPicker = rootPickerView.findViewById(R.id.minutes);
        secondsPicker = rootPickerView.findViewById(R.id.seconds);
        unitSystemPicker = rootPickerView.findViewById(R.id.unit_system);
        minutesPicker.setMinValue(2);
        minutesPicker.setMaxValue(10);
        secondsPicker.setMinValue(0);
        secondsPicker.setMaxValue(59);
        secondsPicker.setFormatter(s -> String.format(Locale.getDefault(), "%02d", s));
        millisPicker = rootPickerView.findViewById(R.id.pace_millis);
        final String[] values = Arrays.stream(distances).map(Distance::toString).toArray(String[]::new);
        unitSystemPicker.setMinValue(0);
        unitSystemPicker.setMaxValue(values.length - 1);
        unitSystemPicker.setDisplayedValues(values);
    }

    public void setPace(Pace pace) {
        currentPace = pace;
        setMinutes(pace.getMinutes());
        setSeconds(pace.getSecondsPart());
        setUnitSystem(pace.getDistance());
        setMillis(pace.getTime());
    }

    public void setMillis(Time time) {
        switch (time.precision) {
            case Seconds:
                millisPicker.setText("0");
                millisPicker.setVisibility(View.GONE);
                break;
            case Deciseconds:
                millisPicker.setHint(getContext().getString(R.string.deci));
                millisPicker.setFilters(new InputFilter[] {new InputFilter.LengthFilter(1)});
                millisPicker.setText(String.valueOf(time.getDecisecondsPart()));
                millisPicker.setVisibility(View.VISIBLE);
                break;
            case Centiseconds:
                millisPicker.setFilters(new InputFilter[] {new InputFilter.LengthFilter(2)});
                millisPicker.setHint(getContext().getString(R.string.centi));
                millisPicker.setText(String.valueOf(time.getCentisecondsPart()));
                millisPicker.setVisibility(View.VISIBLE);
                break;
            case Milliseconds:
                millisPicker.setFilters(new InputFilter[] {new InputFilter.LengthFilter(3)});
                millisPicker.setHint(getContext().getString(R.string.milli));
                millisPicker.setText(String.valueOf(time.getMillisecondsPart()));
                millisPicker.setVisibility(View.VISIBLE);
                break;
        }
    }

    public void setMinutes(int minutes) {
        minutesPicker.setValue(minutes);
    }

    public void setSeconds(int seconds) {
        secondsPicker.setValue(seconds);
    }

    public void setUnitSystem(Distance distance) {
        unitSystemPicker.setValue(Arrays.asList(distances).indexOf(distance));
    }

    private void onNegativeButton(DialogInterface dialogInterface, int i) {
        cancel();
    }

    private void onPositiveButton(DialogInterface dialogInterface, int i) {
        if (onPaceSetListener == null) return;
        minutesPicker.clearFocus();
        secondsPicker.clearFocus();

        int millisInput, millis;
        try {
            millisInput = Integer.parseInt(millisPicker.getText().toString());
        } catch (NumberFormatException e) {
            millisInput = 0;
        }

        switch (currentPace.getTime().precision) {
            case Seconds:
                millis = 0;
                break;
            case Deciseconds:
                millis = millisInput * 100;
                break;
            case Centiseconds:
                millis = millisInput * 10;
                break;
            case Milliseconds:
                millis = millisInput;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + currentPace.getTime().precision);
        }

        onPaceSetListener.onPaceSet(
            new Pace(
                Time.fromMilliseconds(
                        1000 * (minutesPicker.getValue() * 60 + secondsPicker.getValue()) + millis,
                        currentPace.getTime().precision
                ),
                distances[unitSystemPicker.getValue()]
            )
        );
    }
}
