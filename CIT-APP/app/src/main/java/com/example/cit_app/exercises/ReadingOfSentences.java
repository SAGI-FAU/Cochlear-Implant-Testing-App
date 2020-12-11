/**
 * Created by Christoph Popp
 */

package com.example.cit_app.exercises;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cit_app.RadarFeatures;
import com.example.cit_app.data_access.FeatureDataService;
import com.example.cit_app.data_access.PatientDA;
import com.example.cit_app.data_access.PatientDataService;
import com.example.cit_app.other_activities.GeneralRepetitionFinished;
import com.example.cit_app.R;
import com.example.cit_app.data_access.SpeechRecorder;
import com.example.cit_app.other_activities.TrainingsetExerciseFinished;

import java.io.File;
import java.util.Date;
import java.util.Objects;
import java.util.Random;

public class ReadingOfSentences extends AppCompatActivity {

    private static String path;
    private boolean isRecording = false;
    private SpeechRecorder recorder;
    private int exerciseCounter;
    private TextView textRecord;
    private CardView record;
    private ImageView imageView;
    private FeatureDataService featureDataService;
    private PatientDA patientDA;
    //These values were calculated from a personal sample
    private final float INTONATIONMEANMALE = 79.0333333333333f;
    private final float INTONATIONDEVMALE = 20.7496f;
    private final float INTONATIONMEANFEMALE = 96.3429f;
    private final float INTONATIONDEVFEMALE = 19.9838f;
    private final float PITCHMEANFEMALE = 345.4286f;
    private final float PITCHDEVFEMALE = 29.2643f;
    private final float PITCHDEVMALE = 56.8301f;
    private final float PITCHMEANMALE = 257.919f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading_of_sentences);
        Objects.requireNonNull(getSupportActionBar()).setTitle(getResources().getString(R.string.SentenceReading)); // for set actionbar title
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Initialize
        String[] sentences = getResources().getStringArray(R.array.Sentences);
        TextView text = findViewById(R.id.sentences);
        record = findViewById(R.id.recordSentence);
        textRecord = findViewById(R.id.recordSentenceText);
        imageView = findViewById(R.id.recordSentenceImage);
        if(getIntent().getExtras() != null)
            exerciseCounter = getIntent().getExtras().getInt("exerciseCounter", 0);
        Random rand = new Random();
        recorder = SpeechRecorder.getInstance(this, new VolumeHandler(), "ReadingOfSentences");
        featureDataService = new FeatureDataService(this);
        PatientDataService patientDataService = new PatientDataService(this);
        patientDA = patientDataService.getPatient();
        int pos = rand.nextInt(10);
        text.setText(sentences[pos]);
        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isRecording) {
                    recorder.stopRecording();
                    isRecording = false;
                    textRecord.setText(getResources().getString(R.string.record));
                    imageView.setImageResource(R.drawable.play);
                } else {
                    if(!recorder.record().equals("not started")) {
                        isRecording = true;
                        path = recorder.prepare("ReadingOfSentences");
                        textRecord.setText(R.string.recording);
                        imageView.setImageResource(R.drawable.pause);
                    };
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
                    Toast.makeText(ReadingOfSentences.this, getResources().getString(R.string.messageAgain), Toast.LENGTH_SHORT).show();
                    return;
                }
                File f = new File(path);
                if (f.exists() && !f.isDirectory()) {
                    float[] int_f0 = RadarFeatures.intonation(path);
                    if (int_f0.length == 1) {
                        Toast.makeText(ReadingOfSentences.this, getResources().getString(R.string.messageAgain), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (!Float.isNaN(int_f0[0])) {
                        Intent intent = new Intent(getApplicationContext(), GeneralRepetitionFinished.class);
                        intent.putExtra("exercise", "ReadingOfSentences");
                        if (getIntent().getBooleanExtra("trainingset", false)) {
                            File file = new File(path);
                            Date lastModDate = new Date(file.lastModified());
                            //Calculate score in comparison to healthy test speaker
                            if (patientDA.getGender().equals(getResources().getString(R.string.male))) {
                                int_f0[0] = (int_f0[1] - INTONATIONMEANMALE)/INTONATIONDEVMALE;
                                if (int_f0[0] < -5 || int_f0[0] > 5) {
                                    int_f0[0] = 0;
                                } else {
                                    if (int_f0[0] <= 1 && int_f0[0] >= -1) {
                                        int_f0[0] = 1;
                                    } else {
                                        int_f0[0] = Math.abs((Math.abs(int_f0[0])-5)/4f);
                                    }
                                }
                            } else {
                                int_f0[0] = (int_f0[1] - INTONATIONMEANFEMALE)/INTONATIONDEVFEMALE;
                                if (int_f0[0] < -5 || int_f0[0] > 5) {
                                    int_f0[0] = 0;
                                } else {
                                    if (int_f0[0] <= 1 && int_f0[0] >= -1) {
                                        int_f0[0] = 1;
                                    } else {
                                        int_f0[0] = Math.abs((Math.abs(int_f0[0])-5)/4f);
                                    }
                                }
                            }

                            float pitch = 0.0f;
                            if(patientDA.getGender().equals(getResources().getString(R.string.male))) {
                                pitch = (int_f0[2] - PITCHMEANMALE) / PITCHDEVMALE;
                                if (pitch < -5 || pitch > 5) {
                                    pitch = 0;
                                } else {
                                    if (pitch <= 1 && pitch >= -1) {
                                        pitch = 1;
                                    } else {
                                        pitch = Math.abs((Math.abs(pitch) - 5) / 4f);
                                    }
                                }
                            } else {
                                pitch = (int_f0[2] - PITCHMEANFEMALE) / PITCHDEVFEMALE;
                                if (pitch < -5 || pitch > 5) {
                                    pitch = 0;
                                } else {
                                    if (pitch <= 1 && pitch >= -1) {
                                        pitch = 1;
                                    } else {
                                        pitch = Math.abs((Math.abs(pitch) - 5) / 4f);
                                    }
                                }
                            }
                            featureDataService.save_feature(featureDataService.intonation_name, lastModDate, int_f0[0]);
                            featureDataService.save_feature(featureDataService.real_intonation_name, lastModDate, int_f0[1]);
                            featureDataService.save_feature(featureDataService.pitch_mean_name, lastModDate, pitch);
                            featureDataService.save_feature(featureDataService.real_pitch_mean_name, lastModDate, int_f0[2]);
                            intent = new Intent(getApplicationContext(), TrainingsetExerciseFinished.class);
                            intent.putExtra("exerciseList", getIntent().getExtras().getStringArray("exerciseList"));
                            intent.putExtra("exerciseCounter", exerciseCounter);
                        }
                        record.setEnabled(false);
                        recorder.release();
                        getApplicationContext().startActivity(intent);
                    } else {
                        Toast.makeText(ReadingOfSentences.this, getResources().getString(R.string.messageEmpty), Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            }
        }
    }

    //This allows you to return to the activity before
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

}
