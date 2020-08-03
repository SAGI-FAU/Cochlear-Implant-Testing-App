package com.example.cit_app.exercises;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
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
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cit_app.RadarFeatures;
import com.example.cit_app.data_access.FeatureDataService;
import com.example.cit_app.other_activities.GeneralRepetitionFinished;
import com.example.cit_app.R;
import com.example.cit_app.data_access.SpeechRecorder;
import com.example.cit_app.other_activities.Instruction;
import com.example.cit_app.other_activities.MainActivity;
import com.example.cit_app.other_activities.TrainingsetFinished;

import org.w3c.dom.Text;

import java.io.File;
import java.util.Date;

public class Picture_Description extends AppCompatActivity {
    private boolean isRecording = false;
    private String path;
    private SpeechRecorder recorder;
    int exerciseCounter;
    CardView record;
    FeatureDataService featureDataService;
    ImageView imageView;
    TextView recordText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture__description);
        getSupportActionBar().setTitle(getResources().getString(R.string.PictureDescription)); // for set actionbar title
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if(getIntent().getExtras() != null)
            exerciseCounter = getIntent().getExtras().getInt("exerciseCounter", 0) + 1;
        record = (CardView) findViewById(R.id.recordPicture);
        recorder = SpeechRecorder.getInstance(this, new Picture_Description.VolumeHandler(), "Picture_Description");
        recordText = (TextView) findViewById(R.id.recordPictureText);
        imageView = (ImageView) findViewById(R.id.recordPictureImage);
        featureDataService = new FeatureDataService(this);
        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isRecording) {
                    recorder.stopRecording();
                    imageView.setImageResource(R.drawable.play);
                    isRecording = false;
                    recordText.setText("aufnehmen");
                    Intent intent = new Intent(v.getContext(), GeneralRepetitionFinished.class);
                    intent.putExtra("exercise", "Picture description");
                    if(getIntent().getBooleanExtra("trainingset", false)) {
                        SharedPreferences pref = getApplicationContext().getSharedPreferences("ExerciseFinished", 0);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putBoolean("Picture_Description", true);
                        editor.apply();
                        if(exerciseCounter >= getIntent().getExtras().getStringArray("exerciseList").length) {
                            intent = new Intent(v.getContext(), TrainingsetFinished.class);
                        } else {
                            switch (getIntent().getExtras().getStringArray("exerciseList")[exerciseCounter]) {
                                case "MinimalPairs":
                                    intent = new Intent(v.getContext(), Instruction.class);
                                    intent.putExtra("title", getResources().getString(R.string.MinimalPairs));
                                    //TODO Think of some useful descriptions and instructions
                                    intent.putExtra("description", getResources().getString(R.string.DescriptionMinPairs));
                                    intent.putExtra("instruction", getResources().getString(R.string.ExplanationMinPairs));
                                    intent.putExtra("exerciseList", getIntent().getExtras().getStringArray("exerciseList"));
                                    intent.putExtra("exerciseCounter", exerciseCounter);
                                    intent.putExtra("trainingset", true);
                                    break;
                                case "MinimalPairs2":
                                    intent = new Intent(v.getContext(), Instruction.class);
                                    intent.putExtra("title", getResources().getString(R.string.MinimalPairs2));
                                    //TODO Think of some useful descriptions and instructions
                                    intent.putExtra("description", getResources().getString(R.string.DescriptionMinPairs2));
                                    intent.putExtra("instruction", getResources().getString(R.string.ExplanationMinPairs2));
                                    intent.putExtra("exerciseList", getIntent().getExtras().getStringArray("exerciseList"));
                                    intent.putExtra("exerciseCounter", exerciseCounter);
                                    intent.putExtra("trainingset", true);
                                    break;
                                case "Word_List":
                                    intent = new Intent(v.getContext(), Instruction.class);
                                    intent.putExtra("title", getResources().getString(R.string.WordList));
                                    //TODO Think of some useful descriptions and instructions
                                    intent.putExtra("description", getResources().getString(R.string.DescriptionWordList));
                                    intent.putExtra("instruction", getResources().getString(R.string.ExplanationWordList));
                                    intent.putExtra("exerciseList", getIntent().getExtras().getStringArray("exerciseList"));
                                    intent.putExtra("exerciseCounter", exerciseCounter);
                                    intent.putExtra("trainingset", true);
                                    break;
                                case "ReadingOfSentences":
                                    intent = new Intent(v.getContext(), Instruction.class);
                                    intent.putExtra("title", getResources().getString(R.string.SentenceReading));
                                    //TODO Think of some useful descriptions and instructions
                                    intent.putExtra("description", getResources().getString(R.string.DescriptionSentenceReading));
                                    intent.putExtra("instruction", getResources().getString(R.string.ExplanationSentenceReading));
                                    intent.putExtra("exerciseList", getIntent().getExtras().getStringArray("exerciseList"));
                                    intent.putExtra("exerciseCounter", exerciseCounter);
                                    intent.putExtra("trainingset", true);
                                    break;
                                case "SyllableRepetition":
                                    intent = new Intent(v.getContext(), Instruction.class);
                                    intent.putExtra("title", getResources().getString(R.string.SyllableRepetition));
                                    //TODO Think of some useful descriptions and instructions
                                    intent.putExtra("description", getResources().getString(R.string.DescriptionSyllableRepetition));
                                    intent.putExtra("instruction", getResources().getString(R.string.ExplanationSyllableRepetition));
                                    intent.putExtra("exerciseList", getIntent().getExtras().getStringArray("exerciseList"));
                                    intent.putExtra("exerciseCounter", exerciseCounter);
                                    intent.putExtra("trainingset", true);
                                    break;
                                default:
                                    intent = new Intent(v.getContext(), MainActivity.class);
                                    break;
                            }
                        }
                    }
                    recorder.release();
                    v.getContext().startActivity(intent);
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
            if (state.equals("Finished")){

                float int_f0 = RadarFeatures.intonation(path);
                File file = new File(path);
                Date lastModDate = new Date(file.lastModified());
                featureDataService.save_feature(featureDataService.intonation_name, lastModDate, int_f0);
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
