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
import android.media.MediaPlayer;
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

public class MinimalPairs extends AppCompatActivity {

    Button listen, record;
    String path = "";
    int counter = 1;
    private SpeechRecorder recorder;
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
        recorder = SpeechRecorder.getInstance(this, new MinimalPairs.VolumeHandler());
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
                if(isRecording) {
                    if(counter > 19) {
                        isRecording = false;
                        record.setText("aufnehmen");
                        Intent intent = new Intent(v.getContext(), GeneralRepetitionFinished.class);
                        intent.putExtra("exercise", "MinimalPairs");
                        if(getIntent().getBooleanExtra("trainingset", false)) {
                            SharedPreferences pref = getApplicationContext().getSharedPreferences("ExerciseFinished", 0);
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putBoolean("MinimalPairs", true);
                            editor.apply();
                        }
                        recorder.stopRecording();
                        recorder.release();
                        v.getContext().startActivity(intent);
                    } else {
                        recorder.stopRecording();
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
                    path = recorder.prepare("Minimal_Pairs");
                    recorder.record();
                    isRecording = true;
                    record.setText(R.string.recording);
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
