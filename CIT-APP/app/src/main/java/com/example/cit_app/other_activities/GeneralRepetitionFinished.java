/**
 * Created by Christoph Popp
 */

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
import com.example.cit_app.exercises.PictureDescription;
import com.example.cit_app.exercises.ReadingOfSentences;
import com.example.cit_app.exercises.SyllableRepetition;
import com.example.cit_app.exercises.WordList;

import java.util.Objects;

public class GeneralRepetitionFinished extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_general_repetition_finished);
        Objects.requireNonNull(getSupportActionBar()).setTitle(getResources().getString(R.string.GeneralRepetitionFinished)); // for set actionbar title
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Initialize
        Button done = findViewById(R.id.done);
        Button again = findViewById(R.id.again);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MainActivity.class);
                v.getContext().startActivity(intent);
            }
        });
        //Button to repeat the exercise
        again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                switch(Objects.requireNonNull(getIntent().getStringExtra("exercise"))) {
                    case "SyllableRepetition": intent = new Intent(v.getContext(), SyllableRepetition.class); intent.putExtra("word", getIntent().getStringExtra("word")); break;
                    case "Word_List": intent = new Intent(v.getContext(), WordList.class); break;
                    case "ReadingOfSentences": intent = new Intent(v.getContext(), ReadingOfSentences.class); break;
                    case "Picture description": intent = new Intent(v.getContext(), PictureDescription.class);break;
                    case "MinimalPairs": intent = new Intent(v.getContext(), MinimalPairs.class);break;
                    default: intent = new Intent(v.getContext(), MainActivity.class);
                }
                v.getContext().startActivity(intent);
            }
        });
    }

    //This allows you to return to the activity before
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

}
