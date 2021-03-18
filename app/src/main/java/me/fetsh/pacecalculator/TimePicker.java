package me.fetsh.pacecalculator;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

import androidx.annotation.NonNull;

public class TimePicker extends AlertDialog {
    public interface OnTimeSetListener {
        void onTimeSet(Time time);
    }
    private final OnTimeSetListener onTimeSetListener;

    private final NumberPicker hoursPicker;
    private final NumberPicker minutesPicker;
    private final NumberPicker secondsPicker;


    public TimePicker(@NonNull Context context, OnTimeSetListener onTimeSetListener) {
        super(context);

        this.onTimeSetListener = onTimeSetListener;

        final LayoutInflater inflater = LayoutInflater.from(context);
        final View rootPickerView = inflater.inflate(R.layout.time_picker, null);
        rootPickerView.setSaveFromParentEnabled(false);
        setView(rootPickerView);
        setButton(BUTTON_POSITIVE, context.getString(R.string.ok), this::onPositiveButton);
        setButton(BUTTON_NEGATIVE, context.getString(R.string.cancel), this::onNegativeButton);

        hoursPicker = rootPickerView.findViewById(R.id.time_hours);
        minutesPicker = rootPickerView.findViewById(R.id.time_minutes);
        secondsPicker = rootPickerView.findViewById(R.id.time_seconds);

        hoursPicker.setMinValue(0);
        hoursPicker.setMaxValue(23);
        minutesPicker.setMinValue(0);
        minutesPicker.setMaxValue(59);
        minutesPicker.setFormatter(s -> String.format("%02d", s));
        secondsPicker.setMinValue(0);
        secondsPicker.setMaxValue(59);
        secondsPicker.setFormatter(s -> String.format("%02d", s));
    }


    public void setTime(Time time) {
        setHours(time.getHours());
        setMinutes(time.getMinutesPart());
        setSeconds(time.getSecondsPart());
    }

    public void setHours(int hours) {
        hoursPicker.setValue(hours);
    }
    public void setMinutes(int minutes) {
        minutesPicker.setValue(minutes);
    }

    public void setSeconds(int seconds) {
        secondsPicker.setValue(seconds);
    }

    private void onNegativeButton(DialogInterface dialogInterface, int i) {
        cancel();
    }

    private void onPositiveButton(DialogInterface dialogInterface, int i) {
        if (onTimeSetListener == null) return;
        hoursPicker.clearFocus();
        minutesPicker.clearFocus();
        secondsPicker.clearFocus();

        onTimeSetListener.onTimeSet(new Time(hoursPicker.getValue(), minutesPicker.getValue(), secondsPicker.getValue()));
    }
}
