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
        final String[] values = Arrays.stream(UnitSystem.values()).map(UnitSystem::name).toArray(String[]::new);
        unitSystemPicker.setMinValue(0);
        unitSystemPicker.setMaxValue(values.length - 1);
        unitSystemPicker.setDisplayedValues(values);
    }


    public void setPace(Pace pace) {
        setMinutes(pace.getMinutes());
        setSeconds(pace.getSecondsPart());
        setUnitSystem(pace.getUnitSystem());
    }

    public void setMinutes(int minutes) {
        minutesPicker.setValue(minutes);
    }

    public void setSeconds(int seconds) {
        secondsPicker.setValue(seconds);
    }

    public void setUnitSystem(UnitSystem unitSystem) {
        unitSystemPicker.setValue(unitSystem.ordinal());
    }

    private void onNegativeButton(DialogInterface dialogInterface, int i) {
        cancel();
    }

    private void onPositiveButton(DialogInterface dialogInterface, int i) {
        if (onPaceSetListener == null) return;

        onPaceSetListener.onPaceSet(Pace.withUnitSystem(UnitSystem.values()[unitSystemPicker.getValue()], minutesPicker.getValue(), secondsPicker.getValue()));
    }
}
