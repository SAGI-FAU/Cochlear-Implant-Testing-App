package com.example.cit_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
                        newActivity = new Intent(v.getContext(), MinimalPairs.class);
                        break;
                    case "Minimal pairs 2":
                        newActivity = new Intent(v.getContext(), MinimalPairs2.class);
                        break;
                    case "Word list":
                        newActivity = new Intent(v.getContext(), Word_List.class);
                        break;
                    case "Sentence reading":
                        newActivity = new Intent(v.getContext(), Instruction.class);
                        break;
                    case "Syllable repetition":
                        newActivity = new Intent(v.getContext(), SyllableRepetition.class);
                        break;
                    case "Picture description":
                        newActivity = new Intent(v.getContext(), Picture_Description.class);
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
