package com.example.cit_app.exercises;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.cit_app.other_activities.GeneralRepetitionFinished;
import com.example.cit_app.R;
import com.example.cit_app.data_access.SpeechRecorder;

import java.util.ArrayList;
import java.util.Random;

public class Word_List extends AppCompatActivity {
    private boolean isRecording = false;
    private SpeechRecorder recorder;
    private String path;
    TextView word, text;
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
        text = (TextView) findViewById(R.id.wordNumber3);
        record = (Button) findViewById(R.id.recordButtonWordList);
        recorder = SpeechRecorder.getInstance(this, new Word_List.VolumeHandler());
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
                    record.setText("aufnehmen");
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
                        }
                        recorder.release();
                        v.getContext().startActivity(intent);
                    }
                    isRecording = false;
                } else {
                    path = recorder.prepare("Word_List");
                    recorder.record();
                    record.setText(R.string.recording);
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


               /* try {
                    if (mExercise.getId()==7){ // Compute intonation from longest sentence.
                        float int_f0 = RadarFeatures.intonation(filePath);
                        File file = new File(filePath);
                        Date lastModDate = new Date(file.lastModified());
                        FeatureDataService.save_feature(FeatureDataService.intonation_name, lastModDate, int_f0);

                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putBoolean("New Area Speech", true);
                        editor.apply();

                    }
                }
                catch (Exception e){
                    Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.failed),Toast.LENGTH_SHORT).show();
                }*/

            }
        }
    }
}
