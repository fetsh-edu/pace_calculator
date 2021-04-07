package me.fetsh.pacecalculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    public CalculatorDataVM mCalcDataVM;

    private PaceInput mPaceInput;
    private SpeedInput mSpeedInput;
    private TimeInput mTimeInput;
    private DistanceInput mDistanceInput;

    private final DistancesAdapter mAdapter = new DistancesAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        SharedPreferences preferences = getSharedPreferences("RunningCalc", MODE_PRIVATE);
        ViewModelProvider.Factory factory = new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new CalculatorDataVM(preferences);
            }
        };

        mCalcDataVM = new ViewModelProvider(this, factory).get(CalculatorDataVM.class);

        mPaceInput = findViewById(R.id.pace_picker);
        mSpeedInput = findViewById(R.id.speed_picker);
        mDistanceInput = findViewById(R.id.distance_picker);
        mTimeInput = findViewById(R.id.time_picker);

        mPaceInput.setOnPaceSetListener(pace -> mCalcDataVM.setPace(pace));
        mDistanceInput.setOnDistanceSetListener(distance -> mCalcDataVM.setDistance(distance));
        mTimeInput.setOnTimeSetListener(time -> mCalcDataVM.setTime(time));
        mSpeedInput.setOnSpeedSetListener(speed -> mCalcDataVM.setSpeed(speed));

        RecyclerView rvDistances = findViewById(R.id.distance_table);
        rvDistances.setAdapter(mAdapter);
        rvDistances.setLayoutManager(new LinearLayoutManager(this));


        mCalcDataVM.getPace().observe(this, pace -> {
            mPaceInput.setPace(pace);
        });
        mCalcDataVM.getSpeed().observe(this, speed -> {
            mSpeedInput.setSpeed(speed);
        });
        mCalcDataVM.getDistance().observe(this, distance -> {
            mDistanceInput.setDistance(distance);
        });
        mCalcDataVM.getTime().observe(this, time -> {
            mTimeInput.setTime(time);
        });
        mCalcDataVM.getSplits().observe(this, splits -> {
            mAdapter.setPace(mCalcDataVM.getPace().getValue());
            mAdapter.setDistances(splits);
            mAdapter.notifyDataSetChanged();
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_change_split_size) {
            android.util.Log.e("Calc", "Change pace");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}