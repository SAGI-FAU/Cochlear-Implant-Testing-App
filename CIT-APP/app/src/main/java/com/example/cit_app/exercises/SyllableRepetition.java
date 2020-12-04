package com.example.cit_app.exercises;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.cit_app.RadarFeatures;
import com.example.cit_app.data_access.FeatureDataService;
import com.example.cit_app.data_access.PatientDA;
import com.example.cit_app.data_access.PatientDataService;
import com.example.cit_app.other_activities.GeneralRepetitionFinished;
import com.example.cit_app.R;
import com.example.cit_app.data_access.SpeechRecorder;
import com.example.cit_app.other_activities.Instruction;
import com.example.cit_app.other_activities.MainActivity;
import com.example.cit_app.other_activities.TrainingsetExerciseFinished;
import com.example.cit_app.other_activities.TrainingsetFinished;

import java.io.File;
import java.util.Date;
import java.util.Objects;

public class SyllableRepetition extends AppCompatActivity{

    private TextView timer;
    private ProgressBar progress;
    private Button start;
    private TextView syllable;
    private String path;
    private boolean isRecording, started;
    private SpeechRecorder recorder;
    private int exerciseCounter;
    private Context c = this;
    private FeatureDataService featureDataService;
    private float[] int_f0 = new float[2];
    private PatientDA patientDA;
    //These values were calculated from a personal sample
    private final float SPEECHRATEMEANMALE = 3.38095238095238f;
    private final float SPEECHRATEDEVMALE = 0.5133f;
    private final float SPEECHRATEMEANFEMALE = 3.1143f;
    private final float SPEECHRATEDEVFEMALE = 0.4518f;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_syllable_repetition);
        Objects.requireNonNull(getSupportActionBar()).setTitle(getResources().getString(R.string.SyllableRepetition)); // for set actionbar title
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //Initialize
        started = false;
        timer = findViewById(R.id.timer);
        progress = findViewById(R.id.timerProgress);
        start = findViewById(R.id.startTimer);
        syllable = findViewById(R.id.syllable);
        syllable.setText(getIntent().getExtras().getString("word"));
        timer.setText("5");
        if(getIntent().getExtras() != null)
            exerciseCounter = getIntent().getExtras().getInt("exerciseCounter", 0);
        featureDataService = new FeatureDataService(this);
        PatientDataService patientDataService = new PatientDataService(this);
        patientDA = patientDataService.getPatient();
        progress.getIndeterminateDrawable().setColorFilter(0xFFFF5722, android.graphics.PorterDuff.Mode.MULTIPLY);
        recorder = SpeechRecorder.getInstance(this, new SyllableRepetition.VolumeHandler(), "SyllableRepetition");
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
                            intent = new Intent(getApplicationContext(), TrainingsetExerciseFinished.class);
                            intent.putExtra("exerciseList", getIntent().getExtras().getStringArray("exerciseList"));
                            intent.putExtra("exerciseCounter", exerciseCounter);
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

    private class VolumeHandler extends Handler {

        public VolumeHandler() {
        }
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();

            final String state = bundle.getString("State", "Empty");
            if (state.equals("Finished")){

                if(getIntent().getBooleanExtra("trainingset", false)) {
                    int_f0 = RadarFeatures.voiceRate(path);
                    File file = new File(path);
                    Date lastModDate = new Date(file.lastModified());
                    if (patientDA.getGender().equals(getResources().getString(R.string.male))) {
                        //120Hz is the mean value of male test speakers from the info_sentences.csv dataset
                        int_f0[0] = (int_f0[1] - SPEECHRATEMEANMALE) / SPEECHRATEDEVMALE;
                        //120Hz is the mean value of male test speakers from the info_sentences.csv dataset
                        if (int_f0[0] < -5 || int_f0[0] > 5) {
                            int_f0[0] = 0;
                        } else {
                            if (int_f0[0] <= 1 && int_f0[0] >= -1) {
                                int_f0[0] = 1;
                            } else {
                                int_f0[0] = Math.abs((Math.abs(int_f0[0]) - 5) / 4f);
                            }
                        }
                    } else {
                        int_f0[0] = (int_f0[1] - SPEECHRATEMEANFEMALE) / SPEECHRATEDEVFEMALE;
                        //120Hz is the mean value of male test speakers from the info_sentences.csv dataset
                        if (int_f0[0] < -5 || int_f0[0] > 5) {
                            int_f0[0] = 0;
                        } else {
                            if (int_f0[0] <= 1 && int_f0[0] >= -1) {
                                int_f0[0] = 1;
                            } else {
                                int_f0[0] = Math.abs((Math.abs(int_f0[0]) - 5) / 4f);
                            }
                        }
                    }
                    featureDataService.save_feature(featureDataService.vrate_name, lastModDate, int_f0[0]);
                    featureDataService.save_feature(featureDataService.real_speech_rate_name, lastModDate, int_f0[1]);
                }
            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
}
