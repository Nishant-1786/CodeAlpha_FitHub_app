package com.example.fithub;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class addActivities extends AppCompatActivity {
    Button fifteen, thirty, fourty, onehour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_activities);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            fifteen = findViewById(R.id.fiften);
            thirty = findViewById(R.id.thirty);
            fourty = findViewById(R.id.fourty);
            onehour = findViewById(R.id.onehour);

            fifteen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addCalories(50);
                }
            });
            thirty.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addCalories(100);
                }
            });

            fourty.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addCalories(150);
                }
            });

            onehour.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addCalories(200);
                }
            });
            return insets;
        });
    }
    private void addCalories(int calories) {
        SharedPreferences sharedPref = getSharedPreferences("mypref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        int currentCalories = sharedPref.getInt("additionalCalories", 0);
        editor.putInt("additionalCalories", currentCalories + calories);
        editor.apply();
        finish();
    }
}