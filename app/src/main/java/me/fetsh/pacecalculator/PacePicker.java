package me.fetsh.pacecalculator;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

import androidx.annotation.NonNull;

import java.util.Arrays;

public class PacePicker extends AlertDialog {

    public interface OnPaceSetListener {
        void onPaceSet(Pace pace);
    }
    private final OnPaceSetListener onPaceSetListener;

    private final NumberPicker minutesPicker;
    private final NumberPicker secondsPicker;
    private final NumberPicker unitSystemPicker;
    private final Distance[] distances = {
            new Distance(1, DistanceUnit.Kilometer, "km"),
            new Distance(1, DistanceUnit.Mile, "mile")
    };

    public PacePicker(@NonNull Context context, OnPaceSetListener onPaceSetListener) {
        super(context);

        this.onPaceSetListener = onPaceSetListener;

        final LayoutInflater inflater = LayoutInflater.from(context);
        final View rootPickerView = inflater.inflate(R.layout.pace_picker, null);
        rootPickerView.setSaveFromParentEnabled(false);
        setView(rootPickerView);
        setButton(BUTTON_POSITIVE, context.getString(R.string.ok), this::onPositiveButton);
        setButton(BUTTON_NEGATIVE, context.getString(R.string.cancel), this::onNegativeButton);

        minutesPicker = rootPickerView.findViewById(R.id.minutes);
        secondsPicker = rootPickerView.findViewById(R.id.seconds);
        unitSystemPicker = rootPickerView.findViewById(R.id.unit_system);
        minutesPicker.setMinValue(3);
        minutesPicker.setMaxValue(10);
        secondsPicker.setMinValue(0);
        secondsPicker.setMaxValue(59);
        secondsPicker.setFormatter(s -> String.format("%02d", s));
        final String[] values = Arrays.stream(distances).map(Distance::toString).toArray(String[]::new);
        unitSystemPicker.setMinValue(0);
        unitSystemPicker.setMaxValue(values.length - 1);
        unitSystemPicker.setDisplayedValues(values);
    }


    public void setPace(Pace pace) {
        setMinutes(pace.getMinutes());
        setSeconds(pace.getSecondsPart());
        setUnitSystem(pace.getDistance());
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

        onPaceSetListener.onPaceSet(new Pace(new Time(minutesPicker.getValue(), secondsPicker.getValue()), distances[unitSystemPicker.getValue()]));
    }
}
