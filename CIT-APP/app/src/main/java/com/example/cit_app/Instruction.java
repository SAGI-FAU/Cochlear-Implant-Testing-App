package com.example.cit_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class Instruction extends AppCompatActivity {

    TextView title, description, instruction;
    Button start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instruction);
        final Intent intent = getIntent();
        title = (TextView) findViewById(R.id.exerciseTitle);
        description = (TextView) findViewById(R.id.exerciseDescription);
        instruction = (TextView) findViewById(R.id.exerciseInstruction);
        start = (Button) findViewById(R.id.startExercise);
        title.setText(intent.getStringExtra("title"));
        description.setText(intent.getStringExtra("description"));
        instruction.setText(intent.getStringExtra("instruction"));
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newActivity;
                switch(intent.getStringExtra("title")) {
                    case "Minimal pairs":
                    case "Minimalpaare":
                        newActivity = new Intent(v.getContext(), MinimalPairs.class);
                        newActivity.putExtra("trainingset", intent.getExtras().getBoolean("trainingset"));
                        break;
                    case "Minimal pairs 2":
                    case "Minimalpaare2":
                        newActivity = new Intent(v.getContext(), MinimalPairs2.class);
                        newActivity.putExtra("trainingset", intent.getExtras().getBoolean("trainingset"));
                        break;
                    case "Word list":
                    case "Wortliste":
                        newActivity = new Intent(v.getContext(), Word_List.class);
                        newActivity.putExtra("trainingset", intent.getExtras().getBoolean("trainingset"));
                        break;
                    case "Sentence reading":
                    case "Sätze lesen":
                        newActivity = new Intent(v.getContext(), ReadingOfSentences.class);
                        newActivity.putExtra("trainingset", intent.getExtras().getBoolean("trainingset"));
                        break;
                    case "Syllable repetition":
                    case "Silben wiederholen":
                        newActivity = new Intent(v.getContext(), SyllableRepetition.class);
                        newActivity.putExtra("trainingset", intent.getExtras().getBoolean("trainingset"));
                        break;
                    case "Picture description":
                    case "Bildbeschreibung":
                        newActivity = new Intent(v.getContext(), Picture_Description.class);
                        newActivity.putExtra("trainingset", intent.getExtras().getBoolean("trainingset"));
                        break;
                    default:
                        newActivity = new Intent(v.getContext(), MainActivity.class);
                        break;
                }
                v.getContext().startActivity(newActivity);
            }
        });
    }
}
