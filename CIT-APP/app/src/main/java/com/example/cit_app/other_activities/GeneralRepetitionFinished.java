package com.example.cit_app.other_activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.example.cit_app.R;
import com.example.cit_app.exercises.MinimalPairs;
import com.example.cit_app.exercises.ReadingOfSentences;
import com.example.cit_app.exercises.SyllableRepetition;
import com.example.cit_app.exercises.Word_List;

public class GeneralRepetitionFinished extends AppCompatActivity {

    Button done, again;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_general_repetition_finished);
        getSupportActionBar().setTitle(getResources().getString(R.string.GeneralRepetitionFinished)); // for set actionbar title
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        done = (Button) findViewById(R.id.done);
        again = (Button) findViewById(R.id.again);
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
                Intent intent;
                switch(getIntent().getStringExtra("exercise")) {
                    case "SyllableRepetition": intent = new Intent(v.getContext(), SyllableRepetition.class); break;
                    case "Word_List": intent = new Intent(v.getContext(), Word_List.class); break;
                    case "ReadingOfSentences": intent = new Intent(v.getContext(), ReadingOfSentences.class); break;
                    case "MinimalPairs": intent = new Intent(v.getContext(), MinimalPairs.class);break;
                    default: intent = new Intent(v.getContext(), MainActivity.class);
                }
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
