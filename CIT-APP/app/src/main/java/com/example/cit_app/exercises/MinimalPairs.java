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
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cit_app.other_activities.GeneralRepetitionFinished;
import com.example.cit_app.R;
import com.example.cit_app.data_access.SpeechRecorder;
import com.example.cit_app.other_activities.Instruction;
import com.example.cit_app.other_activities.MainActivity;
import com.example.cit_app.other_activities.TrainingsetFinished;

import java.util.ArrayList;
import java.util.Random;

public class MinimalPairs extends AppCompatActivity {

    CardView listen, record;
    String path = "";
    int counter = 1;
    int exerciseCounter;
    private SpeechRecorder recorder;
    private int position = 0;
    private Random random = new Random();
    MediaPlayer player;
    private ArrayList<Integer> usedNumber = new ArrayList<>();
    boolean isRecording;
    boolean isPlaying;
    ImageView imageView;
    private String[] minimal_pairs = new String[20];
    private String[] minimal_pairs_fricatives, minimal_pairs_stops, minimal_pairs_general;
    TextView text, recordText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minimal_pairs);
        getSupportActionBar().setTitle(getResources().getString(R.string.MinimalPairs)); // for set actionbar title
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        record = (CardView) findViewById(R.id.record);
        listen = (CardView) findViewById(R.id.listen);
        recordText = (TextView) findViewById(R.id.recordText);
        imageView = (ImageView) findViewById(R.id.recordImage);
        imageView.setImageResource(R.drawable.play);
        if(getIntent().getExtras() != null)
            exerciseCounter = getIntent().getExtras().getInt("exerciseCounter", 0) + 1;
        isRecording = false;
        isPlaying = false;
        text = (TextView) findViewById(R.id.wordNumber);
        recorder = SpeechRecorder.getInstance(this, new MinimalPairs.VolumeHandler(), "Minimal_Pairs");
        text.setText(counter + " / 20");
        minimal_pairs_fricatives = getResources().getStringArray(R.array.Minimal_Pairs_Fricatives);
        minimal_pairs_stops = getResources().getStringArray(R.array.Minimal_Pairs_Stops);
        minimal_pairs_general = getResources().getStringArray(R.array.Minimal_Pairs);
        //fill minimal_pairs list with 20 pairs consisting of 10 random 5 fricative and 5 stop pairs
        for(int i = 0; i < 20; i+=4) {
            position = 4 * random.nextInt(50);
            if(i < 1) {
                while(usedNumber.contains(position)) {
                    position = 4 * random.nextInt(50);
                }
                minimal_pairs[i] = minimal_pairs_general[position];
                minimal_pairs[i + 1] = minimal_pairs_general[position + 1];
                minimal_pairs[i + 2] = minimal_pairs_general[position + 2];
                minimal_pairs[i + 3] = minimal_pairs_general[position + 3];
                usedNumber.add(position);
            } else {
                if(i < 19) {
                    if(i == 4) {
                        usedNumber.clear();
                    }
                    position = 4 * random.nextInt(12);
                    while(usedNumber.contains(position)) {
                        position = 4 * random.nextInt(12);
                    }
                    minimal_pairs[i] = minimal_pairs_fricatives[position];
                    minimal_pairs[i + 1] = minimal_pairs_fricatives[position + 1];
                    minimal_pairs[i + 2] = minimal_pairs_fricatives[position + 2];
                    minimal_pairs[i + 3] = minimal_pairs_fricatives[position + 3];
                    usedNumber.add(position);
                } else {
                    if(i == 12) {
                        usedNumber.clear();
                    }
                    position = 4 * random.nextInt(7);
                    while(usedNumber.contains(position)) {
                        position = 4 * random.nextInt(7);
                    }
                    minimal_pairs[i] = minimal_pairs_stops[position];
                    minimal_pairs[i + 1] = minimal_pairs_stops[position + 1];
                    minimal_pairs[i + 2] = minimal_pairs_stops[position + 2];
                    minimal_pairs[i + 3] = minimal_pairs_stops[position + 3];
                    usedNumber.add(position);
                }
            }
        }
        listen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isRecording) {

                } else {
                    if(player != null) {
                        player.seekTo(0);
                        player.start();
                    } else {
                        if(counter > 21){

                        } else {
                            minimal_pairs[counter - 1] = minimal_pairs[counter - 1].replace("ä", "ae");
                            minimal_pairs[counter - 1] = minimal_pairs[counter - 1].replace("ö", "oe");
                            minimal_pairs[counter - 1] = minimal_pairs[counter - 1].replace("ü", "ue");
                            minimal_pairs[counter - 1] = minimal_pairs[counter - 1].replace("ß", "ss");
                            String file = minimal_pairs[counter - 1].toLowerCase();
                            int resId = getResources().getIdentifier(file, "raw", getPackageName());
                            String path = "a" + resId;
                            String path2 = path.substring(1);
                            player = MediaPlayer.create(v.getContext(), Integer.parseInt(path2));
                            player.start();
                        }
                    }
                }
            }
        });
        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isRecording) {
                    if(counter > 19) {
                        isRecording = false;
                        recordText.setText("aufnehmen");
                        Intent intent = new Intent(v.getContext(), GeneralRepetitionFinished.class);
                        intent.putExtra("exercise", "MinimalPairs");
                        if(getIntent().getBooleanExtra("trainingset", false)) {
                            SharedPreferences pref = getApplicationContext().getSharedPreferences("ExerciseFinished", 0);
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putBoolean("MinimalPairs", true);
                            editor.apply();
                            if(exerciseCounter >= getIntent().getExtras().getStringArray("exerciseList").length) {
                                intent = new Intent(v.getContext(), TrainingsetFinished.class);
                                intent.putExtra("intonation", getIntent().getExtras().getFloatArray("intonation"));
                                intent.putExtra("realValues", getIntent().getExtras().getFloatArray("realValues"));
                            } else {
                                switch (getIntent().getExtras().getStringArray("exerciseList")[exerciseCounter]) {
                                    case "MinimalPairs2":
                                        intent = new Intent(v.getContext(), Instruction.class);
                                        intent.putExtra("title", getResources().getString(R.string.MinimalPairs2));
                                        //TODO Think of some useful descriptions and instructions
                                        intent.putExtra("description", getResources().getString(R.string.DescriptionMinPairs2));
                                        intent.putExtra("instruction", getResources().getString(R.string.ExplanationMinPairs2));
                                        intent.putExtra("exerciseList", getIntent().getExtras().getStringArray("exerciseList"));
                                        intent.putExtra("exerciseCounter", exerciseCounter);
                                        intent.putExtra("intonation", getIntent().getExtras().getFloatArray("intonation"));
                                        intent.putExtra("realValues", getIntent().getExtras().getFloatArray("realValues"));
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
                                        intent.putExtra("intonation", getIntent().getExtras().getFloatArray("intonation"));
                                        intent.putExtra("realValues", getIntent().getExtras().getFloatArray("realValues"));
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
                                        intent.putExtra("intonation", getIntent().getExtras().getFloatArray("intonation"));
                                        intent.putExtra("realValues", getIntent().getExtras().getFloatArray("realValues"));
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
                                        intent.putExtra("intonation", getIntent().getExtras().getFloatArray("intonation"));
                                        intent.putExtra("realValues", getIntent().getExtras().getFloatArray("realValues"));
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
                                        intent.putExtra("intonation", getIntent().getExtras().getFloatArray("intonation"));
                                        intent.putExtra("realValues", getIntent().getExtras().getFloatArray("realValues"));
                                        intent.putExtra("trainingset", true);
                                        break;
                                    default:
                                        intent = new Intent(v.getContext(), MainActivity.class);
                                        break;
                                }
                            }
                        }
                        recorder.stopRecording();
                        recorder.release();
                        v.getContext().startActivity(intent);
                    } else {
                        recorder.stopRecording();
                        isRecording = false;
                        imageView.setImageResource(R.drawable.play);
                        recordText.setText("aufnehmen");
                        counter++;
                        text.setText(counter + " / 20");
                        if(player != null) {
                            player.stop();
                            player.release();
                            player = null;
                        }
                    }
                } else {
                    if(player != null)
                        player.stop();
                    path = recorder.prepare("Minimal_Pairs");
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
            final int currentVolume = (int) bundle.getDouble("Volume");

            final String state = bundle.getString("State", "Empty");
            if (state.equals("Finished")){
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
