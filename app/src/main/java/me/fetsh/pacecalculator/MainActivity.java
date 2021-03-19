package me.fetsh.pacecalculator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Calculator calc;

    private PaceInput mPaceInput;
    private SpeedInput mSpeedInput;
    private TimeInput mTimeInput;
    private DistanceInput mDistanceInput;

    private final DistancesAdapter mAdapter = new DistancesAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPaceInput = findViewById(R.id.pace_picker);
        mSpeedInput = findViewById(R.id.speed_picker);
        mDistanceInput = findViewById(R.id.distance_picker);
        mTimeInput = findViewById(R.id.time_picker);

        calc = new Calculator(this::onDataCalculated);

        mPaceInput.setOnPaceSetListener(this::onPaceSet);
        mTimeInput.setOnTimeSetListener(this::onTimeSet);
        mSpeedInput.setOnSpeedSetListener(this::onSpeedSet);
        mDistanceInput.setOnDistanceSetListener(this::onDistanceSet);

        RecyclerView rvDistances = findViewById(R.id.distance_table);
        rvDistances.setAdapter(mAdapter);
        rvDistances.setLayoutManager(new LinearLayoutManager(this));
    }

    private void onDataCalculated(Calculator calculator) {
        mPaceInput.setPace(calculator.getPace());
        mSpeedInput.setSpeed(calculator.getSpeed());
        mDistanceInput.setDistance(calculator.getDistance());
        mTimeInput.setTime(calculator.getTime());

        // TODO: Get distances from calculator
        mAdapter.setPace(calculator.getPace());
        mAdapter.setDistances(Distance.all(new Distance(0, calculator.getPace().getDistance().getUnit()), calculator.getDistance(), calculator.getPace().getDistance()));
        mAdapter.notifyDataSetChanged();
    }

    private void onPaceSet(Pace pace) {
        calc.calculateWith(pace);
    }
    private void onTimeSet(Time time) { calc.calculateWithTime(time);}
    private void onSpeedSet(Speed speed) { calc.calculateWithSpeed(speed);}
    private void onDistanceSet(Distance distance) { calc.calculateWithDistance(distance);}

}