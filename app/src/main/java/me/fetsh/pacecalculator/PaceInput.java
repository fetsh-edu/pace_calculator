package me.fetsh.pacecalculator;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;


public class PaceInput extends ConstraintLayout implements View.OnClickListener {

    private final TextView mPaceInput;
    private Pace pace;
    private PacePicker.OnPaceSetListener onPaceSetListener;

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
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.pace_input, this, true);
        mPaceInput = findViewById(R.id.pace_input);
        setOnClickListener(this);
    }

    public void setPace(Pace pace) {
        this.pace = pace;
        mPaceInput.setText(pace.toString());
    }

    public Pace getPace() {
        return pace;
    }

    @Override
    public void onClick(View v) {
        PacePicker pacePicker = new PacePicker(getContext(), onPaceSetListener);
        pacePicker.setMinutes(pace.getMinutes());
        pacePicker.setSeconds(pace.getSecondsPart());
        pacePicker.show();
    }


    public void setOnPaceSetListener(PacePicker.OnPaceSetListener listener) {
        this.onPaceSetListener = listener;
    }
}
