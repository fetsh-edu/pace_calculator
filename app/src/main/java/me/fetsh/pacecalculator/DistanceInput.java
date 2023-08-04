package me.fetsh.pacecalculator;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.textview.MaterialTextView;

import java.util.List;


public class DistanceInput extends ConstraintLayout implements View.OnClickListener {

    public interface OnDistanceSetListener {
        void onDistanceSet(Distance distance);
    }

    private final MaterialTextView mDistanceInput;
    private OnDistanceSetListener onDistanceSetListener;
    private Distance distance;

    public DistanceInput(@NonNull Context context) {
        this(context, null);
    }

    public DistanceInput(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DistanceInput(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public DistanceInput(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.distance_input, this, true);
        setOnClickListener(this);
        mDistanceInput = findViewById(R.id.distance_input);
    }

    @Override
    public void onClick(View v) {
        final List<Distance> distances = Distance.allNamed();
        final PopupMenu popupMenu = new PopupMenu(getContext(), v);
        for (int i = 0; i < distances.size(); i++) {
            popupMenu.getMenu().add(Menu.NONE, i, i, distances.get(i).getName());
        }
        popupMenu.getMenu().add(Menu.NONE, distances.size(), distances.size(), R.string.custom_distance);
        popupMenu.setOnMenuItemClickListener(item -> {
            if (onDistanceSetListener == null) return true;
            if (item.getItemId() < distances.size()) {
                onDistanceSetListener.onDistanceSet(distances.get(item.getItemId()));
            } else {
                DistancePicker distancePicker = new DistancePicker(getContext(), onDistanceSetListener);
                distancePicker.setDistance(((MainActivity) getContext()).mCalcDataVM.getDistance().getValue());
                distancePicker.show();
            }
            return true;
        });
        popupMenu.show();
    }

    public void setDistance(Distance distance) {
        this.distance = distance;
        mDistanceInput.setText(distance.getFullName());
    }

    public void setOnDistanceSetListener(OnDistanceSetListener onDistanceSet) {
        this.onDistanceSetListener = onDistanceSet;
    }
}
