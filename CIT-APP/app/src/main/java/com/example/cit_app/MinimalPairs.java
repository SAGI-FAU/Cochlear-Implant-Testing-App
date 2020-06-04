package com.example.cit_app;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class MinimalPairs extends AppCompatActivity {

    private int Record_Audio_permission_Code = 1;
    private int External_Storage_permission_Code = 2;

    Button listen, record;
    String path = "";
    int counter = 1;
    MediaRecorder recorder;
    MediaPlayer player;
    boolean isRecording;
    boolean isPlaying;
    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minimal_pairs);
        record = (Button) findViewById(R.id.record);
        listen = (Button) findViewById(R.id.listen);
        isRecording = false;
        isPlaying = false;
        text = (TextView) findViewById(R.id.wordNumber);
        text.setText(counter + " / 10");
        counter++;
        listen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isRecording) {

                } else {
                    if(player != null) {
                        player.seekTo(0);
                        player.start();
                    } else {
                        player = MediaPlayer.create(v.getContext(),  R.raw.test);
                        player.start();

                    }
                }
            }
        });
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
                            if(counter > 10) {
                                Intent intent = new Intent(v.getContext(), MinimalPairsExerciseFinished.class);
                                intent.putExtra("correct", 20);
                                v.getContext().startActivity(intent);
                            } else {
                                recorder.stop();
                                recorder.release();
                                isRecording = false;
                                record.setText("aufnehmen");
                                text.setText(counter + " / 10");
                                counter++;
                                if(player != null) {
                                    player.stop();
                                    player.release();
                                    player = null;
                                }
                            }
                        } else {
                            if(player != null)
                                player.stop();
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

    public void setupMediaRecorder() {
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), "CIT-APP");

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("App", "failed to create directory");
            }
        }
        path = mediaStorageDir.getAbsolutePath() + "/" + UUID.randomUUID().toString() + "MinimalPairs.wav";
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        recorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        recorder.setOutputFile(path);
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
}
