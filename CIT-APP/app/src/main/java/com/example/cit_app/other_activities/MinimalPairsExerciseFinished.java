package com.example.cit_app.other_activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.cit_app.MinimalPairsAdapter;
import com.example.cit_app.R;
import com.example.cit_app.exercises.MinimalPairs2;

import java.util.Objects;

public class MinimalPairsExerciseFinished extends AppCompatActivity implements MinimalPairsAdapter.OnExerciseListener{

    private String[] correct_words;
    private MediaPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minimal_pairs_exercise_finished);
        Objects.requireNonNull(getSupportActionBar()).setTitle(getResources().getString(R.string.GeneralRepetitionFinished)); // for set actionbar title
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //Initialize
        CardView done = findViewById(R.id.doneButton);
        CardView again = findViewById(R.id.againButton);
        TextView score = findViewById(R.id.score);
        ProgressBar starsPercentage = (ProgressBar) findViewById(R.id.minimalPairsPercentage);
        RecyclerView exerciseList = (RecyclerView) findViewById(R.id.correct_words);
        int[] images = getIntent().getIntArrayExtra("images");
        String[] chosen_words = getIntent().getStringArrayExtra("results");
        correct_words = getIntent().getStringArrayExtra("cw");
        MinimalPairsAdapter minimalPairsAdapter = new MinimalPairsAdapter(this, correct_words, chosen_words, images, this);
        exerciseList.setAdapter(minimalPairsAdapter);
        exerciseList.setLayoutManager(new LinearLayoutManager(this));
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
            finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onExerciseClick(int position) {

        if (player != null) {
            player.stop();
            player.release();
            player = null;
        }

        String file = correct_words[position].toLowerCase();
        int resId = getResources().getIdentifier(file, "raw", getPackageName());
        String path = "a" + resId;
        String path2 = path.substring(1);
        player = MediaPlayer.create(this, Integer.parseInt(path2));
        player.start();
    }
}
