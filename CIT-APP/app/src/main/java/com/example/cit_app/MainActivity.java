package com.example.cit_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button trainingsetButton, exercisesButton, settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        trainingsetButton = (Button) findViewById(R.id.trainingset);
        exercisesButton = (Button) findViewById(R.id.exercises);
        settings = (Button) findViewById(R.id.settings);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSettings();
            }
        });
        exercisesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openExercises();
            }
        });
        trainingsetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTrainingset();
            }
        });
        SharedPreferences prefs = getSharedPreferences("LoginPref", this.MODE_PRIVATE);
        int login = prefs.getInt("UserCreated", 0);
        if (login != 1) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("UserCreated",1);
            editor.commit();
            Intent intent = new Intent(this, Login.class);
            this.startActivity(intent);
        }
    }
    //Open new Activity showing all available exercises
    public void openExercises() {
        Intent exercises = new Intent(this, Exercises.class);
        startActivity(exercises);
    }
    //Open new Activity showing a trainingset used for evaluation
    public void openTrainingset() {
        Intent trainingset = new Intent(this, Trainingset.class);
        startActivity(trainingset);
    }
    public void openSettings() {
        Intent settings = new Intent(this, Settings.class);
        startActivity(settings);
    }
}
