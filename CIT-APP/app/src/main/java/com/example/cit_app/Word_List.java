package com.example.cit_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class Word_List extends AppCompatActivity {

    TextView word;
    Button record;
    Random rand = new Random();
    int counter = 1;
    ArrayList<Integer> usedInts = new ArrayList<>();
    String[] wordListPlosive, wordListFricative, wordListNasal, wordListSibilant, wordList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word__list);
        wordList = new String[10];
        wordListFricative = getResources().getStringArray(R.array.Word_List_Fricatives);
        wordListPlosive = getResources().getStringArray(R.array.Word_List_Plosives);
        wordListNasal = getResources().getStringArray(R.array.Word_List_Nasal);
        wordListSibilant = getResources().getStringArray(R.array.Word_List_Sibilant);
        word = (TextView) findViewById(R.id.word);
        record = (Button) findViewById(R.id.recordButtonWordList);
        for(int i = 0; i < 10; i++) {
            int tmp = -1;
            if(i < 2) {
                while((tmp == -1) || usedInts.contains(tmp)) {
                    tmp = rand.nextInt(63);
                }
                wordList[i] = wordListPlosive[tmp];
                usedInts.add(tmp);
            } else {
                if(i < 4) {
                    while((tmp == -1) || usedInts.contains(tmp)) {
                        tmp = rand.nextInt(10);
                    }
                    wordList[i] = wordListNasal[tmp];
                    usedInts.add(tmp);
                } else {
                    if(i < 6) {
                        while((tmp == -1) || usedInts.contains(tmp)) {
                            tmp = rand.nextInt(10);
                        }
                        wordList[i] = wordListSibilant[tmp];
                        usedInts.add(tmp);
                    } else {
                        while((tmp == -1) || usedInts.contains(tmp)) {
                            tmp = rand.nextInt(14);
                        }
                        wordList[i] = wordListFricative[tmp];
                        usedInts.add(tmp);
                    }
                }
            }
        }

        word.setText(wordList[0]);
        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO record sound
                if(counter < 10) {
                    word.setText(wordList[counter]);
                    counter++;
                } else {
                    Intent intent = new Intent(v.getContext(), GeneralRepetitionFinished.class);
                    intent.putExtra("exercise", "Word_List");
                    v.getContext().startActivity(intent);
                }
            }
        });

    }
}
