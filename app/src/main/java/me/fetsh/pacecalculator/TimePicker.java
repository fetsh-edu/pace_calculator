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

import java.util.Locale;

public class TimePicker extends AlertDialog {
    public interface OnTimeSetListener {
        void onTimeSet(Time time);
    }
    private final OnTimeSetListener onTimeSetListener;

    private final NumberPicker hoursPicker;
    private final NumberPicker minutesPicker;
    private final NumberPicker secondsPicker;
    private final EditText millisPicker;
    private Precision currentPrecision;


    public TimePicker(@NonNull Context context, OnTimeSetListener onTimeSetListener) {
        super(context, R.style.Theme_PaceCalculator_AlertDialog);

        this.onTimeSetListener = onTimeSetListener;

        final LayoutInflater inflater = LayoutInflater.from(context);
        final View rootPickerView = inflater.inflate(R.layout.time_picker, null);
        rootPickerView.setSaveFromParentEnabled(false);
        setView(rootPickerView);
        setButton(BUTTON_POSITIVE, context.getString(R.string.ok), this::onPositiveButton);
        setButton(BUTTON_NEGATIVE, context.getString(R.string.cancel), this::onNegativeButton);
        setTitle(R.string.set_time);

        hoursPicker = rootPickerView.findViewById(R.id.time_hours);
        minutesPicker = rootPickerView.findViewById(R.id.time_minutes);
        secondsPicker = rootPickerView.findViewById(R.id.time_seconds);
        millisPicker = rootPickerView.findViewById(R.id.time_millis);

        hoursPicker.setMinValue(0);
        hoursPicker.setMaxValue(23);
        minutesPicker.setMinValue(0);
        minutesPicker.setMaxValue(59);
        minutesPicker.setFormatter(s -> String.format(Locale.US, "%02d", s));
        secondsPicker.setMinValue(0);
        secondsPicker.setMaxValue(59);
        secondsPicker.setFormatter(s -> String.format(Locale.US, "%02d", s));
    }


    public void setTime(Time time) {
        this.currentPrecision = time.precision;
        setHours(time.getHours());
        setMinutes(time.getMinutesPart());
        setSeconds(time.getSecondsPart());
        setMillis(time);
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
    public void setMillis(Time time) {
        switch (currentPrecision) {
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

    private void onNegativeButton(DialogInterface dialogInterface, int i) {
        cancel();
    }

    private void onPositiveButton(DialogInterface dialogInterface, int i) {
        if (onTimeSetListener == null) return;
        hoursPicker.clearFocus();
        minutesPicker.clearFocus();
        secondsPicker.clearFocus();
        millisPicker.clearFocus();

        int millisInput, millis;
        try {
            millisInput = Integer.parseInt(millisPicker.getText().toString());
        } catch (NumberFormatException e) {
            millisInput = 0;
        }
        switch (currentPrecision) {
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
                throw new IllegalStateException("Unexpected value: " + currentPrecision);
        }

        onTimeSetListener.onTimeSet(
            Time.fromMilliseconds(
                    1000 * (hoursPicker.getValue() * 60 * 60 + minutesPicker.getValue() * 60 + secondsPicker.getValue()) + millis,
                currentPrecision
            )
        );
    }
}
