package me.fetsh.pacecalculator;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

public class SpeedInput extends ConstraintLayout implements View.OnClickListener {

    private final TextView mSpeedInput;
    private final TextView mSpeedPostfix;
    private Speed speed;
    private SpeedPicker.OnSpeedSetListener onSpeedSetListener;

    public SpeedInput(@NonNull Context context) {
        this(context, null);
    }

    public SpeedInput(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SpeedInput(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public SpeedInput(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.speed_input, this, true);
        mSpeedInput = findViewById(R.id.speed_input);
        mSpeedPostfix = findViewById(R.id.speed_header_postfix);
        setOnClickListener(this);
    }

    public void setSpeed(Speed speed) {
        this.speed = speed;
        mSpeedInput.setText(speed.toString());
        mSpeedPostfix.setText(speed.getPostfixString());
    }

    @Override
    public void onClick(View v) {
        SpeedPicker speedPicker = new SpeedPicker(getContext(), onSpeedSetListener);
        speedPicker.setSpeed(speed);
        speedPicker.show();
    }

    public void setOnSpeedSetListener(SpeedPicker.OnSpeedSetListener listener) {
        this.onSpeedSetListener = listener;
    }
}
