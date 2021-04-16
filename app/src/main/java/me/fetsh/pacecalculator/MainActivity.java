package me.fetsh.pacecalculator;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public CalculatorDataVM mCalcDataVM;
    private static final List<Distance> mSplits = Distance.splits();
    private static final String[] mSplitPickerItems;
    static {
        String[] splitNames = mSplits.stream().map(Distance::getFullName).toArray(String[]::new);
        mSplitPickerItems = new String[splitNames.length + 1];
        System.arraycopy(splitNames, 0, mSplitPickerItems, 1, splitNames.length);
    }

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
        mCalcDataVM.getSplit().observe(this, split -> {
            mCalcDataVM.updateDistancesWithSplit(split);
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
            showAlertDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showAlertDialog() {
        mSplitPickerItems[0] = getString(R.string.split_auto);
        int checkedItem;
        if (mCalcDataVM.getSplit().getValue() == null) {
            checkedItem = 0;
        } else {
            checkedItem = mSplits.indexOf(mCalcDataVM.getSplit().getValue()) + 1;
        }
        new AlertDialog.Builder(new ContextThemeWrapper(MainActivity.this, R.style.Theme_PaceCalculator_AlertDialog))
                .setTitle(R.string.set_split)
                .setSingleChoiceItems(mSplitPickerItems, checkedItem, (dialog, which) -> {
                    if (which == 0) {
                        mCalcDataVM.setSplit(null);
                    } else {
                        mCalcDataVM.setSplit(mSplits.get(which - 1));
                    }
                    dialog.cancel();
                })
                .create()
                .show();
    }
}