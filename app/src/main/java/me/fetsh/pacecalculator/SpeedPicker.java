package me.fetsh.pacecalculator;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;

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
    private final Spinner distanceSpinner;
    private final SpeedPicker.OnSpeedSetListener onSpeedSetListener;
    private final Distance[] distances = {
            new Distance(1, DistanceUnit.Kilometer, "km/h"),
            new Distance(1, DistanceUnit.Mile, "mph")
    };

    public SpeedPicker(@NonNull Context context, SpeedPicker.OnSpeedSetListener onSpeedSetListener) {
        super(context);
        this.onSpeedSetListener = onSpeedSetListener;
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View rootPickerView = inflater.inflate(R.layout.speed_picker, null);
        rootPickerView.setSaveFromParentEnabled(false);
        setView(rootPickerView);
        setButton(BUTTON_POSITIVE, context.getString(R.string.ok), this::onPositiveButton);
        setButton(BUTTON_NEGATIVE, context.getString(R.string.cancel), this::onNegativeButton);
        setTitle(R.string.set_speed);

        speedInput = rootPickerView.findViewById(R.id.speed_edit_text);
        distanceSpinner = rootPickerView.findViewById(R.id.speed_spinner);

        final String[] values = Arrays.stream(distances).map(Distance::toString).toArray(String[]::new);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, values);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        distanceSpinner.setAdapter(adapter);
    }

    public void setSpeed(Speed speed) {
        speedInput.setText(defaultFormatter.format(speed.getSpeed()));
        setUnitSystem(speed.getDistance());
    }

    public void setUnitSystem(Distance distance) {
        int position = Arrays.asList(distances).indexOf(distance);
        distanceSpinner.setSelection(position);
    }

    private void onNegativeButton(DialogInterface dialogInterface, int i) {
        cancel();
    }

    private void onPositiveButton(DialogInterface dialogInterface, int i) {
        if (onSpeedSetListener == null) return;

        onSpeedSetListener.onSpeedSet(new Speed(Double.parseDouble(speedInput.getText().toString()), distances[distanceSpinner.getSelectedItemPosition()]));
    }
}
