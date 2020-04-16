package com.example.cit_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MinimalPairsExerciseFinished extends AppCompatActivity {

    private Button done, again;
    private TextView score;
    private ProgressBar starsPercentage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minimal_pairs_exercise_finished);
        done = (Button) findViewById(R.id.doneButton);
        again = (Button) findViewById(R.id.againButton);
        score = (TextView) findViewById(R.id.score);
        starsPercentage = (ProgressBar) findViewById(R.id.minimalPairsPercentage);
        int correct = getIntent().getIntExtra("correct", 0);
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
}
