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

public class DistancePicker extends AlertDialog {

    private static final DecimalFormat defaultFormatter;

    static {
        defaultFormatter = new DecimalFormat("#.#", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
        defaultFormatter.setMaximumFractionDigits(4);
    }

    private final EditText distanceInput;
    private final NumberPicker unitSystemPicker;
    private final DistanceInput.OnDistanceSetListener onDistanceSetListener;

    public DistancePicker(@NonNull Context context, DistanceInput.OnDistanceSetListener onDistanceSetListener) {
        super(context, R.style.Theme_PaceCalculator_AlertDialog);
        this.onDistanceSetListener = onDistanceSetListener;
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View rootPickerView = inflater.inflate(R.layout.distance_picker, null);
        rootPickerView.setSaveFromParentEnabled(false);
        setView(rootPickerView);
        setButton(BUTTON_POSITIVE, context.getString(R.string.ok), this::onPositiveButton);
        setButton(BUTTON_NEGATIVE, context.getString(R.string.cancel), this::onNegativeButton);
        setTitle(R.string.set_distance);

        distanceInput = rootPickerView.findViewById(R.id.distance_edit_text);
        unitSystemPicker = rootPickerView.findViewById(R.id.unit_system);

        final String[] values = Arrays.stream(DistanceUnit.values()).map(DistanceUnit::getShortName).toArray(String[]::new);

        unitSystemPicker.setMinValue(0);
        unitSystemPicker.setMaxValue(values.length - 1);
        unitSystemPicker.setDisplayedValues(values);
    }

    public void setDistance(Distance distance) {
        distanceInput.setText(defaultFormatter.format(distance.getAmount()));
        setUnitSystem(distance.getUnit());
    }

    public void setUnitSystem(DistanceUnit unit) {
        unitSystemPicker.setValue(unit.ordinal());
    }

    private void onNegativeButton(DialogInterface dialogInterface, int i) {
        cancel();
    }

    private void onPositiveButton(DialogInterface dialogInterface, int i) {
        if (onDistanceSetListener == null) return;

        onDistanceSetListener.onDistanceSet(
                new Distance(Double.parseDouble(distanceInput.getText().toString()), DistanceUnit.values()[unitSystemPicker.getValue()])
        );
    }
}
