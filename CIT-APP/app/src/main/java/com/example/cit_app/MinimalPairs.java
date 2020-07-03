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
import android.net.Uri;
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
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

public class MinimalPairs extends AppCompatActivity {

    private int Record_Audio_permission_Code = 1;
    private int External_Storage_permission_Code = 2;

    public static boolean exerciseFinished = false;

    Button listen, record;
    String path = "";
    int counter = 1;
    MediaRecorder recorder;
    private int position = 0;
    private Random random = new Random();
    MediaPlayer player;
    private ArrayList<Integer> usedNumber = new ArrayList<>();
    boolean isRecording;
    boolean isPlaying;
    private String[] minimal_pairs = new String[20];
    private String[] minimal_pairs_fricatives, minimal_pairs_stops, minimal_pairs_general;
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
        text.setText(counter + " / 20");
        minimal_pairs_fricatives = getResources().getStringArray(R.array.Minimal_Pairs_Fricatives);
        minimal_pairs_stops = getResources().getStringArray(R.array.Minimal_Pairs_Stops);
        minimal_pairs_general = getResources().getStringArray(R.array.Minimal_Pairs);
        //fill minimal_pairs list with 20 pairs consisting of 10 random 5 fricative and 5 stop pairs
        for(int i = 0; i < 20; i++) {
            position = 4 * random.nextInt(50);
            if(i < 10) {
                while(usedNumber.contains(position)) {
                    position = 4 * random.nextInt(50);
                }
                minimal_pairs[i] = minimal_pairs_general[position];
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
                    minimal_pairs[i] = minimal_pairs_fricatives[position];
                    usedNumber.add(position);
                } else {
                    if(i == 15) {
                        usedNumber.clear();
                    }
                    position = 4 * random.nextInt(7);
                    while(usedNumber.contains(position)) {
                        position = 4 * random.nextInt(7);
                    }
                    minimal_pairs[i] = minimal_pairs_stops[position];
                    usedNumber.add(position);
                }
            }
        }
        listen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isRecording) {

                } else {
                    if(player != null) {
                        player.seekTo(0);
                        player.start();
                    } else {
                        if(counter > 21){

                        } else {
                            minimal_pairs[counter - 1] = minimal_pairs[counter - 1].replace("ä", "ae");
                            minimal_pairs[counter - 1] = minimal_pairs[counter - 1].replace("ö", "oe");
                            minimal_pairs[counter - 1] = minimal_pairs[counter - 1].replace("ü", "ue");
                            minimal_pairs[counter - 1] = minimal_pairs[counter - 1].replace("ß", "ss");
                            String file = minimal_pairs[counter - 1].toLowerCase();
                            int resId = getResources().getIdentifier(file, "raw", getPackageName());
                            String path = "a" + resId;
                            String path2 = path.substring(1);
                            player = MediaPlayer.create(v.getContext(), Integer.parseInt(path2));
                            player.start();
                        }
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
                            if(counter > 19) {
                                recorder.stop();
                                recorder.release();
                                isRecording = false;
                                record.setText("aufnehmen");
                                Intent intent = new Intent(v.getContext(), GeneralRepetitionFinished.class);
                                intent.putExtra("exercise", "MinimalPairs");
                                if(getIntent().getBooleanExtra("trainingset", false)) {
                                    exerciseFinished = true;
                                }
                                v.getContext().startActivity(intent);
                            } else {
                                recorder.stop();
                                recorder.release();
                                isRecording = false;
                                record.setText("aufnehmen");
                                counter++;
                                text.setText(counter + " / 20");
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
