package me.fetsh.pacecalculator;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;

public class TwoNumbersDialog extends AlertDialog {


    private final OnNumbersSetListener mNumbersSetListener;
    private final TwoNumbersPicker mTwoNumbersPicker;



    public interface OnNumbersSetListener {
        void onNumbersSet(TwoNumbersPicker view, int num1, int num2);
    }

    private TwoNumbersDialog(
            Context context,
            OnNumbersSetListener listener,
            int firstMin,
            int firstMax,
            int secondMin,
            int secondMax,
            int firsInitial,
            int secondInitial
    ) {
        super(context);
        this.mNumbersSetListener = listener;

        final Context themeContext = getContext();
        final LayoutInflater inflater = LayoutInflater.from(themeContext);
        final View view = inflater.inflate(R.layout.two_numbers_picker_dialog, null);
        setView(view);
        setButton(BUTTON_POSITIVE, themeContext.getString(R.string.ok), this::onPositiveButton);
        setButton(BUTTON_NEGATIVE, themeContext.getString(R.string.cancel), this::onNegativeButton);
        mTwoNumbersPicker = view.findViewById(R.id.twoNumbersPicker);
        mTwoNumbersPicker.setFirstMinMax(firstMin, firstMax);
        mTwoNumbersPicker.setSecondMinMax(secondMin, secondMax);
        mTwoNumbersPicker.setFirstNumber(firsInitial);
        mTwoNumbersPicker.setSecondNumber(secondInitial);
    }

    private void onNegativeButton(DialogInterface dialogInterface, int i) {
        cancel();
    }

    private void onPositiveButton(DialogInterface dialogInterface, int i) {
        if (mNumbersSetListener == null) return;

        mNumbersSetListener.onNumbersSet(
                mTwoNumbersPicker,
                mTwoNumbersPicker.getFirstNumber(),
                mTwoNumbersPicker.getSecondNumber()
        );
    }
    public static class Builder {

        private int initFirstNumber;
        private int initSecondNumber;
        private int firstNumberMin;
        private int firstNumberMax;
        private int secondNumberMin;
        private int secondNumberMax;

        private final Context mContext;
        private OnNumbersSetListener mOnNumbersSetListener;

        public  Builder(Context context, OnNumbersSetListener listener) {
            mContext = context;
            mOnNumbersSetListener = listener;
        }

        public Builder setOnNumbersSetListener(OnNumbersSetListener mNumbersSetListener) {
            this.mOnNumbersSetListener = mNumbersSetListener;
            return this;
        }

        public Builder setInitFirstNumber(int initFirstNumber) {
            this.initFirstNumber = initFirstNumber;
            return this;
        }

        public Builder setInitSecondNumber(int initSecondNumber) {
            this.initSecondNumber = initSecondNumber;
            return this;
        }

        public Builder setFirstMinMax(int min, int max) {
            this.firstNumberMin = min;
            this.firstNumberMax = max;
            return this;
        }
        public Builder setSecondMinMax(int min, int max) {
            this.secondNumberMin = min;
            this.secondNumberMax = max;
            return this;
        }

        public TwoNumbersDialog create() {
            return new TwoNumbersDialog(
                    mContext, mOnNumbersSetListener,
                    firstNumberMin, firstNumberMax,
                    secondNumberMin, secondNumberMax,
                    initFirstNumber, initSecondNumber
            );
        }
    }
}
