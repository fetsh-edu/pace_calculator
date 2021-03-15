package me.fetsh.pacecalculator;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.NumberPicker;

import androidx.annotation.NonNull;


public class TwoNumbersPicker extends FrameLayout {

    public interface OnNumbersChangedListener {
        /**
         * @param view The view associated with this listener.
         * @param firstNumber The current hour.
         * @param secondNumber The current minute.
         */
        void onNumbersChanged(TwoNumbersPicker view, int firstNumber, int secondNumber);
    }

    private final NumberPicker mFirstNumberSpinner;
    private final NumberPicker mSecondNumberSpinner;

    private final Context mContext;
    private OnNumbersChangedListener mOnNumbersChangedListener;

    public TwoNumbersPicker(@NonNull Context context) {
        this(context, null);

    }

    public TwoNumbersPicker(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public TwoNumbersPicker(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        mContext = context;

        final LayoutInflater inflater = LayoutInflater.from(mContext);
        final View view = inflater.inflate(R.layout.pace_picker, this, true);
        view.setSaveFromParentEnabled(false);
        mFirstNumberSpinner = findViewById(R.id.firstNumber);
        mSecondNumberSpinner = findViewById(R.id.secondNumber);
    }

    public void setFirstMinMax(int min, int max) {
        mFirstNumberSpinner.setMinValue(min);
        mFirstNumberSpinner.setMaxValue(max);
    }
    public void setSecondMinMax(int min, int max) {
        mSecondNumberSpinner.setMinValue(min);
        mSecondNumberSpinner.setMaxValue(max);
    }

    public int getFirstNumber() {
        return mFirstNumberSpinner.getValue();
    }

    public int getSecondNumber() {
        return mSecondNumberSpinner.getValue();
    }

    public void setFirstNumber(int firstNumber) {
        if (firstNumber == getFirstNumber()) {
            return;
        }
        mFirstNumberSpinner.setValue(firstNumber);
        onNumbersChanged();
    }

    public void setSecondNumber(int secondNumber) {
        if (secondNumber == getSecondNumber()) {
            return;
        }
        mSecondNumberSpinner.setValue(secondNumber);
        onNumbersChanged();
    }

    private void onNumbersChanged() {
        if (mOnNumbersChangedListener == null) return;
        mOnNumbersChangedListener.onNumbersChanged(this, getFirstNumber(),
                getSecondNumber());
    }
}
