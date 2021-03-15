package me.fetsh.pacecalculator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Calculator calc;

    private PaceInput mPaceInput;
    private TextView mSpeedInput;
    private TextView mTimeInput;
    private TextView mDistanceInput;

    private DistancesAdapter mAdapter = new DistancesAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPaceInput = findViewById(R.id.pace_picker);
        mSpeedInput = findViewById(R.id.speed_input);
        mDistanceInput = findViewById(R.id.distance_input);
        mTimeInput = findViewById(R.id.time_input);

        calc = new Calculator(this::onDataCalculated);

        mPaceInput.setOnPaceSetListener(this::onPaceSet);

        RecyclerView rvDistances = findViewById(R.id.distance_table);
        rvDistances.setAdapter(mAdapter);
        rvDistances.setLayoutManager(new LinearLayoutManager(this));
    }

    private void onDataCalculated(Calculator calculator) {
        mPaceInput.setPace(calculator.getPace());
        mSpeedInput.setText(calculator.getSpeed().toString());
        mDistanceInput.setText(calculator.getDistance());
        mTimeInput.setText(calculator.getTime().toString());

        // TODO: Get distances from calculator
        mAdapter.setPace(calculator.getPace());
        mAdapter.setDistances(Distance.all());
        mAdapter.notifyDataSetChanged();
    }

    private void onPaceSet(Pace pace) {
        calc.calculateWith(pace);
    }
}