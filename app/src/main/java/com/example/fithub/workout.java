package com.example.fithub;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class workout extends AppCompatActivity {
    private Spinner exerciseSpinner;
    private ImageView gifImageView;
    Button yoga;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_workout);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            exerciseSpinner = findViewById(R.id.exercise_spinner);
            gifImageView = findViewById(R.id.gif);
            yoga = findViewById(R.id.yoga_youtube);
            yoga.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    gotoUrl("https://www.youtube.com/watch?v=dAqQqmaI9vY&t=82s&ab_channel=FitTuber");
                }
            });



            String[] exercises = {"Squat", "Glute Bridge", "Arm Circles", "Push-up", "Plank", "Lunge"};

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, exercises);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            exerciseSpinner.setAdapter(adapter);

            exerciseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String selectedExercise = (String) parent.getItemAtPosition(position);
                    updateGifImage(selectedExercise);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // Do nothing
                }
            });
            return insets;
        });
    }
    void  gotoUrl(String s){
        Uri uri = Uri.parse(s);
        startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }
    private void updateGifImage(String exercise) {
        int gifResource = 0;
        switch (exercise.toLowerCase()) {
            case "squat":
                gifResource = R.drawable.squat;
                break;
            case "glute bridge":
                gifResource = R.drawable.glute_bridge;
                break;
            case "arm circles":
                gifResource = R.drawable.arm_circles;
                break;
            case "push-up":
                gifResource = R.drawable.push_up;
                break;
            case "plank":
                gifResource = R.drawable.plank;
                break;
            case "lunge":
                gifResource = R.drawable.lunge;
                break;
        }
        if (gifResource != 0) {
            gifImageView.setImageResource(gifResource);
        }
    }
}