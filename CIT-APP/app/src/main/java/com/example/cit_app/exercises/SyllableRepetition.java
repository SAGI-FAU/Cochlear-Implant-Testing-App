package com.example.cit_app.exercises;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cit_app.RadarFeatures;
import com.example.cit_app.data_access.FeatureDataService;
import com.example.cit_app.other_activities.GeneralRepetitionFinished;
import com.example.cit_app.R;
import com.example.cit_app.data_access.SpeechRecorder;

import java.io.File;
import java.util.Date;

public class SyllableRepetition extends AppCompatActivity{

    //TODO Add recording of voice
    private TextView timer;
    private ProgressBar progress;
    private Button start;
    private String path;
    private boolean isRecording, started;
    private SpeechRecorder recorder;
    private CountDownTimer cdt;
    private Context c = this;
    int counter = 5000;
    private FeatureDataService featureDataService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_syllable_repetition);
        started = false;
        timer = (TextView) findViewById(R.id.timer);
        timer.setText("5");
        featureDataService = new FeatureDataService(this);
        progress = (ProgressBar) findViewById(R.id.timerProgress);
        progress.getIndeterminateDrawable().setColorFilter(0xFFFF5722, android.graphics.PorterDuff.Mode.MULTIPLY);
        start = (Button) findViewById(R.id.startTimer);
        recorder = SpeechRecorder.getInstance(this, new SyllableRepetition.VolumeHandler());
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!started) {
                    started = true;
                    //startTimer();
                    MyCountDownTimer myCountDownTimer = new MyCountDownTimer(4000, 1000);
                    myCountDownTimer.Start();
                    if (isRecording) {
                    } else {
                        path = recorder.prepare("SyllableRepetition");
                        recorder.record();
                        isRecording = true;
                        start.setText(R.string.recording);
                    }
                }
            }
        });
    }

    class MyCountDownTimer {
        private long millisInFuture;
        private long countDownInterval;
        public MyCountDownTimer(long pMillisInFuture, long pCountDownInterval) {
            this.millisInFuture = pMillisInFuture;
            this.countDownInterval = pCountDownInterval;
        }
        public void Start()
        {
            final Handler handler = new Handler();
            final Runnable counter = new Runnable(){

                public void run(){
                    if(millisInFuture <= 0) {
                        timer.setText("" + millisInFuture/1000);
                        progress.setProgress((int)(millisInFuture/1000) * 20);
                        isRecording = false;
                        started = false;
                        Intent intent = new Intent(c, GeneralRepetitionFinished.class);
                        intent.putExtra("exercise", "SyllableRepetition");
                        if(getIntent().getBooleanExtra("trainingset", false)) {
                            SharedPreferences pref = getApplicationContext().getSharedPreferences("ExerciseFinished", 0);
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putBoolean("SyllableRepetition", true);
                            editor.apply();
                        }
                        recorder.stopRecording();
                        recorder.release();
                        c.startActivity(intent);
                    } else {
                        long sec = millisInFuture/1000;
                        timer.setText("" + sec);
                        progress.setProgress((int)sec * 20);
                        millisInFuture -= countDownInterval;
                        handler.postDelayed(this, countDownInterval);
                    }
                }
            };

            handler.postDelayed(counter, countDownInterval);
        }
    }

    /*public void startTimer(){
        cdt = new CountDownTimer(counter, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timer.setText("" + millisUntilFinished);
                progress.setProgress((counter/1000) * 20);
                counter = millisUntilFinished;
            }

            @Override
            public void onFinish() {
                isRecording = false;
                started = false;
                Intent intent = new Intent(c, GeneralRepetitionFinished.class);
                intent.putExtra("exercise", "SyllableRepetition");
                if(getIntent().getBooleanExtra("trainingset", false)) {
                    SharedPreferences pref = getApplicationContext().getSharedPreferences("ExerciseFinished", 0);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putBoolean("SyllableRepetition", true);
                    editor.apply();
                }
                recorder.stopRecording();
                recorder.release();
                c.startActivity(intent);
            }
        }.start();
    }*/

    private class VolumeHandler extends Handler {

        public VolumeHandler() {
        }
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();

            final String state = bundle.getString("State", "Empty");
            if (state.equals("Finished")){

                float int_f0 = RadarFeatures.voiceRate(path);
                Toast.makeText(c, "" + int_f0, Toast.LENGTH_SHORT).show();
                File file = new File(path);
                Date lastModDate = new Date(file.lastModified());
                featureDataService.save_feature(featureDataService.vrate_name, lastModDate, int_f0);
            }
        }
    }
}
