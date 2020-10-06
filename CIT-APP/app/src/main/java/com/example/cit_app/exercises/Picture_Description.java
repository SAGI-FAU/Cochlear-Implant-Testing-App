package com.example.cit_app.exercises;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
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
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.Random;

public class Picture_Description extends AppCompatActivity {
    private boolean isRecording = false;
    private static String path;
    private SpeechRecorder recorder;
    private SubsamplingScaleImageView picture;
    private int exerciseCounter;
    private FeatureDataService featureDataService;
    private  ImageView imageView;
    private  TextView recordText;
    private PatientDA patientDA;
    private CardView record;
    private int[] images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture__description);
        Objects.requireNonNull(getSupportActionBar()).setTitle(getResources().getString(R.string.PictureDescription)); // for set actionbar title
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        images = new int[8];
        //Initialize
        if(getIntent().getExtras() != null)
            exerciseCounter = getIntent().getExtras().getInt("exerciseCounter", 0);
        record = findViewById(R.id.recordPicture);
        picture = findViewById(R.id.picture);
        recorder = SpeechRecorder.getInstance(this, new Picture_Description.VolumeHandler(), "Picture_Description");
        recordText = findViewById(R.id.recordPictureText);
        imageView = findViewById(R.id.recordPictureImage);
        featureDataService = new FeatureDataService(this);
        PatientDataService patientDataService = new PatientDataService(this);
        patientDA = patientDataService.getPatient();
        Calendar c  = Calendar.getInstance();
        if(c.get(Calendar.YEAR) - (patientDA.getBirthday().getYear() + 1900) < 13) {
            images[0] = R.drawable.picture_description_child1;
            images[1] = R.drawable.picture_description_child2;
            images[2] = R.drawable.picture_description_child3;
            images[3] = R.drawable.picture_description_child4;
            images[4] = R.drawable.picture_description_child5;
            images[5] = R.drawable.picture_description_child6;
            images[6] = R.drawable.picture_description_child7;
            images[7] = R.drawable.cookie;
        } else {
            images[0] = R.drawable.picture_description_adults1;
            images[1] = R.drawable.picture_description_adults2;
            images[2] = R.drawable.picture_description_adults3;
            images[3] = R.drawable.picture_description_adults4;
            images[4] = R.drawable.picture_description_adults5;
            images[5] = R.drawable.picture_description_adults6;
            images[6] = R.drawable.picture_description_adults7;
            images[7] = R.drawable.cookie;
        }
        Random rand = new Random();
        int choose = rand.nextInt(7);
        ImageSource src = ImageSource.resource(images[choose]);
        picture.setImage(src);
        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isRecording) {
                    recorder.stopRecording();
                    imageView.setImageResource(R.drawable.play);
                    isRecording = false;
                    recordText.setText(getResources().getString(R.string.record));
                } else {
                    path = recorder.prepare("Picture_Description");
                    recorder.record();
                    imageView.setImageResource(R.drawable.pause);
                    isRecording = true;
                    recordText.setText(R.string.recording);
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
            if (state.equals("Finished")) {
                if(path == null) {
                    Toast.makeText(Picture_Description.this, getResources().getString(R.string.messageAgain), Toast.LENGTH_SHORT).show();
                    return;
                }
                File f = new File(path);
                if (f.exists() && !f.isDirectory()) {
                    float[] int_f0 = RadarFeatures.intonation(path);
                    if (int_f0.length == 1) {
                        Toast.makeText(Picture_Description.this, getResources().getString(R.string.messageAgain), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (Float.isNaN(int_f0[0])) {
                        Toast.makeText(Picture_Description.this, getResources().getString(R.string.messageEmpty), Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        File file = new File(path);
                        Date lastModDate = new Date(file.lastModified());
                        if (patientDA.getGender().equals(getResources().getString(R.string.male))) {
                            //120Hz is the mean value of male test speakers from the info_sentences.csv dataset
                            if (int_f0[1] >= 17.5) {
                                int_f0[0] = 1;
                            } else {
                                int_f0[0] = int_f0[1] / 17.5f;
                            }
                        } else {
                            //120Hz is the mean value of female test speakers from the info_sentences.csv dataset
                            if (int_f0[1] >= 31.4) {
                                int_f0[0] = 1;
                            } else {
                                int_f0[0] = int_f0[1] / 31.4f;
                            }
                        }
                        featureDataService.save_feature(featureDataService.intonation_name, lastModDate, int_f0[0]);
                        featureDataService.save_feature(featureDataService.real_intonation_name, lastModDate, int_f0[1]);
                        featureDataService.save_feature(featureDataService.pitch_mean_name, lastModDate, int_f0[2]);
                        Intent intent = new Intent(getApplicationContext(), GeneralRepetitionFinished.class);
                        intent.putExtra("exercise", "Picture description");
                        if (getIntent().getBooleanExtra("trainingset", false)) {
                            intent = new Intent(getApplicationContext(), TrainingsetExerciseFinished.class);
                            intent.putExtra("exerciseList", getIntent().getExtras().getStringArray("exerciseList"));
                            intent.putExtra("exerciseCounter", exerciseCounter);
                        }
                        record.setEnabled(false);
                        recorder.release();
                        getApplicationContext().startActivity(intent);
                    }
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
