package me.fetsh.pacecalculator;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

// Create the basic adapter extending from RecyclerView.Adapter
// Note that we specify the custom ViewHolder which gives us access to our views
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

        TextView distanceTV = holder.distanceView;
        TextView timeTV = holder.timeView;

        if (distanceIsImportant(distance)) {
            distanceTV.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            timeTV.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            holder.itemView.setBackgroundResource(R.color.lightGray);
        } else if (distanceIsSemiImportant(distance)) {
            distanceTV.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            timeTV.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            holder.itemView.setBackgroundResource(R.color.lightGray);
        } else {
            distanceTV.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            timeTV.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            holder.itemView.setBackgroundResource(R.color.lightestGray);
        }

        if (distance.getDistance() == 0) {
            distanceTV.setText(R.string.distance);
            timeTV.setText(R.string.time);
        } else {
            distanceTV.setText(distance.toString());
            timeTV.setText(mPace.multipliedBy(distance.getDistance()).toString());
        }
    }

    private boolean distanceIsImportant(Distance distance) {
        return distance instanceof Distance.NamedDistance || distance.getDistance() == 0;
    }

    private boolean distanceIsSemiImportant(Distance distance) {
        return (int) distance.getDistance() % 5 == 0;
    }

    @Override
    public int getItemCount() {
        return mDistances.size();
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView distanceView;
        public TextView timeView;
        public ConstraintLayout parentView;


        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            parentView = itemView.findViewById(R.id.parent);
            distanceView = itemView.findViewById(R.id.distance);
            timeView = itemView.findViewById(R.id.time);
        }

    }
}