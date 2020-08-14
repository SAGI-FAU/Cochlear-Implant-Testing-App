package com.example.cit_app.other_activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.cit_app.R;
import com.example.cit_app.exercises.MinimalPairs2;

public class MinimalPairsExerciseFinished extends AppCompatActivity {

    private Button done, again;
    private TextView score;
    private ProgressBar starsPercentage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minimal_pairs_exercise_finished);
        getSupportActionBar().setTitle(getResources().getString(R.string.GeneralRepetitionFinished)); // for set actionbar title
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
            finish();
        return super.onOptionsItemSelected(item);
    }
}
