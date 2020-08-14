package com.example.cit_app.exercises;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cit_app.data_access.FeatureDataService;
import com.example.cit_app.other_activities.Instruction;
import com.example.cit_app.other_activities.MainActivity;
import com.example.cit_app.other_activities.MinimalPairsExerciseFinished;
import com.example.cit_app.R;
import com.example.cit_app.other_activities.TrainingsetFinished;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

public class MinimalPairs2 extends AppCompatActivity {

    private Random random = new Random();
    private TextView text;
    private String[] minimal_pairs = new String[80];
    int exerciseCounter;
    private int position = 0;
    private int counter = 0;
    private int oldPos = 0;
    private int choose = 0;
    private MediaPlayer player;
    private String[] minimal_pairs_fricatives, minimal_pairs_stops, minimal_pairs_general, minimal_pairs_result, minimal_pairs_correct;
    private Button start, topTop, botBot, topCenter, botCenter;
    FeatureDataService featureDataService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minimal_pairs2);
        getSupportActionBar().setTitle(getResources().getString(R.string.MinimalPairs2)); // for set actionbar title
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //initialize buttons and lists
        start = (Button) findViewById(R.id.startButton);
        topTop = (Button) findViewById(R.id.topTop);
        botBot = (Button) findViewById(R.id.topCenter);
        topCenter = (Button) findViewById(R.id.botCenter);
        featureDataService = new FeatureDataService(this);
        if(getIntent().getExtras() != null)
            exerciseCounter = getIntent().getExtras().getInt("exerciseCounter", 0) + 1;
        botCenter = (Button) findViewById(R.id.botBot);
        ArrayList<Integer> usedNumber = new ArrayList<>();
        text = (TextView) findViewById(R.id.wordNumber2);
        minimal_pairs_fricatives = getResources().getStringArray(R.array.Minimal_Pairs_Fricatives);
        minimal_pairs_stops = getResources().getStringArray(R.array.Minimal_Pairs_Stops);
        minimal_pairs_general = getResources().getStringArray(R.array.Minimal_Pairs);
        minimal_pairs_result = new String[20];
        minimal_pairs_correct = new String[20];
        text.setText((counter + 1) + " / 20");
        //fill minimal_pairs list with 20 pairs consisting of 10 random 5 fricative and 5 stop pairs
        for(int i = 0; i < 20; i++) {
            position = 4 * random.nextInt(50);
            if(i < 10) {
                while(usedNumber.contains(position)) {
                    position = 4 * random.nextInt(50);
                }
                int corr = random.nextInt(4);
                minimal_pairs_correct[i] = minimal_pairs_general[position + corr];
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
                    int corr = random.nextInt(4);
                    minimal_pairs_correct[i] = minimal_pairs_fricatives[position + corr];
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
                    int corr = random.nextInt(4);
                    minimal_pairs_correct[i] = minimal_pairs_stops[position + corr];
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
        topTop.setText(minimal_pairs[position % 4]);
        topCenter.setText(minimal_pairs[(position + 1) % 4]);
        botCenter.setText(minimal_pairs[(position + 2) % 4]);
        botBot.setText(minimal_pairs[(position + 3) % 4]);
        oldPos += 4;
        topTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(oldPos >= 80) {
                    minimal_pairs_result[counter] = topTop.getText().toString();
                    Intent intent = new Intent(MinimalPairs2.this, MinimalPairsExerciseFinished.class);
                    int correct = 0;
                    for(int i = 0; i < 20; i++) {
                        if(minimal_pairs_result[i].equals(minimal_pairs_correct[i])) {
                            correct++;
                        }
                    }
                    intent.putExtra("correct", correct);

                    if(getIntent().getBooleanExtra("trainingset", false)) {
                        SharedPreferences pref = getApplicationContext().getSharedPreferences("ExerciseFinished", 0);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putBoolean("MinimalPairs2", true);
                        editor.apply();
                        if(exerciseCounter >= getIntent().getExtras().getStringArray("exerciseList").length) {
                            intent = new Intent(v.getContext(), TrainingsetFinished.class);
                        } else {
                            switch (getIntent().getExtras().getStringArray("exerciseList")[exerciseCounter]) {
                                case "MinimalPairs":
                                    intent = new Intent(v.getContext(), Instruction.class);
                                    intent.putExtra("title", getResources().getString(R.string.MinimalPairs));
                                    //TODO Think of some useful descriptions and instructions
                                    intent.putExtra("description", getResources().getString(R.string.DescriptionMinPairs));
                                    intent.putExtra("instruction", getResources().getString(R.string.ExplanationMinPairs));
                                    intent.putExtra("exerciseList", getIntent().getExtras().getStringArray("exerciseList"));
                                    intent.putExtra("exerciseCounter", exerciseCounter);
                                    intent.putExtra("trainingset", true);
                                    break;
                                case "Word_List":
                                    intent = new Intent(v.getContext(), Instruction.class);
                                    intent.putExtra("title", getResources().getString(R.string.WordList));
                                    //TODO Think of some useful descriptions and instructions
                                    intent.putExtra("description", getResources().getString(R.string.DescriptionWordList));
                                    intent.putExtra("instruction", getResources().getString(R.string.ExplanationWordList));
                                    intent.putExtra("exerciseList", getIntent().getExtras().getStringArray("exerciseList"));
                                    intent.putExtra("exerciseCounter", exerciseCounter);
                                    intent.putExtra("trainingset", true);
                                    break;
                                case "ReadingOfSentences":
                                    intent = new Intent(v.getContext(), Instruction.class);
                                    intent.putExtra("title", getResources().getString(R.string.SentenceReading));
                                    //TODO Think of some useful descriptions and instructions
                                    intent.putExtra("description", getResources().getString(R.string.DescriptionSentenceReading));
                                    intent.putExtra("instruction", getResources().getString(R.string.ExplanationSentenceReading));
                                    intent.putExtra("exerciseList", getIntent().getExtras().getStringArray("exerciseList"));
                                    intent.putExtra("exerciseCounter", exerciseCounter);
                                    intent.putExtra("trainingset", true);
                                    break;
                                case "SyllableRepetition":
                                    intent = new Intent(v.getContext(), Instruction.class);
                                    intent.putExtra("title", getResources().getString(R.string.SyllableRepetition));
                                    //TODO Think of some useful descriptions and instructions
                                    intent.putExtra("description", getResources().getString(R.string.DescriptionSyllableRepetition));
                                    intent.putExtra("instruction", getResources().getString(R.string.ExplanationSyllableRepetition));
                                    intent.putExtra("exerciseList", getIntent().getExtras().getStringArray("exerciseList"));
                                    intent.putExtra("exerciseCounter", exerciseCounter);
                                    intent.putExtra("trainingset", true);
                                    break;
                                case "Picture_Description":
                                    intent = new Intent(v.getContext(), Instruction.class);
                                    intent.putExtra("title", getResources().getString(R.string.PictureDescription));
                                    //TODO Think of some useful descriptions and instructions
                                    intent.putExtra("description", getResources().getString(R.string.DescriptionPictureDescription));
                                    intent.putExtra("instruction", getResources().getString(R.string.ExplanationPictureDescription));
                                    intent.putExtra("exerciseList", getIntent().getExtras().getStringArray("exerciseList"));
                                    intent.putExtra("exerciseCounter", exerciseCounter);
                                    intent.putExtra("trainingset", true);
                                    break;
                                default:
                                    intent = new Intent(v.getContext(), MainActivity.class);
                                    break;
                            }
                        }
                    }
                    float int_f0 = (float)((double) correct / (double) 20);
                    Toast.makeText(MinimalPairs2.this, "" + int_f0, Toast.LENGTH_SHORT).show();
                    featureDataService.save_feature(featureDataService.hearing_name, Calendar.getInstance().getTime(), int_f0);
                    v.getContext().startActivity(intent);
                } else {
                    position += random.nextInt(3);
                    minimal_pairs_result[counter] = topTop.getText().toString();
                    counter++;
                    topTop.setText(minimal_pairs[oldPos + (position % 4)]);
                    topCenter.setText(minimal_pairs[oldPos + ((position + 1) % 4)]);
                    botCenter.setText(minimal_pairs[oldPos + ((position + 2) % 4)]);
                    botBot.setText(minimal_pairs[oldPos + ((position + 3) % 4)]);
                    text.setText((counter + 1) + " / 20");
                    oldPos += 4;
                    choose += 1;
                    if(player != null) {
                        player.stop();
                        player.release();
                        player = null;
                    }
                }
            }
        });
        topCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(oldPos >= 80) {
                    minimal_pairs_result[counter] = topCenter.getText().toString();
                    Intent intent = new Intent(MinimalPairs2.this, MinimalPairsExerciseFinished.class);
                    int correct = 0;
                    for(int i = 0; i < 20; i++) {
                        if(minimal_pairs_result[i].equals(minimal_pairs_correct[i])) {
                            correct++;
                        }
                    }
                    intent.putExtra("correct", correct);
                    if(getIntent().getBooleanExtra("trainingset", false)) {
                        SharedPreferences pref = getApplicationContext().getSharedPreferences("ExerciseFinished", 0);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putBoolean("MinimalPairs2", true);
                        editor.apply();
                        if(exerciseCounter >= getIntent().getExtras().getStringArray("exerciseList").length) {
                            intent = new Intent(v.getContext(), TrainingsetFinished.class);
                        } else {
                            switch (getIntent().getExtras().getStringArray("exerciseList")[exerciseCounter]) {
                                case "MinimalPairs":
                                    intent = new Intent(v.getContext(), Instruction.class);
                                    intent.putExtra("title", getResources().getString(R.string.MinimalPairs));
                                    //TODO Think of some useful descriptions and instructions
                                    intent.putExtra("description", getResources().getString(R.string.DescriptionMinPairs));
                                    intent.putExtra("instruction", getResources().getString(R.string.ExplanationMinPairs));
                                    intent.putExtra("exerciseList", getIntent().getExtras().getStringArray("exerciseList"));
                                    intent.putExtra("exerciseCounter", exerciseCounter);
                                    intent.putExtra("trainingset", true);
                                    break;
                                case "Word_List":
                                    intent = new Intent(v.getContext(), Instruction.class);
                                    intent.putExtra("title", getResources().getString(R.string.WordList));
                                    //TODO Think of some useful descriptions and instructions
                                    intent.putExtra("description", getResources().getString(R.string.DescriptionWordList));
                                    intent.putExtra("instruction", getResources().getString(R.string.ExplanationWordList));
                                    intent.putExtra("exerciseList", getIntent().getExtras().getStringArray("exerciseList"));
                                    intent.putExtra("exerciseCounter", exerciseCounter);
                                    intent.putExtra("trainingset", true);
                                    break;
                                case "ReadingOfSentences":
                                    intent = new Intent(v.getContext(), Instruction.class);
                                    intent.putExtra("title", getResources().getString(R.string.SentenceReading));
                                    //TODO Think of some useful descriptions and instructions
                                    intent.putExtra("description", getResources().getString(R.string.DescriptionSentenceReading));
                                    intent.putExtra("instruction", getResources().getString(R.string.ExplanationSentenceReading));
                                    intent.putExtra("exerciseList", getIntent().getExtras().getStringArray("exerciseList"));
                                    intent.putExtra("exerciseCounter", exerciseCounter);
                                    intent.putExtra("trainingset", true);
                                    break;
                                case "SyllableRepetition":
                                    intent = new Intent(v.getContext(), Instruction.class);
                                    intent.putExtra("title", getResources().getString(R.string.SyllableRepetition));
                                    //TODO Think of some useful descriptions and instructions
                                    intent.putExtra("description", getResources().getString(R.string.DescriptionSyllableRepetition));
                                    intent.putExtra("instruction", getResources().getString(R.string.ExplanationSyllableRepetition));
                                    intent.putExtra("exerciseList", getIntent().getExtras().getStringArray("exerciseList"));
                                    intent.putExtra("exerciseCounter", exerciseCounter);
                                    intent.putExtra("trainingset", true);
                                    break;
                                case "Picture_Description":
                                    intent = new Intent(v.getContext(), Instruction.class);
                                    intent.putExtra("title", getResources().getString(R.string.PictureDescription));
                                    //TODO Think of some useful descriptions and instructions
                                    intent.putExtra("description", getResources().getString(R.string.DescriptionPictureDescription));
                                    intent.putExtra("instruction", getResources().getString(R.string.ExplanationPictureDescription));
                                    intent.putExtra("exerciseList", getIntent().getExtras().getStringArray("exerciseList"));
                                    intent.putExtra("exerciseCounter", exerciseCounter);
                                    intent.putExtra("trainingset", true);
                                    break;
                                default:
                                    intent = new Intent(v.getContext(), MainActivity.class);
                                    break;
                            }
                        }
                    }
                    float int_f0 = (float)((double) correct / (double) 20);
                    Toast.makeText(MinimalPairs2.this, "" + int_f0, Toast.LENGTH_SHORT).show();
                    featureDataService.save_feature(featureDataService.hearing_name, Calendar.getInstance().getTime(), int_f0);
                    v.getContext().startActivity(intent);
                } else {
                    position = random.nextInt(3);
                    minimal_pairs_result[counter] = topCenter.getText().toString();
                    counter++;
                    topTop.setText(minimal_pairs[oldPos + (position % 4)]);
                    topCenter.setText(minimal_pairs[oldPos + ((position + 1) % 4)]);
                    botCenter.setText(minimal_pairs[oldPos + ((position + 2) % 4)]);
                    botBot.setText(minimal_pairs[oldPos + ((position + 3) % 4)]);
                    text.setText((counter + 1) + " / 20");
                    oldPos += 4;
                    choose += 1;
                    if(player != null) {
                        player.stop();
                        player.release();
                        player = null;
                    }
                }
            }
        });
        botCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(oldPos >= 80) {
                    minimal_pairs_result[counter] = botCenter.getText().toString();
                    Intent intent = new Intent(MinimalPairs2.this, MinimalPairsExerciseFinished.class);
                    int correct = 0;
                    for(int i = 0; i < 20; i++) {
                        if(minimal_pairs_result[i].equals(minimal_pairs_correct[i])) {
                            correct++;
                        }
                    }
                    intent.putExtra("correct", correct);
                    if(getIntent().getBooleanExtra("trainingset", false)) {
                        SharedPreferences pref = getApplicationContext().getSharedPreferences("ExerciseFinished", 0);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putBoolean("MinimalPairs2", true);
                        editor.apply();
                        if(exerciseCounter >= getIntent().getExtras().getStringArray("exerciseList").length) {
                            intent = new Intent(v.getContext(), TrainingsetFinished.class);
                        } else {
                            switch (getIntent().getExtras().getStringArray("exerciseList")[exerciseCounter]) {
                                case "MinimalPairs":
                                    intent = new Intent(v.getContext(), Instruction.class);
                                    intent.putExtra("title", getResources().getString(R.string.MinimalPairs));
                                    //TODO Think of some useful descriptions and instructions
                                    intent.putExtra("description", getResources().getString(R.string.DescriptionMinPairs));
                                    intent.putExtra("instruction", getResources().getString(R.string.ExplanationMinPairs));
                                    intent.putExtra("exerciseList", getIntent().getExtras().getStringArray("exerciseList"));
                                    intent.putExtra("exerciseCounter", exerciseCounter);
                                    intent.putExtra("trainingset", true);
                                    break;
                                case "Word_List":
                                    intent = new Intent(v.getContext(), Instruction.class);
                                    intent.putExtra("title", getResources().getString(R.string.WordList));
                                    //TODO Think of some useful descriptions and instructions
                                    intent.putExtra("description", getResources().getString(R.string.DescriptionWordList));
                                    intent.putExtra("instruction", getResources().getString(R.string.ExplanationWordList));
                                    intent.putExtra("exerciseList", getIntent().getExtras().getStringArray("exerciseList"));
                                    intent.putExtra("exerciseCounter", exerciseCounter);
                                    intent.putExtra("trainingset", true);
                                    break;
                                case "ReadingOfSentences":
                                    intent = new Intent(v.getContext(), Instruction.class);
                                    intent.putExtra("title", getResources().getString(R.string.SentenceReading));
                                    //TODO Think of some useful descriptions and instructions
                                    intent.putExtra("description", getResources().getString(R.string.DescriptionSentenceReading));
                                    intent.putExtra("instruction", getResources().getString(R.string.ExplanationSentenceReading));
                                    intent.putExtra("exerciseList", getIntent().getExtras().getStringArray("exerciseList"));
                                    intent.putExtra("exerciseCounter", exerciseCounter);
                                    intent.putExtra("trainingset", true);
                                    break;
                                case "SyllableRepetition":
                                    intent = new Intent(v.getContext(), Instruction.class);
                                    intent.putExtra("title", getResources().getString(R.string.SyllableRepetition));
                                    //TODO Think of some useful descriptions and instructions
                                    intent.putExtra("description", getResources().getString(R.string.DescriptionSyllableRepetition));
                                    intent.putExtra("instruction", getResources().getString(R.string.ExplanationSyllableRepetition));
                                    intent.putExtra("exerciseList", getIntent().getExtras().getStringArray("exerciseList"));
                                    intent.putExtra("exerciseCounter", exerciseCounter);
                                    intent.putExtra("trainingset", true);
                                    break;
                                case "Picture_Description":
                                    intent = new Intent(v.getContext(), Instruction.class);
                                    intent.putExtra("title", getResources().getString(R.string.PictureDescription));
                                    //TODO Think of some useful descriptions and instructions
                                    intent.putExtra("description", getResources().getString(R.string.DescriptionPictureDescription));
                                    intent.putExtra("instruction", getResources().getString(R.string.ExplanationPictureDescription));
                                    intent.putExtra("exerciseList", getIntent().getExtras().getStringArray("exerciseList"));
                                    intent.putExtra("exerciseCounter", exerciseCounter);
                                    intent.putExtra("trainingset", true);
                                    break;
                                default:
                                    intent = new Intent(v.getContext(), MainActivity.class);
                                    break;
                            }
                        }
                    }
                    float int_f0 = (float)((double) correct / (double) 20);
                    Toast.makeText(MinimalPairs2.this, "" + int_f0, Toast.LENGTH_SHORT).show();
                    featureDataService.save_feature(featureDataService.hearing_name, Calendar.getInstance().getTime(), int_f0);
                    v.getContext().startActivity(intent);
                } else {
                    position = random.nextInt(3);
                    minimal_pairs_result[counter] = botCenter.getText().toString();
                    counter++;
                    topTop.setText(minimal_pairs[oldPos + (position % 4)]);
                    topCenter.setText(minimal_pairs[oldPos + ((position + 1) % 4)]);
                    botCenter.setText(minimal_pairs[oldPos + ((position + 2) % 4)]);
                    botBot.setText(minimal_pairs[oldPos + ((position + 3) % 4)]);
                    text.setText((counter + 1) + " / 20");
                    oldPos += 4;
                    choose += 1;
                    if(player != null) {
                        player.stop();
                        player.release();
                        player = null;
                    }
                }
            }
        });
        botBot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(oldPos >= 80) {
                    minimal_pairs_result[counter] = botBot.getText().toString();
                    Intent intent = new Intent(MinimalPairs2.this, MinimalPairsExerciseFinished.class);
                    int correct = 0;
                    for(int i = 0; i < 20; i++) {
                        if(minimal_pairs_result[i].equals(minimal_pairs_correct[i])) {
                            correct++;
                        }
                    }
                    intent.putExtra("correct", correct);
                    if(getIntent().getBooleanExtra("trainingset", false)) {
                        SharedPreferences pref = getApplicationContext().getSharedPreferences("ExerciseFinished", 0);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putBoolean("MinimalPairs2", true);
                        editor.apply();
                        if(exerciseCounter >= getIntent().getExtras().getStringArray("exerciseList").length) {
                            intent = new Intent(v.getContext(), TrainingsetFinished.class);
                        } else {
                            switch (getIntent().getExtras().getStringArray("exerciseList")[exerciseCounter]) {
                                case "MinimalPairs":
                                    intent = new Intent(v.getContext(), Instruction.class);
                                    intent.putExtra("title", getResources().getString(R.string.MinimalPairs));
                                    //TODO Think of some useful descriptions and instructions
                                    intent.putExtra("description", getResources().getString(R.string.DescriptionMinPairs));
                                    intent.putExtra("instruction", getResources().getString(R.string.ExplanationMinPairs));
                                    intent.putExtra("exerciseList", getIntent().getExtras().getStringArray("exerciseList"));
                                    intent.putExtra("exerciseCounter", exerciseCounter);
                                    intent.putExtra("trainingset", true);
                                    break;
                                case "Word_List":
                                    intent = new Intent(v.getContext(), Instruction.class);
                                    intent.putExtra("title", getResources().getString(R.string.WordList));
                                    //TODO Think of some useful descriptions and instructions
                                    intent.putExtra("description", getResources().getString(R.string.DescriptionWordList));
                                    intent.putExtra("instruction", getResources().getString(R.string.ExplanationWordList));
                                    intent.putExtra("exerciseList", getIntent().getExtras().getStringArray("exerciseList"));
                                    intent.putExtra("exerciseCounter", exerciseCounter);
                                    intent.putExtra("trainingset", true);
                                    break;
                                case "ReadingOfSentences":
                                    intent = new Intent(v.getContext(), Instruction.class);
                                    intent.putExtra("title", getResources().getString(R.string.SentenceReading));
                                    //TODO Think of some useful descriptions and instructions
                                    intent.putExtra("description", getResources().getString(R.string.DescriptionSentenceReading));
                                    intent.putExtra("instruction", getResources().getString(R.string.ExplanationSentenceReading));
                                    intent.putExtra("exerciseList", getIntent().getExtras().getStringArray("exerciseList"));
                                    intent.putExtra("exerciseCounter", exerciseCounter);
                                    intent.putExtra("trainingset", true);
                                    break;
                                case "SyllableRepetition":
                                    intent = new Intent(v.getContext(), Instruction.class);
                                    intent.putExtra("title", getResources().getString(R.string.SyllableRepetition));
                                    //TODO Think of some useful descriptions and instructions
                                    intent.putExtra("description", getResources().getString(R.string.DescriptionSyllableRepetition));
                                    intent.putExtra("instruction", getResources().getString(R.string.ExplanationSyllableRepetition));
                                    intent.putExtra("exerciseList", getIntent().getExtras().getStringArray("exerciseList"));
                                    intent.putExtra("exerciseCounter", exerciseCounter);
                                    intent.putExtra("trainingset", true);
                                    break;
                                case "Picture_Description":
                                    intent = new Intent(v.getContext(), Instruction.class);
                                    intent.putExtra("title", getResources().getString(R.string.PictureDescription));
                                    //TODO Think of some useful descriptions and instructions
                                    intent.putExtra("description", getResources().getString(R.string.DescriptionPictureDescription));
                                    intent.putExtra("instruction", getResources().getString(R.string.ExplanationPictureDescription));
                                    intent.putExtra("exerciseList", getIntent().getExtras().getStringArray("exerciseList"));
                                    intent.putExtra("exerciseCounter", exerciseCounter);
                                    intent.putExtra("trainingset", true);
                                    break;
                                default:
                                    intent = new Intent(v.getContext(), MainActivity.class);
                                    break;
                            }
                        }
                    }
                    float int_f0 = (float)((double) correct / (double) 20);
                    Toast.makeText(MinimalPairs2.this, "" + int_f0, Toast.LENGTH_SHORT).show();
                    featureDataService.save_feature(featureDataService.hearing_name, Calendar.getInstance().getTime(), int_f0);
                    v.getContext().startActivity(intent);
                } else {
                    position = random.nextInt(3);
                    minimal_pairs_result[counter] = botBot.getText().toString();
                    counter++;
                    topTop.setText(minimal_pairs[oldPos + (position % 4)]);
                    topCenter.setText(minimal_pairs[oldPos + ((position + 1) % 4)]);
                    botCenter.setText(minimal_pairs[oldPos + ((position + 2) % 4)]);
                    botBot.setText(minimal_pairs[oldPos + ((position + 3) % 4)]);
                    text.setText((counter + 1) + " / 20");
                    oldPos += 4;
                    choose += 1;
                    if(player != null) {
                        player.stop();
                        player.release();
                        player = null;
                    }
                }
            }
        });
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(player != null) {
                    player.seekTo(0);
                    player.start();
                } else {
                    minimal_pairs_correct[choose] = minimal_pairs_correct[choose].replace("ä", "ae");
                    minimal_pairs_correct[choose] = minimal_pairs_correct[choose].replace("ö", "oe");
                    minimal_pairs_correct[choose] = minimal_pairs_correct[choose].replace("ü", "ue");
                    minimal_pairs_correct[choose] = minimal_pairs_correct[choose].replace("ß", "ss");
                    String file = minimal_pairs_correct[choose].toLowerCase();
                    int resId = getResources().getIdentifier(file, "raw", getPackageName());
                    String path = "a" + resId;
                    String path2 = path.substring(1);
                    player = MediaPlayer.create(v.getContext(), Integer.parseInt(path2));
                    player.start();
                }
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        if(!getIntent().getExtras().getBoolean("trainingset"))
            finish();
        return super.onOptionsItemSelected(item);
    }
}
