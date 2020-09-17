package com.example.cit_app.other_activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.cit_app.R;

public class TrainingsetFinished extends AppCompatActivity {

    private Button showResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainingset_finished);
        getSupportActionBar().setTitle(getResources().getString(R.string.TrainingsetFinished)); // for set actionbar title
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        showResults = (Button) findViewById(R.id.results);
        showResults.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ResultsPerDay.class);
                intent.putExtra("trainingset", true);
                SharedPreferences pref = getApplicationContext().getSharedPreferences("ExerciseFinished", 0);
                SharedPreferences.Editor editor = pref.edit();
                editor.putBoolean("Finished", true);
                editor.apply();
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