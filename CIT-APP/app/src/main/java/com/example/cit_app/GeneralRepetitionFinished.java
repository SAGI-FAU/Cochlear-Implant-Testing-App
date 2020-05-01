package com.example.cit_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class GeneralRepetitionFinished extends AppCompatActivity {

    Button done, again;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_repetition_finished);
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
                    default: intent = new Intent(v.getContext(), MainActivity.class);
                }
                v.getContext().startActivity(intent);
            }
        });
    }
}