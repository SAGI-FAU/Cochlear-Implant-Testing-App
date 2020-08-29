package com.example.cit_app.exercises;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
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

import java.io.File;
import java.util.Date;
import java.util.Random;

public class ReadingOfSentences extends AppCompatActivity {

    private String path;
    private boolean isRecording = false;
    private SpeechRecorder recorder;
    String[] sentences;
    int exerciseCounter;
    TextView text, textRecord;
    ImageView imageView;
    Random rand;
    CardView record;
    FeatureDataService featureDataService;
    float int_f0 = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading_of_sentences);
        getSupportActionBar().setTitle(getResources().getString(R.string.SentenceReading)); // for set actionbar title
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sentences = getResources().getStringArray(R.array.Sentences);
        text = (TextView) findViewById(R.id.sentences);
        record = (CardView) findViewById(R.id.recordSentence);
        textRecord = (TextView) findViewById(R.id.recordSentenceText);
        imageView = (ImageView) findViewById(R.id.recordSentenceImage);
        if(getIntent().getExtras() != null)
            exerciseCounter = getIntent().getExtras().getInt("exerciseCounter", 0) + 1;
        rand = new Random();
        recorder = SpeechRecorder.getInstance(this, new VolumeHandler(), "ReadingOfSentences");
        featureDataService = new FeatureDataService(this);
        int pos = rand.nextInt(10);
        text.setText(sentences[pos]);
        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isRecording) {
                    recorder.stopRecording();
                    isRecording = false;
                    textRecord.setText("aufnehmen");
                    imageView.setImageResource(R.drawable.play);
                    if(int_f0 == -1 || Float.isNaN(int_f0)) {

                    } else {
                        Intent intent = new Intent(v.getContext(), GeneralRepetitionFinished.class);
                        intent.putExtra("exercise", "ReadingOfSentences");
                        if(getIntent().getBooleanExtra("trainingset", false)) {
                            SharedPreferences pref = getApplicationContext().getSharedPreferences("ExerciseFinished", 0);
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putBoolean("ReadingOfSentences", true);
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
                                    case "Picture_Description":
                                        intent = new Intent(v.getContext(), Instruction.class);
                                        intent.putExtra("title", getResources().getString(R.string.PictureDescription));
                                        //TODO Think of some useful descriptions and instructions
                                        intent.putExtra("description", getResources().getString(R.string.DescriptionPictureDescription));
                                        intent.putExtra("instruction", getResources().getString(R.string.ExplanationPictureDescription));
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
                    }
                } else {
                    isRecording = true;
                    path = recorder.prepare("ReadingOfSentences");
                    recorder.record();
                    textRecord.setText(R.string.recording);
                    imageView.setImageResource(R.drawable.pause);
                }
            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        if(!getIntent().getExtras().getBoolean("trainingset"))
            finish();
        return super.onOptionsItemSelected(item);
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

                int_f0 = RadarFeatures.intonation(path);
                if(Float.isNaN(int_f0)) {

                } else {
                    File file = new File(path);
                    Date lastModDate = new Date(file.lastModified());
                    featureDataService.save_feature(featureDataService.intonation_name, lastModDate, int_f0);
                }
            }
        }
    }

}
