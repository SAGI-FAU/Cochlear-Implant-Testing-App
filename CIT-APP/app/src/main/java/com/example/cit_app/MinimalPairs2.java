package com.example.cit_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class MinimalPairs2 extends AppCompatActivity {

    private Random random = new Random();
    private String[] minimal_pairs = new String[80];
    private int position = 0;
    private int counter = 0;
    private int oldPos = 0;
    private String[] minimal_pairs_fricatives, minimal_pairs_stops, minimal_pairs_general, minimal_pairs_result, minimal_pairs_correct;
    private Button start, leftLeft, rightRight, midLeft, midRight;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minimal_pairs2);
        //initialize buttons and lists
        start = (Button) findViewById(R.id.startButton);
        leftLeft = (Button) findViewById(R.id.leftLeft);
        rightRight = (Button) findViewById(R.id.rightRight);
        midLeft = (Button) findViewById(R.id.midLeft);
        midRight = (Button) findViewById(R.id.midRight);
        ArrayList<Integer> usedNumber = new ArrayList<>();
        minimal_pairs_fricatives = getResources().getStringArray(R.array.Minimal_Pairs_Fricatives);
        minimal_pairs_stops = getResources().getStringArray(R.array.Minimal_Pairs_Stops);
        minimal_pairs_general = getResources().getStringArray(R.array.Minimal_Pairs);
        minimal_pairs_result = new String[20];
        minimal_pairs_correct = new String[20];
        //fill minimal_pairs list with 20 pairs consisting of 10 random 5 fricative and 5 stop pairs
        for(int i = 0; i < 20; i++) {
            position = 4 * random.nextInt(50);
            if(i < 10) {
                while(usedNumber.contains(position)) {
                    position = 4 * random.nextInt(50);
                }
                minimal_pairs_correct[i] = minimal_pairs_general[position];
                minimal_pairs[i * 4] = minimal_pairs_general[position];
                minimal_pairs[i * 4 + 1] = minimal_pairs_general[position + 1];
                minimal_pairs[i * 4 + 2] = minimal_pairs_general[position + 2];
                minimal_pairs[i * 4 + 3] = minimal_pairs_general[position + 3];

                usedNumber.add(position);
            } else {
                if(i < 15) {
                    if(i == 10) {
                        usedNumber.clear();
                    }
                    position = 4 * random.nextInt(13);
                    while(usedNumber.contains(position)) {
                        position = 4 * random.nextInt(13);
                    }
                    minimal_pairs_correct[i] = minimal_pairs_fricatives[position];
                    minimal_pairs[i * 4] = minimal_pairs_fricatives[position];
                    minimal_pairs[i * 4 + 1] = minimal_pairs_fricatives[position + 1];
                    minimal_pairs[i * 4 + 2] = minimal_pairs_fricatives[position + 2];
                    minimal_pairs[i * 4 + 3] = minimal_pairs_fricatives[position + 3];
                    usedNumber.add(position);
                } else {
                    if(i == 15) {
                        usedNumber.clear();
                    }
                    position = 4 * random.nextInt(7);
                    while(usedNumber.contains(position)) {
                        position = 4 * random.nextInt(7);
                    }
                    minimal_pairs_correct[i] = minimal_pairs_stops[position];
                    minimal_pairs[i * 4] = minimal_pairs_stops[position];
                    minimal_pairs[i * 4 + 1] = minimal_pairs_stops[position + 1];
                    minimal_pairs[i * 4 + 2] = minimal_pairs_stops[position + 2];
                    minimal_pairs[i * 4 + 3] = minimal_pairs_stops[position + 3];
                    usedNumber.add(position);
                }
            }
        }
        //set text and action of the buttons
        position = random.nextInt(3);
        leftLeft.setText(minimal_pairs[position % 4]);
        midLeft.setText(minimal_pairs[(position + 1) % 4]);
        midRight.setText(minimal_pairs[(position + 2) % 4]);
        rightRight.setText(minimal_pairs[(position + 3) % 4]);
        oldPos += 4;
        leftLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(oldPos >= 80) {
                    minimal_pairs_result[counter] = leftLeft.getText().toString();
                    Intent intent = new Intent(MinimalPairs2.this, MinimalPairsExerciseFinished.class);
                    int correct = 0;
                    for(int i = 0; i < 20; i++) {
                        if(minimal_pairs_result[i].equals(minimal_pairs_correct[i])) {
                            correct++;
                        }
                    }
                    intent.putExtra("correct", correct);
                    v.getContext().startActivity(intent);
                } else {
                    position += random.nextInt(3);
                    minimal_pairs_result[counter] = leftLeft.getText().toString();
                    counter++;
                    leftLeft.setText(minimal_pairs[oldPos + (position % 4)]);
                    midLeft.setText(minimal_pairs[oldPos + ((position + 1) % 4)]);
                    midRight.setText(minimal_pairs[oldPos + ((position + 2) % 4)]);
                    rightRight.setText(minimal_pairs[oldPos + ((position + 3) % 4)]);
                    oldPos += 4;
                }
            }
        });
        midLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(oldPos >= 80) {
                    minimal_pairs_result[counter] = midLeft.getText().toString();
                    Intent intent = new Intent(MinimalPairs2.this, MinimalPairsExerciseFinished.class);
                    int correct = 0;
                    for(int i = 0; i < 20; i++) {
                        if(minimal_pairs_result[i].equals(minimal_pairs_correct[i])) {
                            correct++;
                        }
                    }
                    intent.putExtra("correct", correct);
                    v.getContext().startActivity(intent);
                } else {
                    position = random.nextInt(3);
                    minimal_pairs_result[counter] = midLeft.getText().toString();
                    counter++;
                    leftLeft.setText(minimal_pairs[oldPos + (position % 4)]);
                    midLeft.setText(minimal_pairs[oldPos + ((position + 1) % 4)]);
                    midRight.setText(minimal_pairs[oldPos + ((position + 2) % 4)]);
                    rightRight.setText(minimal_pairs[oldPos + ((position + 3) % 4)]);
                    oldPos += 4;
                }
            }
        });
        midRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(oldPos >= 80) {
                    minimal_pairs_result[counter] = midRight.getText().toString();
                    Intent intent = new Intent(MinimalPairs2.this, MinimalPairsExerciseFinished.class);
                    int correct = 0;
                    for(int i = 0; i < 20; i++) {
                        if(minimal_pairs_result[i].equals(minimal_pairs_correct[i])) {
                            correct++;
                        }
                    }
                    intent.putExtra("correct", correct);
                    v.getContext().startActivity(intent);
                } else {
                    position = random.nextInt(3);
                    minimal_pairs_result[counter] = midRight.getText().toString();
                    counter++;
                    leftLeft.setText(minimal_pairs[oldPos + (position % 4)]);
                    midLeft.setText(minimal_pairs[oldPos + ((position + 1) % 4)]);
                    midRight.setText(minimal_pairs[oldPos + ((position + 2) % 4)]);
                    rightRight.setText(minimal_pairs[oldPos + ((position + 3) % 4)]);
                    oldPos += 4;
                }
            }
        });
        rightRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(oldPos >= 80) {
                    minimal_pairs_result[counter] = rightRight.getText().toString();
                    Intent intent = new Intent(MinimalPairs2.this, MinimalPairsExerciseFinished.class);
                    int correct = 0;
                    for(int i = 0; i < 20; i++) {
                        if(minimal_pairs_result[i].equals(minimal_pairs_correct[i])) {
                            correct++;
                        }
                    }
                    intent.putExtra("correct", correct);
                    v.getContext().startActivity(intent);
                } else {
                    position = random.nextInt(3);
                    minimal_pairs_result[counter] = rightRight.getText().toString();
                    counter++;
                    leftLeft.setText(minimal_pairs[oldPos + (position % 4)]);
                    midLeft.setText(minimal_pairs[oldPos + ((position + 1) % 4)]);
                    midRight.setText(minimal_pairs[oldPos + ((position + 2) % 4)]);
                    rightRight.setText(minimal_pairs[oldPos + ((position + 3) % 4)]);
                    oldPos += 4;
                }
            }
        });
    }


}
