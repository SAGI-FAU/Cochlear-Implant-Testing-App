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

import com.example.cit_app.other_activities.GeneralRepetitionFinished;
import com.example.cit_app.R;
import com.example.cit_app.data_access.SpeechRecorder;

public class Picture_Description extends AppCompatActivity {
    private boolean isRecording = false;
    private String path;
    private SpeechRecorder recorder;
    Button record;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture__description);
        record = (Button) findViewById(R.id.recordPicture);
        recorder = SpeechRecorder.getInstance(this, new Picture_Description.VolumeHandler());
        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isRecording) {
                    recorder.stopRecording();
                    isRecording = false;
                    record.setText("aufnehmen");
                    Intent intent = new Intent(v.getContext(), GeneralRepetitionFinished.class);
                    intent.putExtra("exercise", "Picture description");
                    if(getIntent().getBooleanExtra("trainingset", false)) {
                        SharedPreferences pref = getApplicationContext().getSharedPreferences("ExerciseFinished", 0);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putBoolean("Picture_Description", true);
                        editor.apply();
                    }
                    recorder.release();
                    v.getContext().startActivity(intent);
                } else {
                    path = recorder.prepare("Picture_Description");
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
