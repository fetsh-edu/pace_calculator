package me.fetsh.pacecalculator;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DistancesAdapter extends RecyclerView.Adapter<DistancesAdapter.ViewHolder> {

    private List<Distance> mDistances;
    private Pace mPace;

    public DistancesAdapter() {
    }

    public DistancesAdapter(List<Distance> distances, Pace pace) {
        mDistances = distances;
        mPace = pace;
    }

    public void setPace(Pace mPace) {
        this.mPace = mPace;
    }

    public void setDistances(List<Distance> distances) {
        this.mDistances = distances;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View distanceView = inflater.inflate(R.layout.distance_row, parent, false);

        return new ViewHolder(distanceView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Distance distance = mDistances.get(position);
        Distance step = mDistances.get(1);

        TextView distanceTV = holder.distanceView;
        TextView timeTV = holder.timeView;

        if (distanceIsImportant(distance) || position == mDistances.size() - 1) {
            distanceTV.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            timeTV.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            holder.itemView.setBackgroundResource(R.color.lightGray);
        } else if (distanceIsSemiImportant(distance, step)) {
            distanceTV.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            timeTV.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            holder.itemView.setBackgroundResource(R.color.lightGray);
        } else {
            distanceTV.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            timeTV.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            holder.itemView.setBackgroundResource(R.color.lightestGray);
        }

        if (distance.getAmount() == 0) {
            distanceTV.setText(R.string.distance);
            distanceTV.append(" (" + step.getSplitDistanceHeader() + ")");
            timeTV.setText(R.string.time);
        } else {
            if (position == mDistances.size() - 1) {
                distanceTV.setText(distance.getFullName());
            } else {
                distanceTV.setText(distance.toString());
            }
            timeTV.setText(mPace.getTime(distance).toString());
        }
    }

    private boolean distanceIsImportant(Distance distance) {
        return distance.getName() != null || distance.getAmount() == 0;
    }

    private boolean distanceIsSemiImportant(Distance distance, Distance step) {
        return ((int) distance.getAmount()) % ((int) step.getAmount() * 5) == 0;
    }

    @Override
    public int getItemCount() {
        return mDistances.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView distanceView;
        public TextView timeView;
        public ConstraintLayout parentView;

        public ViewHolder(View itemView) {
            super(itemView);
            parentView = itemView.findViewById(R.id.parent);
            distanceView = itemView.findViewById(R.id.distance);
            timeView = itemView.findViewById(R.id.time);
        }

    }
}