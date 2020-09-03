package com.example.cit_app.exercises;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cit_app.other_activities.GeneralRepetitionFinished;
import com.example.cit_app.R;
import com.example.cit_app.data_access.SpeechRecorder;
import com.example.cit_app.other_activities.Instruction;
import com.example.cit_app.other_activities.MainActivity;
import com.example.cit_app.other_activities.TrainingsetFinished;

import java.util.ArrayList;
import java.util.Random;

public class Word_List extends AppCompatActivity {
    private boolean isRecording = false;
    private SpeechRecorder recorder;
    private String path;
    TextView word, text, recordText;
    ImageView imageView;
    CardView record;
    Random rand = new Random();
    int exerciseCounter;
    int counter = 1;
    ArrayList<Integer> usedInts = new ArrayList<>();
    String[] wordListPlosive, wordListFricative, wordListNasal, wordListSibilant, wordList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word__list);
        getSupportActionBar().setTitle(getResources().getString(R.string.WordList)); // for set actionbar title
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        wordList = new String[10];
        if(getIntent().getExtras() != null)
            exerciseCounter = getIntent().getExtras().getInt("exerciseCounter", 0) + 1;
        wordListFricative = getResources().getStringArray(R.array.Word_List_Fricatives);
        wordListPlosive = getResources().getStringArray(R.array.Word_List_Plosives);
        wordListNasal = getResources().getStringArray(R.array.Word_List_Nasal);
        wordListSibilant = getResources().getStringArray(R.array.Word_List_Sibilant);
        word = (TextView) findViewById(R.id.word);
        text = (TextView) findViewById(R.id.wordNumber3);
        record = (CardView) findViewById(R.id.recordWordList);
        recordText = (TextView) findViewById(R.id.recordWordListText);
        imageView = (ImageView) findViewById(R.id.recordWordListImage);
        recorder = SpeechRecorder.getInstance(this, new Word_List.VolumeHandler(), "Word_List");
        text.setText(counter + " / 10");
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
                if(isRecording) {
                    recorder.stopRecording();
                    recordText.setText("aufnehmen");
                    imageView.setImageResource(R.drawable.play);
                    if(counter < 10) {
                        word.setText(wordList[counter]);
                        counter++;
                        text.setText(counter + " / 10");
                    } else {
                        Intent intent = new Intent(v.getContext(), GeneralRepetitionFinished.class);
                        intent.putExtra("exercise", "Word_List");
                        if(getIntent().getBooleanExtra("trainingset", false)) {
                            SharedPreferences pref = getApplicationContext().getSharedPreferences("ExerciseFinished", 0);
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putBoolean("Word_List", true);
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
                                    case "MinimalPairs2":
                                        intent = new Intent(v.getContext(), Instruction.class);
                                        intent.putExtra("title", getResources().getString(R.string.MinimalPairs2));
                                        //TODO Think of some useful descriptions and instructions
                                        intent.putExtra("description", getResources().getString(R.string.DescriptionMinPairs2));
                                        intent.putExtra("instruction", getResources().getString(R.string.ExplanationMinPairs2));
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
                        recorder.release();
                        v.getContext().startActivity(intent);
                    }
                    isRecording = false;
                } else {
                    path = recorder.prepare("Word_List");
                    recorder.record();
                    recordText.setText(R.string.recording);
                    imageView.setImageResource(R.drawable.pause);
                    isRecording = true;
                }
            }
        });

    }

    private class VolumeHandler extends Handler {

        public VolumeHandler() {
        }
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            final int currentVolume = (int) bundle.getDouble("Volume");

            final String state = bundle.getString("State", "Empty");
            if (state.equals("Finished")){

            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        if(!getIntent().getExtras().getBoolean("trainingset"))
            finish();
        return super.onOptionsItemSelected(item);
    }
}
