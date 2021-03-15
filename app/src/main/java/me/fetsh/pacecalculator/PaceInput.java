package me.fetsh.pacecalculator;

import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.app.AlertDialog;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;


public class PaceInput extends ConstraintLayout implements View.OnClickListener {

    public interface OnPaceSetListener {
        void onPaceSet(Pace pace);
    }

    private final TextView mPaceInput;

    private AlertDialog pacePickerDialog;
    private NumberPicker minutesPicker;
    private NumberPicker secondsPicker;

    private Pace pace;
    private OnPaceSetListener paceSetListener;

    public PaceInput(@NonNull Context context) {
        this(context, null);
    }

    public PaceInput(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PaceInput(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public PaceInput(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.pace_input, this, true);
        mPaceInput = findViewById(R.id.pace_input);
        setOnClickListener(this);
        buildPaceAlertDialog(context);
    }

    private void buildPaceAlertDialog(@NonNull Context context) {
        LinearLayout rootPickerView = new LinearLayout(context);
        rootPickerView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        rootPickerView.setOrientation(LinearLayout.HORIZONTAL);
        rootPickerView.setGravity(Gravity.CENTER_HORIZONTAL);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(Utils.dpToPx(context, 70), LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, Utils.dpToPx(context, 5), 0);

        minutesPicker = new NumberPicker(context);
        minutesPicker.setLayoutParams(params);
        secondsPicker = new NumberPicker(context);
        secondsPicker.setLayoutParams(params);

        minutesPicker.setMinValue(3);
        minutesPicker.setMaxValue(10);
        minutesPicker.setWrapSelectorWheel(true);
        rootPickerView.addView(minutesPicker);

        secondsPicker.setMinValue(0);
        secondsPicker.setMaxValue(59);
        secondsPicker.setWrapSelectorWheel(true);
        rootPickerView.addView(secondsPicker);

        pacePickerDialog = new AlertDialog.Builder(getContext())
                .setTitle(R.string.pace)
                .setView(rootPickerView)
                .setPositiveButton(R.string.ok, this::onPositiveButton)
                .setNegativeButton(R.string.cancel, this::onNegativeButton)
                .create();
    }

    public void setPace(Pace pace) {
        this.pace = pace;
        mPaceInput.setText(pace.toString());
    }

    public void setOnPaceSetListener(OnPaceSetListener onPaceSet) {
        this.paceSetListener = onPaceSet;
    }

    public Pace getPace() {
        return pace;
    }

    @Override
    public void onClick(View v) {
        minutesPicker.setValue(getPace().getMinutes());
        secondsPicker.setValue(getPace().getSecondsPart());
        pacePickerDialog.show();
    }

    private void onNegativeButton(DialogInterface dialogInterface, int i) {
        dialogInterface.cancel();
    }

    private void onPositiveButton(DialogInterface dialogInterface, int i) {
        if (paceSetListener == null) return;

        paceSetListener.onPaceSet(new Pace(minutesPicker.getValue(), secondsPicker.getValue()));
    }
}
