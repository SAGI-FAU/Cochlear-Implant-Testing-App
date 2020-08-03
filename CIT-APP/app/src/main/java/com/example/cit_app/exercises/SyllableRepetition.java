package com.example.cit_app.exercises;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.cit_app.RadarFeatures;
import com.example.cit_app.data_access.FeatureDataService;
import com.example.cit_app.other_activities.GeneralRepetitionFinished;
import com.example.cit_app.R;
import com.example.cit_app.data_access.SpeechRecorder;
import com.example.cit_app.other_activities.Instruction;
import com.example.cit_app.other_activities.MainActivity;
import com.example.cit_app.other_activities.TrainingsetFinished;

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
    int exerciseCounter;
    private CountDownTimer cdt;
    private Context c = this;
    int counter = 5000;
    private FeatureDataService featureDataService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_syllable_repetition);
        getSupportActionBar().setTitle(getResources().getString(R.string.SyllableRepetition)); // for set actionbar title
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        started = false;
        timer = (TextView) findViewById(R.id.timer);
        timer.setText("5");
        if(getIntent().getExtras() != null)
            exerciseCounter = getIntent().getExtras().getInt("exerciseCounter", 0) + 1;
        featureDataService = new FeatureDataService(this);
        progress = (ProgressBar) findViewById(R.id.timerProgress);
        progress.getIndeterminateDrawable().setColorFilter(0xFFFF5722, android.graphics.PorterDuff.Mode.MULTIPLY);
        start = (Button) findViewById(R.id.startTimer);
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
                            SharedPreferences pref = getApplicationContext().getSharedPreferences("ExerciseFinished", 0);
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putBoolean("SyllableRepetition", true);
                            editor.apply();
                            if(exerciseCounter >= getIntent().getExtras().getStringArray("exerciseList").length) {
                                intent = new Intent(c, TrainingsetFinished.class);
                            } else {
                                switch (getIntent().getExtras().getStringArray("exerciseList")[exerciseCounter]) {
                                    case "MinimalPairs":
                                        intent = new Intent(c, Instruction.class);
                                        intent.putExtra("title", getResources().getString(R.string.MinimalPairs));
                                        //TODO Think of some useful descriptions and instructions
                                        intent.putExtra("description", getResources().getString(R.string.DescriptionMinPairs));
                                        intent.putExtra("instruction", getResources().getString(R.string.ExplanationMinPairs));
                                        intent.putExtra("exerciseList", getIntent().getExtras().getStringArray("exerciseList"));
                                        intent.putExtra("exerciseCounter", exerciseCounter);
                                        intent.putExtra("trainingset", true);
                                        break;
                                    case "MinimalPairs2":
                                        intent = new Intent(c, Instruction.class);
                                        intent.putExtra("title", getResources().getString(R.string.MinimalPairs2));
                                        //TODO Think of some useful descriptions and instructions
                                        intent.putExtra("description", getResources().getString(R.string.DescriptionMinPairs2));
                                        intent.putExtra("instruction", getResources().getString(R.string.ExplanationMinPairs2));
                                        intent.putExtra("exerciseList", getIntent().getExtras().getStringArray("exerciseList"));
                                        intent.putExtra("exerciseCounter", exerciseCounter);
                                        intent.putExtra("trainingset", true);
                                        break;
                                    case "Word_List":
                                        intent = new Intent(c, Instruction.class);
                                        intent.putExtra("title", getResources().getString(R.string.WordList));
                                        //TODO Think of some useful descriptions and instructions
                                        intent.putExtra("description", getResources().getString(R.string.DescriptionWordList));
                                        intent.putExtra("instruction", getResources().getString(R.string.ExplanationWordList));
                                        intent.putExtra("exerciseList", getIntent().getExtras().getStringArray("exerciseList"));
                                        intent.putExtra("exerciseCounter", exerciseCounter);
                                        intent.putExtra("trainingset", true);
                                        break;
                                    case "ReadingOfSentences":
                                        intent = new Intent(c, Instruction.class);
                                        intent.putExtra("title", getResources().getString(R.string.SentenceReading));
                                        //TODO Think of some useful descriptions and instructions
                                        intent.putExtra("description", getResources().getString(R.string.DescriptionSentenceReading));
                                        intent.putExtra("instruction", getResources().getString(R.string.ExplanationSentenceReading));
                                        intent.putExtra("exerciseList", getIntent().getExtras().getStringArray("exerciseList"));
                                        intent.putExtra("exerciseCounter", exerciseCounter);
                                        intent.putExtra("trainingset", true);
                                        break;
                                    case "Picture_Description":
                                        intent = new Intent(c, Instruction.class);
                                        intent.putExtra("title", getResources().getString(R.string.PictureDescription));
                                        //TODO Think of some useful descriptions and instructions
                                        intent.putExtra("description", getResources().getString(R.string.DescriptionPictureDescription));
                                        intent.putExtra("instruction", getResources().getString(R.string.ExplanationPictureDescription));
                                        intent.putExtra("exerciseList", getIntent().getExtras().getStringArray("exerciseList"));
                                        intent.putExtra("exerciseCounter", exerciseCounter);
                                        intent.putExtra("trainingset", true);
                                        break;
                                    default:
                                        intent = new Intent(c, MainActivity.class);
                                        break;
                                }
                            }
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

                float int_f0 = RadarFeatures.voiceRate(path);
                File file = new File(path);
                Date lastModDate = new Date(file.lastModified());
                featureDataService.save_feature(featureDataService.vrate_name, lastModDate, int_f0);
            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        if(!getIntent().getExtras().getBoolean("trainingset"))
            finish();
        return super.onOptionsItemSelected(item);
    }
}
