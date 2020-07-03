package com.example.cit_app;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class SyllableRepetition extends AppCompatActivity{

    private int Record_Audio_permission_Code = 1;
    private int External_Storage_permission_Code = 2;

    public static boolean exerciseFinished = false;

    //TODO Add recording of voice
    private TextView timer;
    private ProgressBar progress;
    private Button start;
    private String path;
    private boolean isRecording, started;
    private MediaRecorder recorder;
    private CountDownTimer cdt;
    private Context c = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_syllable_repetition);
        started = false;
        timer = (TextView) findViewById(R.id.timer);
        progress = (ProgressBar) findViewById(R.id.timerProgress);
        start = (Button) findViewById(R.id.startTimer);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(v.getContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                    requestRecordAudioPermission();
                } else {
                    if (ContextCompat.checkSelfPermission(v.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        requestExternalStoragePermission();
                    } else {
                        if(!started) {
                            started = true;
                            startTimer();
                            if (isRecording) {
                            } else {
                                setupMediaRecorder();
                                try {
                                    isRecording = true;
                                    recorder.prepare();
                                    recorder.start();
                                    start.setText("recording...");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
            }
        });
    }

    public void startTimer(){
        cdt = new CountDownTimer(11900, 1000) {
            int count = 10;
            @Override
            public void onTick(long millisUntilFinished) {
                timer.setText("" + count);
                progress.setProgress(count * 10);
                count --;
            }

            @Override
            public void onFinish() {
                recorder.stop();
                recorder.release();
                isRecording = false;
                started = false;
                Intent intent = new Intent(c, GeneralRepetitionFinished.class);
                intent.putExtra("exercise", "SyllableRepetition");
                Toast.makeText(c, "" + getIntent().getBooleanExtra("trainingset", false), Toast.LENGTH_SHORT).show();
                if(getIntent().getBooleanExtra("trainingset", false)) {
                    //exerciseFinished = true;
                }
                c.startActivity(intent);
            }
        }.start();
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
        path = mediaStorageDir.getAbsolutePath() + "/" + UUID.randomUUID().toString() + "SyllableRepetition.wav";
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        recorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        recorder.setOutputFile(path);
    }
}
