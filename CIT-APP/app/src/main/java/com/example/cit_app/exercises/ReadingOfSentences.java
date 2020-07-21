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
import android.widget.Toast;

import com.example.cit_app.RadarFeatures;
import com.example.cit_app.data_access.FeatureDataService;
import com.example.cit_app.other_activities.GeneralRepetitionFinished;
import com.example.cit_app.R;
import com.example.cit_app.data_access.SpeechRecorder;

import java.io.File;
import java.util.Date;
import java.util.Random;

public class ReadingOfSentences extends AppCompatActivity {

    private String path;
    private boolean isRecording = false;
    private SpeechRecorder recorder;
    String[] sentences;
    TextView text;
    Random rand;
    Button record;
    FeatureDataService featureDataService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading_of_sentences);
        sentences = getResources().getStringArray(R.array.Sentences);
        text = (TextView) findViewById(R.id.sentences);
        record = (Button) findViewById(R.id.recordSentences);
        rand = new Random();
        recorder = SpeechRecorder.getInstance(this, new VolumeHandler());
        featureDataService = new FeatureDataService(this);
        int pos = rand.nextInt(10);
        text.setText(sentences[pos]);
        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isRecording) {
                    recorder.stopRecording();
                    isRecording = false;
                    record.setText("aufnehmen");
                    Intent intent = new Intent(v.getContext(), GeneralRepetitionFinished.class);
                    intent.putExtra("exercise", "ReadingOfSentences");
                    if(getIntent().getBooleanExtra("trainingset", false)) {
                        SharedPreferences pref = getApplicationContext().getSharedPreferences("ExerciseFinished", 0);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putBoolean("ReadingOfSentences", true);
                        editor.apply();
                    }
                    recorder.release();
                    v.getContext().startActivity(intent);
                } else {
                    isRecording = true;
                    path = recorder.prepare("ReadingOfSentences");
                    recorder.record();
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

            final String state = bundle.getString("State", "Empty");
            if (state.equals("Finished")){

                float int_f0 = RadarFeatures.intonation(path);
                File file = new File(path);
                Date lastModDate = new Date(file.lastModified());
                featureDataService.save_feature(featureDataService.intonation_name, lastModDate, int_f0);
            }
        }
    }

}
