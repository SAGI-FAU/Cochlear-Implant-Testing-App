/**
 * Created by Christoph Popp
 */

package com.example.cit_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.cit_app.exercises.MinimalPairs;
import com.example.cit_app.other_activities.Instruction;
import com.example.cit_app.other_activities.MainActivity;

import java.util.Random;

//This class is the view showing the exercise list
public class Exercises extends AppCompatActivity {

    private CardView minPairs, minPairs2, wordList, pictureDescription, sentenceReading, repetition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_exercises);
        getSupportActionBar().setTitle(getResources().getString(R.string.exerciseList)); // for set actionbar title
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //initialize
        minPairs = (CardView) findViewById(R.id.minpairs_cardview);
        minPairs2 = (CardView) findViewById(R.id.minpairs2_cardview);
        wordList = (CardView) findViewById(R.id.wordlist_cardview);
        pictureDescription = (CardView) findViewById(R.id.picture_cardview);
        sentenceReading= (CardView) findViewById(R.id.sentence_cardview);
        repetition = (CardView) findViewById(R.id.repetition_cardview);
        minPairs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Instruction.class);
                intent.putExtra("title", getResources().getString(R.string.MinimalPairs));
                intent.putExtra("description", getResources().getString(R.string.DescriptionMinPairs));
                intent.putExtra("instruction", getResources().getString(R.string.ExplanationMinPairs));
                intent.putExtra("trainingset", false);
                v.getContext().startActivity(intent);
            }
        });
        minPairs2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Instruction.class);
                intent.putExtra("title", getResources().getString(R.string.MinimalPairs2));
                intent.putExtra("description", getResources().getString(R.string.DescriptionMinPairs2));
                intent.putExtra("instruction", getResources().getString(R.string.ExplanationMinPairs2));
                intent.putExtra("trainingset", false);
                v.getContext().startActivity(intent);
            }
        });
        wordList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Instruction.class);
                intent.putExtra("title", getResources().getString(R.string.WordList));
                intent.putExtra("description", getResources().getString(R.string.DescriptionWordList));
                intent.putExtra("instruction", getResources().getString(R.string.ExplanationWordList));
                intent.putExtra("trainingset", false);
                v.getContext().startActivity(intent);
            }
        });
        pictureDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Instruction.class);
                intent.putExtra("title", getResources().getString(R.string.PictureDescription));
                intent.putExtra("description", getResources().getString(R.string.DescriptionPictureDescription));
                intent.putExtra("instruction", getResources().getString(R.string.ExplanationPictureDescription));
                intent.putExtra("trainingset", false);
                v.getContext().startActivity(intent);
            }
        });
        sentenceReading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Instruction.class);
                intent.putExtra("title", getResources().getString(R.string.SentenceReading));
                intent.putExtra("description", getResources().getString(R.string.DescriptionSentenceReading));
                intent.putExtra("instruction", getResources().getString(R.string.ExplanationSentenceReading));
                intent.putExtra("trainingset", false);
                v.getContext().startActivity(intent);
            }
        });
        repetition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Instruction.class);
                intent.putExtra("title", getResources().getString(R.string.SyllableRepetition));
                intent.putExtra("description", getResources().getString(R.string.DescriptionSyllableRepetition));
                String s;
                String s1;
                String s2;
                Random r = new Random();
                int i = r.nextInt(7);
                switch (i) {
                    case 0:
                        s = getResources().getString(R.string.pataka);
                        s1 = getResources().getString(R.string.ExamplePataka);
                        s2 = "Pataka";
                        break;
                    case 1:
                        s = getResources().getString(R.string.petaka);
                        s1 = getResources().getString(R.string.ExamplePetaka);
                        s2 = "Petaka";
                        break;
                    case 2:
                        s = getResources().getString(R.string.pakata);
                        s1 = getResources().getString(R.string.ExamplePakata);
                        s2 = "Pakata";
                        break;
                    case 3:
                        s = getResources().getString(R.string.pa);
                        s1 = getResources().getString(R.string.ExamplePa);
                        s2 = "Pa";
                        break;
                    case 4:
                        s = getResources().getString(R.string.ta);
                        s1 = getResources().getString(R.string.ExampleTa);
                        s2 = "Ta";
                        break;
                    case 5:
                        s = getResources().getString(R.string.ka);
                        s1 = getResources().getString(R.string.ExampleKa);
                        s2 = "Ka";
                        break;
                    case 6:
                        s = getResources().getString(R.string.sifaschu);
                        s1 = getResources().getString(R.string.ExampleSifaschu);
                        s2 = "Sifaschu";
                        break;
                    default:
                        s = "";
                        s1 = "";
                        s2 = "";
                        break;
                }
                intent.putExtra("word", s2);
                intent.putExtra("instruction", getResources().getString(R.string.ExplanationSyllableRepetition1) + s + getResources().getString(R.string.ExplanationSyllableRepetition2) + s1 + getResources().getString(R.string.ExplanationSyllableRepetition3));
                intent.putExtra("trainingset", false);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
