package com.example.cit_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

public class Instruction extends AppCompatActivity {

    TextView title, description, instruction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instruction);
        Intent intent = getIntent();
        title = (TextView) findViewById(R.id.exerciseTitle);
        description = (TextView) findViewById(R.id.exerciseDescription);
        instruction = (TextView) findViewById(R.id.exerciseInstruction);
        title.setText(intent.getStringExtra("title"));
        description.setText(intent.getStringExtra("description"));
        instruction.setText(intent.getStringExtra("instruction"));
    }
}
