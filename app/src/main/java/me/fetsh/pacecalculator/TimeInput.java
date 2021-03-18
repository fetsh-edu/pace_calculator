package me.fetsh.pacecalculator;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

public class TimeInput extends ConstraintLayout implements View.OnClickListener {
    private final TextView mTimeInput;
    private Pace time;
    private TimePicker.OnTimeSetListener onTimeSetListener;

    public TimeInput(@NonNull Context context) {
        this(context, null);
    }

    public TimeInput(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TimeInput(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public TimeInput(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.time_input, this, true);
        mTimeInput = findViewById(R.id.time_input);
        setOnClickListener(this);
    }

    public void setTime(Pace pace) {
        this.time = pace;
        mTimeInput.setText(pace.toString());
    }

    @Override
    public void onClick(View v) {
        TimePicker timePicker = new TimePicker(getContext(), onTimeSetListener);
        timePicker.setTime(time);
        timePicker.show();
    }

    public void setOnTimeSetListener(TimePicker.OnTimeSetListener listener) {
        this.onTimeSetListener = listener;
    }
}
