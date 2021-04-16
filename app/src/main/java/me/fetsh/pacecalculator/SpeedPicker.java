package me.fetsh.pacecalculator;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;

import androidx.annotation.NonNull;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Arrays;
import java.util.Locale;

public class SpeedPicker extends AlertDialog {

    public interface OnSpeedSetListener {
        void onSpeedSet(Speed speed);
    }

    private static final DecimalFormat defaultFormatter;

    static {
        defaultFormatter = new DecimalFormat("#.#", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
        defaultFormatter.setMaximumFractionDigits(2);
    }

    private final EditText speedInput;
    private final NumberPicker unitSystemPicker;
    private final SpeedPicker.OnSpeedSetListener onSpeedSetListener;
    private final Distance[] distances = {
            new Distance(1, DistanceUnit.Kilometer, "km/h"),
            new Distance(1, DistanceUnit.Mile, "mph")
    };

    public SpeedPicker(@NonNull Context context, SpeedPicker.OnSpeedSetListener onSpeedSetListener) {
        super(context, R.style.Theme_PaceCalculator_AlertDialog);
        this.onSpeedSetListener = onSpeedSetListener;
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View rootPickerView = inflater.inflate(R.layout.speed_picker, null);
        rootPickerView.setSaveFromParentEnabled(false);
        setView(rootPickerView);
        setButton(BUTTON_POSITIVE, context.getString(R.string.ok), this::onPositiveButton);
        setButton(BUTTON_NEGATIVE, context.getString(R.string.cancel), this::onNegativeButton);
        setTitle(R.string.set_speed);

        speedInput = rootPickerView.findViewById(R.id.speed_edit_text);
        unitSystemPicker = rootPickerView.findViewById(R.id.unit_system);

        final String[] values = Arrays.stream(distances).map(Distance::toString).toArray(String[]::new);

        unitSystemPicker.setMinValue(0);
        unitSystemPicker.setMaxValue(values.length - 1);
        unitSystemPicker.setDisplayedValues(values);
    }

    public void setSpeed(Speed speed) {
        speedInput.setText(defaultFormatter.format(speed.getSpeed()));
        setUnitSystem(speed.getDistance());
    }

    public void setUnitSystem(Distance distance) {
        int position = Arrays.asList(distances).indexOf(distance);
        unitSystemPicker.setValue(position);
    }

    private void onNegativeButton(DialogInterface dialogInterface, int i) {
        cancel();
    }

    private void onPositiveButton(DialogInterface dialogInterface, int i) {
        if (onSpeedSetListener == null) return;
        onSpeedSetListener.onSpeedSet(new Speed(Double.parseDouble(speedInput.getText().toString()), distances[unitSystemPicker.getValue()]));
    }
}
