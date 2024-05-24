package com.example.fithub;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

public class StepHistoryActivity extends AppCompatActivity {

    private TableLayout tableLayout;
    private Map<String, Integer> stepsData = new HashMap<>();
    private Map<String, Double> caloriesBurnedData = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_history);

        tableLayout = findViewById(R.id.tableLayout);
        Typeface typeface = getResources().getFont(R.font.slabo);
        loadStepsData();
        displayStepsData();
    }

    private void loadStepsData() {
        SharedPreferences sharedPref = getSharedPreferences("stepsData", Context.MODE_PRIVATE);
        String[] daysOfWeek = getResources().getStringArray(R.array.days_of_week);
        for (String day : daysOfWeek) {
            stepsData.put(day, sharedPref.getInt(day, 0));
        }
    }



    private void displayStepsData() {
        // Display data in table
        String[] daysOfWeek = getResources().getStringArray(R.array.days_of_week);
        for (String day : daysOfWeek) {
            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

            TextView dayTextView = new TextView(this);
            dayTextView.setText(day);
            dayTextView.setPadding(160, 18, 8, 8);
            dayTextView.setTextColor(Color.BLACK);
            Typeface typeface = getResources().getFont(R.font.slabo);
            dayTextView.setTypeface(typeface);

            TextView stepsTextView = new TextView(this);
            stepsTextView.setText(String.valueOf(stepsData.get(day)));
            stepsTextView.setPadding(330, 18, 8, 8);
            stepsTextView.setTextColor(Color.BLACK);
            stepsTextView.setTypeface(typeface);
            tableRow.addView(dayTextView);
            tableRow.addView(stepsTextView);
            tableLayout.addView(tableRow);

        }
    }
}
