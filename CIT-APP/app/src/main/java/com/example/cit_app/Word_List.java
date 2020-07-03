package com.example.cit_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

public class Word_List extends AppCompatActivity {

    public static boolean exerciseFinished = false;

    private int Record_Audio_permission_Code = 1;
    private int External_Storage_permission_Code = 2;
    private boolean isRecording = false;
    private MediaRecorder recorder;
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
                if(ContextCompat.checkSelfPermission(v.getContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                    requestRecordAudioPermission();
                } else {
                    if(ContextCompat.checkSelfPermission(v.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        requestExternalStoragePermission();
                    } else {
                        if(isRecording) {
                            recorder.stop();
                            recorder.release();
                            record.setText("aufnehmen");
                            if(counter < 10) {
                                word.setText(wordList[counter]);
                                counter++;
                                text.setText(counter + " / 10");
                            } else {
                                Intent intent = new Intent(v.getContext(), GeneralRepetitionFinished.class);
                                intent.putExtra("exercise", "Word_List");
                                if(getIntent().getBooleanExtra("trainingset", false)) {
                                    //exerciseFinished = true;
                                }
                                v.getContext().startActivity(intent);
                            }
                            isRecording = false;
                        } else {
                            setupMediaRecorder();
                            try {
                                record.setText("recording...");
                                recorder.prepare();
                                recorder.start();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            isRecording = true;
                        }
                    }
                }
            }
        });

    }

    public void requestRecordAudioPermission() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO) || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            new AlertDialog.Builder(this).setTitle("Permission needed").setMessage("For this exercise permission to record your voice and write to your storage is needed").setPositiveButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO}, Record_Audio_permission_Code);
                }
            }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).create().show();
        } else {
            requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO}, Record_Audio_permission_Code);
        }
    }

    public void requestExternalStoragePermission() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO) || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            new AlertDialog.Builder(this).setTitle("Permission needed").setMessage("For this exercise permission to record your voice and write to your storage is needed").setPositiveButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, External_Storage_permission_Code);
                }
            }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).create().show();
        } else {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, External_Storage_permission_Code);
        }
    }

    public void setupMediaRecorder() {
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), "CIT-APP");

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("App", "failed to create directory");
            }
        }
        path = mediaStorageDir.getAbsolutePath() + "/" + UUID.randomUUID().toString() + "WordList.wav";
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        recorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        recorder.setOutputFile(path);
    }
}
