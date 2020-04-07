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

    private String[] minimal_pairs = new String[80];
    private int position = 0;
    private String[] minimal_pairs_fricatives, minimal_pairs_stops, minimal_pairs_general;
    private Button start, leftLeft, rightRight, midLeft, midRight;
    private ArrayList<String> minimal_pairs_result = new ArrayList<>();
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
        Random random = new Random();
        ArrayList<Integer> usedNumber = new ArrayList<>();
        minimal_pairs_fricatives = getResources().getStringArray(R.array.Minimal_Pairs_Fricatives);
        minimal_pairs_stops = getResources().getStringArray(R.array.Minimal_Pairs_Stops);
        minimal_pairs_general = getResources().getStringArray(R.array.Minimal_Pairs);
        //fill minimal_pairs list with 20 pairs consisting of 10 random 5 fricative and 5 stop pairs
        for(int i = 0; i < 20; i++) {
            position = 4 * random.nextInt(50);
            if(i < 10) {
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
                    minimal_pairs[i * 4] = minimal_pairs_stops[position];
                    minimal_pairs[i * 4 + 1] = minimal_pairs_stops[position + 1];
                    minimal_pairs[i * 4 + 2] = minimal_pairs_stops[position + 2];
                    minimal_pairs[i * 4 + 3] = minimal_pairs_stops[position + 3];
                    usedNumber.add(position);
                }
            }
        }
        //set text and action of the buttons
        position = 0;
        leftLeft.setText(minimal_pairs[position]);
        midLeft.setText(minimal_pairs[position + 1]);
        midRight.setText(minimal_pairs[position + 2]);
        rightRight.setText(minimal_pairs[position + 3]);
        position += 4;

        Toast.makeText(MinimalPairs2.this, "Position" + position, Toast.LENGTH_SHORT).show();
        leftLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position >= 80) {
                    Intent intent = new Intent(MinimalPairs2.this, MainActivity.class);
                    v.getContext().startActivity(intent);
                } else {
                    minimal_pairs_result.add(leftLeft.getText().toString());
                    leftLeft.setText(minimal_pairs[position]);
                    midLeft.setText(minimal_pairs[position + 1]);
                    midRight.setText(minimal_pairs[position + 2]);
                    rightRight.setText(minimal_pairs[position + 3]);
                    position += 4;

                    Toast.makeText(MinimalPairs2.this, "Position" + position, Toast.LENGTH_SHORT).show();
                }
            }
        });
        midLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position >= 80) {
                    Intent intent = new Intent(MinimalPairs2.this, MainActivity.class);
                    v.getContext().startActivity(intent);
                } else {
                    minimal_pairs_result.add(midLeft.getText().toString());
                    leftLeft.setText(minimal_pairs[position]);
                    midLeft.setText(minimal_pairs[position + 1]);
                    midRight.setText(minimal_pairs[position + 2]);
                    rightRight.setText(minimal_pairs[position + 3]);
                    position += 4;
                    Toast.makeText(MinimalPairs2.this, "Position" + position, Toast.LENGTH_SHORT).show();
                }
            }
        });
        midRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position >= 80) {
                    Intent intent = new Intent(MinimalPairs2.this, MainActivity.class);
                    v.getContext().startActivity(intent);
                } else {
                    minimal_pairs_result.add(midRight.getText().toString());
                    leftLeft.setText(minimal_pairs[position]);
                    midLeft.setText(minimal_pairs[position + 1]);
                    midRight.setText(minimal_pairs[position + 2]);
                    rightRight.setText(minimal_pairs[position + 3]);
                    position += 4;
                    Toast.makeText(MinimalPairs2.this, "Position" + position, Toast.LENGTH_SHORT).show();
                }
            }
        });
        rightRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position >= 80) {
                    Intent intent = new Intent(MinimalPairs2.this, MainActivity.class);
                    v.getContext().startActivity(intent);
                } else {
                    minimal_pairs_result.add(rightRight.getText().toString());
                    leftLeft.setText(minimal_pairs[position]);
                    midLeft.setText(minimal_pairs[position + 1]);
                    midRight.setText(minimal_pairs[position + 2]);
                    rightRight.setText(minimal_pairs[position + 3]);
                    position += 4;
                    Toast.makeText(MinimalPairs2.this, "Position" + position, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}
