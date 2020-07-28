package com.example.cit_app.other_activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.cit_app.R;
import com.example.cit_app.exercises.MinimalPairs;
import com.example.cit_app.exercises.MinimalPairs2;
import com.example.cit_app.exercises.Picture_Description;
import com.example.cit_app.exercises.ReadingOfSentences;
import com.example.cit_app.exercises.SyllableRepetition;
import com.example.cit_app.exercises.Word_List;

public class Instruction extends AppCompatActivity {

    TextView title, description, instruction;
    Button start;
    Intent newActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_instruction);
        final Intent intent = getIntent();
        title = (TextView) findViewById(R.id.exerciseTitle);
        description = (TextView) findViewById(R.id.exerciseDescription);
        instruction = (TextView) findViewById(R.id.exerciseInstruction);
        start = (Button) findViewById(R.id.startExercise);
        title.setText(intent.getStringExtra("title"));
        description.setText(intent.getStringExtra("description"));
        instruction.setText(intent.getStringExtra("instruction"));
        String path = "android.resource://" + getPackageName() + "/";
        switch(intent.getStringExtra("title")) {
            case "Minimal pairs":
            case "Minimalpaare":
                newActivity = new Intent(this, MinimalPairs.class);
                newActivity.putExtra("trainingset", intent.getExtras().getBoolean("trainingset"));
                path = path + R.raw.minimalpairs_instruction;
                break;
            case "Minimal pairs 2":
            case "Minimalpaare 2":
                newActivity = new Intent(this, MinimalPairs2.class);
                newActivity.putExtra("trainingset", intent.getExtras().getBoolean("trainingset"));
                path = path + R.raw.minimalpairs2_instruction;
                break;
            case "Word list":
            case "Wortliste":
                newActivity = new Intent(this, Word_List.class);
                newActivity.putExtra("trainingset", intent.getExtras().getBoolean("trainingset"));
                path = path + R.raw.wordlist_instruction;
                break;
            case "Sentence reading":
            case "Sätze lesen":
                newActivity = new Intent(this, ReadingOfSentences.class);
                newActivity.putExtra("trainingset", intent.getExtras().getBoolean("trainingset"));
                path = path + R.raw.readingofsentences_instruction;
                break;
            case "Syllable repetition":
            case "Wortwiederholung":
                newActivity = new Intent(this, SyllableRepetition.class);
                newActivity.putExtra("trainingset", intent.getExtras().getBoolean("trainingset"));
                path = path + R.raw.syllablerepetition_instruction;
                break;
            case "Picture description":
            case "Bildbeschreibung":
                newActivity = new Intent(this, Picture_Description.class);
                newActivity.putExtra("trainingset", intent.getExtras().getBoolean("trainingset"));
                path = path + R.raw.picturedescription_instruction;
                break;
            default:
                newActivity = new Intent(this, MainActivity.class);
                break;
        }
        // Set Instruction Video
        final VideoView videoView = findViewById(R.id.videoView);
        videoView.setVideoURI(Uri.parse(path));
        Toast.makeText(this, "" + path, Toast.LENGTH_SHORT).show();
        MediaController mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);
        videoView.seekTo(1);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.getContext().startActivity(newActivity);
            }
        });
    }
}
