package com.example.cit_app.other_activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.cit_app.R;
import com.example.cit_app.RadarFeatures;
import com.example.cit_app.data_access.FeatureDataService;
import com.example.cit_app.exercises.MinimalPairs2;
import com.example.cit_app.other_activities.MainActivity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MinimalPairsExerciseFinished extends AppCompatActivity {

    private Button done, again;
    private TextView score;
    private ProgressBar starsPercentage;
    FeatureDataService featureDataService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minimal_pairs_exercise_finished);
        featureDataService = new FeatureDataService(this);
        done = (Button) findViewById(R.id.doneButton);
        again = (Button) findViewById(R.id.againButton);
        score = (TextView) findViewById(R.id.score);
        starsPercentage = (ProgressBar) findViewById(R.id.minimalPairsPercentage);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("Hearing", 0);
        SharedPreferences.Editor edit = pref.edit();
        int correct = getIntent().getIntExtra("correct", 0);
        edit.putFloat("HearingAbility",(float)(((double) correct / (double) 20)));
        edit.apply();
        score.setText(correct + " " + "/ 20");
        double progress = 100 * ((double) correct / (double) 20);
        starsPercentage.setProgress((int)progress, true);
        float int_f0 = (float)((double) correct / (double) 20);
        featureDataService.save_feature(featureDataService.hearing_name, Calendar.getInstance().getTime(), int_f0);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MainActivity.class);
                v.getContext().startActivity(intent);
            }
        });
        again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MinimalPairs2.class);
                v.getContext().startActivity(intent);
            }
        });

    }
}