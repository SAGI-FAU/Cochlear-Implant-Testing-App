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

import com.example.cit_app.other_activities.GeneralRepetitionFinished;
import com.example.cit_app.R;
import com.example.cit_app.data_access.SpeechRecorder;
import com.example.cit_app.other_activities.Instruction;
import com.example.cit_app.other_activities.MainActivity;
import com.example.cit_app.other_activities.TrainingsetExerciseFinished;
import com.example.cit_app.other_activities.TrainingsetFinished;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class Word_List extends AppCompatActivity {
    private boolean isRecording = false;
    private SpeechRecorder recorder;
    private TextView word, text, recordText;
    private ImageView imageView;
    private  Random rand = new Random();
    private  int exerciseCounter;
    private  int counter = 1;
    private   ArrayList<Integer> usedInts = new ArrayList<>();
    private String[] wordList;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word__list);
        Objects.requireNonNull(getSupportActionBar()).setTitle(getResources().getString(R.string.WordList)); // for set actionbar title
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //Initialize
        wordList = new String[10];
        if(getIntent().getExtras() != null)
            exerciseCounter = getIntent().getExtras().getInt("exerciseCounter", 0);
        String[] wordListFricative = getResources().getStringArray(R.array.Word_List_Fricatives);
        String[] wordListPlosive = getResources().getStringArray(R.array.Word_List_Plosives);
        String[] wordListNasal = getResources().getStringArray(R.array.Word_List_Nasal);
        String[] wordListSibilant = getResources().getStringArray(R.array.Word_List_Sibilant);
        word = findViewById(R.id.word);
        text = findViewById(R.id.wordNumber3);
        CardView record = findViewById(R.id.recordWordList);
        recordText = findViewById(R.id.recordWordListText);
        imageView = findViewById(R.id.recordWordListImage);
        recorder = SpeechRecorder.getInstance(this, new Word_List.VolumeHandler(), "Word_List");
        text.setText(counter + " / 10");
        //Find random words but at least 2 plosives, 2 nasal, 2 sibilant and 4 fricative sounds
        for(int i = 0; i < 10; i++) {
            int tmp = -1;
            if(i < 2) {
                while((tmp == -1) || usedInts.contains(tmp)) {
                    tmp = rand.nextInt(63);
                }
                wordList[i] = wordListPlosive[tmp];
                usedInts.add(tmp);
            } else {
                if(i < 4) {
                    while((tmp == -1) || usedInts.contains(tmp)) {
                        tmp = rand.nextInt(10);
                    }
                    wordList[i] = wordListNasal[tmp];
                    usedInts.add(tmp);
                } else {
                    if(i < 6) {
                        while((tmp == -1) || usedInts.contains(tmp)) {
                            tmp = rand.nextInt(10);
                        }
                        wordList[i] = wordListSibilant[tmp];
                        usedInts.add(tmp);
                    } else {
                        while((tmp == -1) || usedInts.contains(tmp)) {
                            tmp = rand.nextInt(14);
                        }
                        wordList[i] = wordListFricative[tmp];
                        usedInts.add(tmp);
                    }
                }
            }
        }

        word.setText(wordList[0]);
        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isRecording) {
                    recorder.stopRecording();
                    recordText.setText(getResources().getString(R.string.record));
                    imageView.setImageResource(R.drawable.play);
                    if(counter < 10) {
                        word.setText(wordList[counter]);
                        counter++;
                        text.setText(counter + " / 10");
                    } else {
                        Intent intent = new Intent(v.getContext(), GeneralRepetitionFinished.class);
                        intent.putExtra("exercise", "Word_List");
                        if(getIntent().getBooleanExtra("trainingset", false)) {
                            intent = new Intent(v.getContext(), TrainingsetExerciseFinished.class);
                            intent.putExtra("exerciseList", getIntent().getExtras().getStringArray("exerciseList"));
                            intent.putExtra("exerciseCounter", exerciseCounter);
                        }
                        record.setEnabled(false);
                        recorder.release();
                        v.getContext().startActivity(intent);
                    }
                    isRecording = false;
                } else {
                    recorder.record();
                    recordText.setText(R.string.recording);
                    imageView.setImageResource(R.drawable.pause);
                    isRecording = true;
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
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(!Objects.requireNonNull(getIntent().getExtras()).getBoolean("trainingset"))
            finish();
        return super.onOptionsItemSelected(item);
    }
}
