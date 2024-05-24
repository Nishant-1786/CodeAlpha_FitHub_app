package com.example.fithub;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class two extends AppCompatActivity implements SensorEventListener {
    TextView stepscount, Calories, count, reset;
    Button logout, Nextsteps, newactivity, workout;
    private ProgressBar progressBarCircular;
    private SensorManager msensorManager = null;
    private static final int ADD_ACTIVITY_REQUEST = 1;
    private Sensor stepSensor;
    public int totalstep = 0;
    public int previousTotalstep = 0;
    private int additionalCalories = 0;

    ImageView profile;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private static final double CALORIES_PER_STEP = 0.05;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_two);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);

            logout = findViewById(R.id.logout);
            Nextsteps = findViewById(R.id.Nextstep);
            firebaseAuth = FirebaseAuth.getInstance();
            profile = findViewById(R.id.profile);
            stepscount = findViewById(R.id.stepCount);
            count = findViewById(R.id.count);
            Calories = findViewById(R.id.caloriesBurn);
            newactivity = findViewById(R.id.newactivity);
            reset = findViewById(R.id.reset);
            workout = findViewById(R.id.workout);
            profile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(two.this, com.example.fithub.profile.class);
                    startActivity(intent);
                }
            });
            workout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(two.this, workout.class);
                    startActivity(intent);
                }
            });
            progressBarCircular = findViewById(R.id.progressBarCircular);
            Nextsteps.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(two.this, StepHistoryActivity.class);
                    intent.putExtra("additionalCalories", additionalCalories);
                    startActivity(intent);
                }
            });

            resetStep();
            loadData();
            newactivity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(two.this, addActivities.class);
                    startActivity(intent);
                }
            });
            msensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
            stepSensor = msensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
            msensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_NORMAL);
            firebaseAuth = FirebaseAuth.getInstance();

            logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    firebaseAuth.signOut();
                    Intent intent = new Intent(two.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
            return insets;
        });
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            totalstep = (int) event.values[0];
            int currentSteps = totalstep - previousTotalstep;
            double caloriesBurned = currentSteps * CALORIES_PER_STEP + additionalCalories;
            Calories.setText(" Calories:  " + String.format("%.2f", caloriesBurned));
            Log.d("StepCounter", "Total Steps: " + totalstep);
            Log.d("StepCounter", "Current Steps: " + currentSteps);
            stepscount.setText("Step count: " + currentSteps);
            count.setText(String.valueOf(currentSteps));
            progressBarCircular.setProgress(currentSteps);
            saveDailySteps(currentSteps);
        }
    }

    private void saveDailySteps(int currentSteps) {
        SharedPreferences sharedPref = getSharedPreferences("stepsData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        String day = new SimpleDateFormat("EEEE", Locale.getDefault()).format(new Date());
        editor.putInt(day, currentSteps);
        editor.apply();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_ACTIVITY_REQUEST && resultCode == RESULT_OK) {
            int calories = data.getIntExtra("calories", 0);
            additionalCalories += calories;
            double caloriesBurned = (totalstep - previousTotalstep) * CALORIES_PER_STEP + additionalCalories;
            Calories.setText(" Calories:  " + String.format("%.2f", caloriesBurned));
            saveData();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void resetStep() {
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previousTotalstep = totalstep;
                additionalCalories = 0;
                stepscount.setText("Step count: 0");
                count.setText(String.valueOf(0));
                progressBarCircular.setProgress(0);
                Calories.setText(" Calories: 0.00");
                saveData();
            }
        });
    }

    private void saveData() {
        SharedPreferences sharedPref = getSharedPreferences("mypref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("Key1", previousTotalstep);
        editor.putInt("additionalCalories", additionalCalories);
        editor.apply();
    }

    private void loadData() {
        SharedPreferences sharedPref = getSharedPreferences("mypref", Context.MODE_PRIVATE);
        previousTotalstep = sharedPref.getInt("Key1", 0);
        additionalCalories = sharedPref.getInt("additionalCalories", 0);
    }
}
