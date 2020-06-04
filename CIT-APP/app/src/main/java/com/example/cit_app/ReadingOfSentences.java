package com.example.cit_app;

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

import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.UUID;

public class ReadingOfSentences extends AppCompatActivity {

    private int Record_Audio_permission_Code = 1;
    private int External_Storage_permission_Code = 2;
    private String path;
    private boolean isRecording;
    private MediaRecorder recorder;
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
                if(ContextCompat.checkSelfPermission(v.getContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                    requestRecordAudioPermission();
                } else {
                    if (ContextCompat.checkSelfPermission(v.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        requestExternalStoragePermission();
                    } else {
                        if(isRecording) {
                            recorder.stop();
                            recorder.release();
                            isRecording = false;
                            record.setText("aufnehmen");
                            Intent intent = new Intent(v.getContext(), GeneralRepetitionFinished.class);
                            intent.putExtra("exercise", "ReadingOfSentences");
                            v.getContext().startActivity(intent);
                        } else {
                            setupMediaRecorder();
                            try {
                                isRecording = true;
                                recorder.prepare();
                                recorder.start();
                                record.setText("recording...");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
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
        path = mediaStorageDir.getAbsolutePath() + "/" + UUID.randomUUID().toString() + "SentenceReading.wav";
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        recorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        recorder.setOutputFile(path);
    }
}
