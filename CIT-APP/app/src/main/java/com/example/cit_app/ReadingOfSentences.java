package com.example.cit_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

public class ReadingOfSentences extends AppCompatActivity {

    String[] sentences;
    TextView text;
    Random rand;
    Button record;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading_of_sentences);
        sentences = getResources().getStringArray(R.array.Sentences);
        text = (TextView) findViewById(R.id.sentences);
        record = (Button) findViewById(R.id.recordSentences);
        rand = new Random();

        int pos = rand.nextInt(10);
        text.setText(sentences[pos]);
        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), GeneralRepetitionFinished.class);
                intent.putExtra("exercise", "ReadingOfSentences");
                v.getContext().startActivity(intent);
            }
        });

    }
}
