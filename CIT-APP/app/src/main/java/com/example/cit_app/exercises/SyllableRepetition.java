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

import com.example.cit_app.other_activities.GeneralRepetitionFinished;
import com.example.cit_app.R;
import com.example.cit_app.data_access.SpeechRecorder;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_syllable_repetition);
        started = false;
        timer = (TextView) findViewById(R.id.timer);
        progress = (ProgressBar) findViewById(R.id.timerProgress);
        progress.getIndeterminateDrawable().setColorFilter(0xFFFF5722, android.graphics.PorterDuff.Mode.MULTIPLY);
        start = (Button) findViewById(R.id.startTimer);
        recorder = SpeechRecorder.getInstance(this, new SyllableRepetition.VolumeHandler());
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!started) {
                    started = true;
                    startTimer();
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
