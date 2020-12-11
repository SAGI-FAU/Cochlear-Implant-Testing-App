/**
 * Created by Christoph Popp
 */

package com.example.cit_app.other_activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.cit_app.R;

import java.util.Objects;
import java.util.Random;


public class TrainingsetExerciseFinished extends AppCompatActivity {

    
    private Button done, again;
    private int exerciseCounter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainingset_exercise_finished);

        //initialize
        done = findViewById(R.id.done);
        again = findViewById(R.id.again);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if (getIntent().getExtras() != null)
                    exerciseCounter = getIntent().getExtras().getInt("exerciseCounter", 0) + 1;
                if(exerciseCounter >= Objects.requireNonNull(Objects.requireNonNull(getIntent().getExtras()).getStringArray("exerciseList")).length) {
                    intent = new Intent(v.getContext(), TrainingsetFinished.class);
                } else {
                    switch (Objects.requireNonNull(getIntent().getExtras().getStringArray("exerciseList"))[exerciseCounter]) {
                        case "MinimalPairs2":
                            intent = new Intent(v.getContext(), Instruction.class);
                            intent.putExtra("title", getResources().getString(R.string.MinimalPairs2));
                            intent.putExtra("description", getResources().getString(R.string.DescriptionMinPairs2));
                            intent.putExtra("instruction", getResources().getString(R.string.ExplanationMinPairs2));
                            intent.putExtra("exerciseList", getIntent().getExtras().getStringArray("exerciseList"));
                            intent.putExtra("exerciseCounter", exerciseCounter);
                            intent.putExtra("trainingset", true);
                            break;
                        case "Word_List":
                            intent = new Intent(v.getContext(), Instruction.class);
                            intent.putExtra("title", getResources().getString(R.string.WordList));
                            intent.putExtra("description", getResources().getString(R.string.DescriptionWordList));
                            intent.putExtra("instruction", getResources().getString(R.string.ExplanationWordList));
                            intent.putExtra("exerciseList", getIntent().getExtras().getStringArray("exerciseList"));
                            intent.putExtra("exerciseCounter", exerciseCounter);
                            intent.putExtra("trainingset", true);
                            break;
                        case "ReadingOfSentences":
                            intent = new Intent(v.getContext(), Instruction.class);
                            intent.putExtra("title", getResources().getString(R.string.SentenceReading));
                            intent.putExtra("description", getResources().getString(R.string.DescriptionSentenceReading));
                            intent.putExtra("instruction", getResources().getString(R.string.ExplanationSentenceReading));
                            intent.putExtra("exerciseList", getIntent().getExtras().getStringArray("exerciseList"));
                            intent.putExtra("exerciseCounter", exerciseCounter);
                            intent.putExtra("trainingset", true);
                            break;
                        case "SyllableRepetition":
                            intent = new Intent(v.getContext(), Instruction.class);
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
                            intent.putExtra("exerciseList", getIntent().getExtras().getStringArray("exerciseList"));
                            intent.putExtra("exerciseCounter", exerciseCounter);
                            intent.putExtra("trainingset", true);
                            break;
                        case "Picture_Description":
                            intent = new Intent(v.getContext(), Instruction.class);
                            intent.putExtra("title", getResources().getString(R.string.PictureDescription));
                            intent.putExtra("description", getResources().getString(R.string.DescriptionPictureDescription));
                            intent.putExtra("instruction", getResources().getString(R.string.ExplanationPictureDescription));
                            intent.putExtra("exerciseList", getIntent().getExtras().getStringArray("exerciseList"));
                            intent.putExtra("exerciseCounter", exerciseCounter);
                            intent.putExtra("trainingset", true);
                            break;
                        case "MinimalPairs":
                            intent = new Intent(v.getContext(), Instruction.class);
                            intent.putExtra("title", getResources().getString(R.string.MinimalPairs));
                            intent.putExtra("description", getResources().getString(R.string.DescriptionMinPairs));
                            intent.putExtra("instruction", getResources().getString(R.string.ExplanationMinPairs));
                            intent.putExtra("exerciseList", getIntent().getExtras().getStringArray("exerciseList"));
                            intent.putExtra("exerciseCounter", exerciseCounter);
                            intent.putExtra("trainingset", true);
                            break;
                        default:
                            intent = new Intent(v.getContext(), MainActivity.class);
                            break;
                    }
                }
                v.getContext().startActivity(intent);
            }
        });

        again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if (getIntent().getExtras() != null)
                    exerciseCounter = getIntent().getExtras().getInt("exerciseCounter", 0);
                if(exerciseCounter >= Objects.requireNonNull(Objects.requireNonNull(getIntent().getExtras()).getStringArray("exerciseList")).length) {
                    intent = new Intent(v.getContext(), TrainingsetFinished.class);
                } else {
                    SharedPreferences pref = getApplicationContext().getSharedPreferences("ExerciseFinished", 0);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putBoolean("MinimalPairs", true);
                    editor.apply();
                    switch (Objects.requireNonNull(getIntent().getExtras().getStringArray("exerciseList"))[exerciseCounter]) {
                        case "MinimalPairs2":
                            intent = new Intent(v.getContext(), Instruction.class);
                            intent.putExtra("title", getResources().getString(R.string.MinimalPairs2));
                            intent.putExtra("description", getResources().getString(R.string.DescriptionMinPairs2));
                            intent.putExtra("instruction", getResources().getString(R.string.ExplanationMinPairs2));
                            intent.putExtra("exerciseList", getIntent().getExtras().getStringArray("exerciseList"));
                            intent.putExtra("exerciseCounter", exerciseCounter);
                            intent.putExtra("trainingset", true);
                            break;
                        case "Word_List":
                            intent = new Intent(v.getContext(), Instruction.class);
                            intent.putExtra("title", getResources().getString(R.string.WordList));
                            intent.putExtra("description", getResources().getString(R.string.DescriptionWordList));
                            intent.putExtra("instruction", getResources().getString(R.string.ExplanationWordList));
                            intent.putExtra("exerciseList", getIntent().getExtras().getStringArray("exerciseList"));
                            intent.putExtra("exerciseCounter", exerciseCounter);
                            intent.putExtra("trainingset", true);
                            break;
                        case "ReadingOfSentences":
                            intent = new Intent(v.getContext(), Instruction.class);
                            intent.putExtra("title", getResources().getString(R.string.SentenceReading));
                            intent.putExtra("description", getResources().getString(R.string.DescriptionSentenceReading));
                            intent.putExtra("instruction", getResources().getString(R.string.ExplanationSentenceReading));
                            intent.putExtra("exerciseList", getIntent().getExtras().getStringArray("exerciseList"));
                            intent.putExtra("exerciseCounter", exerciseCounter);
                            intent.putExtra("trainingset", true);
                            break;
                        case "SyllableRepetition":
                            intent = new Intent(v.getContext(), Instruction.class);
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
                            intent.putExtra("exerciseList", getIntent().getExtras().getStringArray("exerciseList"));
                            intent.putExtra("exerciseCounter", exerciseCounter);
                            intent.putExtra("trainingset", true);
                            break;
                        case "Picture_Description":
                            intent = new Intent(v.getContext(), Instruction.class);
                            intent.putExtra("title", getResources().getString(R.string.PictureDescription));
                            intent.putExtra("description", getResources().getString(R.string.DescriptionPictureDescription));
                            intent.putExtra("instruction", getResources().getString(R.string.ExplanationPictureDescription));
                            intent.putExtra("exerciseList", getIntent().getExtras().getStringArray("exerciseList"));
                            intent.putExtra("exerciseCounter", exerciseCounter);
                            intent.putExtra("trainingset", true);
                            break;
                        case "MinimalPairs":
                            intent = new Intent(v.getContext(), Instruction.class);
                            intent.putExtra("title", getResources().getString(R.string.MinimalPairs));
                            intent.putExtra("description", getResources().getString(R.string.DescriptionMinPairs));
                            intent.putExtra("instruction", getResources().getString(R.string.ExplanationMinPairs));
                            intent.putExtra("exerciseList", getIntent().getExtras().getStringArray("exerciseList"));
                            intent.putExtra("exerciseCounter", exerciseCounter);
                            intent.putExtra("trainingset", true);
                            break;
                        default:
                            intent = new Intent(v.getContext(), MainActivity.class);
                            break;
                    }
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