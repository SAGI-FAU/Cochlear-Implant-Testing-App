package com.example.cit_app.exercises;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
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

public class MinimalPairs extends AppCompatActivity {

    private int counter = 1;
    private SpeechRecorder recorder;
    private Dialog dialog;
    private Random random = new Random();
    private  MediaPlayer player;
    private ArrayList<Integer> usedNumber = new ArrayList<>();
    private boolean isRecording;
    private ImageView imageView;
    private String[] minimal_pairs = new String[20];
    private TextView text, recordText;
    private boolean listenPressed = false;
    private int exerciseCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minimal_pairs);
        Objects.requireNonNull(getSupportActionBar()).setTitle(getResources().getString(R.string.MinimalPairs)); // for set actionbar title
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //Initialize
        CardView record = findViewById(R.id.record);
        CardView listen = findViewById(R.id.listen);
        recordText = findViewById(R.id.recordText);
        imageView = findViewById(R.id.recordImage);
        text = findViewById(R.id.wordNumber);
        if(getIntent().getExtras() != null)
            exerciseCounter = getIntent().getExtras().getInt("exerciseCounter", 0);
        dialog = new Dialog(this);
        imageView.setImageResource(R.drawable.play);
        isRecording = false;
        boolean isPlaying = false;
        recorder = SpeechRecorder.getInstance(this, new MinimalPairs.VolumeHandler(), "MinimalPairs");
        text.setText(counter + " / 20");
        String[] minimal_pairs_fricatives = getResources().getStringArray(R.array.Minimal_Pairs_Fricatives);
        String[] minimal_pairs_stops = getResources().getStringArray(R.array.Minimal_Pairs_Stops);
        String[] minimal_pairs_general = getResources().getStringArray(R.array.Minimal_Pairs);

        //fill minimal_pairs list with 20 pairs consisting of 1 random 2 fricative and 2 stop pairs
        for(int i = 0; i < 20; i+=4) {
            int position = 4 * random.nextInt(50);
            if(i < 1) {
                while(usedNumber.contains(position)) {
                    position = 4 * random.nextInt(50);
                }
                minimal_pairs[i] = minimal_pairs_general[position];
                minimal_pairs[i + 1] = minimal_pairs_general[position + 1];
                minimal_pairs[i + 2] = minimal_pairs_general[position + 2];
                minimal_pairs[i + 3] = minimal_pairs_general[position + 3];
            } else {
                if (i < 9) {
                    if (i == 4) {
                        usedNumber.clear();
                    }
                    position = 4 * random.nextInt(12);
                    while (usedNumber.contains(position)) {
                        position = 4 * random.nextInt(12);
                    }
                    minimal_pairs[i] = minimal_pairs_fricatives[position];
                    minimal_pairs[i + 1] = minimal_pairs_fricatives[position + 1];
                    minimal_pairs[i + 2] = minimal_pairs_fricatives[position + 2];
                    minimal_pairs[i + 3] = minimal_pairs_fricatives[position + 3];
                } else {
                    if (i == 12) {
                        usedNumber.clear();
                    }
                    position = 4 * random.nextInt(7);
                    while (usedNumber.contains(position)) {
                        position = 4 * random.nextInt(7);
                    }
                    minimal_pairs[i] = minimal_pairs_stops[position];
                    minimal_pairs[i + 1] = minimal_pairs_stops[position + 1];
                    minimal_pairs[i + 2] = minimal_pairs_stops[position + 2];
                    minimal_pairs[i + 3] = minimal_pairs_stops[position + 3];
                }
            }
            usedNumber.add(position);
        }
        //Listen Button
        listen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listenPressed = true;
                if(!isRecording) {
                    if(player != null) {
                        player.seekTo(0);
                        player.start();
                    } else {
                        if(counter <= 21){
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

        //Record Button
        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listenPressed) {
                    if (isRecording) {
                        if (counter > 19) {
                            isRecording = false;
                            recordText.setText(getResources().getString(R.string.record));
                            Intent intent = new Intent(v.getContext(), GeneralRepetitionFinished.class);
                            intent.putExtra("exercise", "MinimalPairs");
                            if (getIntent().getBooleanExtra("trainingset", false)) {
                                intent = new Intent(v.getContext(), TrainingsetExerciseFinished.class);
                                intent.putExtra("exerciseList", getIntent().getExtras().getStringArray("exerciseList"));
                                intent.putExtra("exerciseCounter", exerciseCounter);
                            }
                            recorder.stopRecording();
                            record.setEnabled(false);
                            recorder.release();
                            v.getContext().startActivity(intent);
                        } else {
                            recorder.stopRecording();
                            isRecording = false;
                            listenPressed = false;
                            imageView.setImageResource(R.drawable.play);
                            recordText.setText(getResources().getString(R.string.record));
                            counter++;
                            text.setText(counter + " / 20");
                            if (player != null) {
                                player.stop();
                                player.release();
                                player = null;
                            }
                        }
                    } else {
                        if (player != null)
                            player.stop();
                        recorder.prepare(getResources().getString(R.string.MinimalPairs));
                        recorder.record();
                        imageView.setImageResource(R.drawable.pause);
                        isRecording = true;
                        recordText.setText(R.string.recording);
                    }
                } else {
                    showPopUp(v);
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
            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

    private void showPopUp(View v) {

        dialog.setContentView(R.layout.popup_results_explanation);
        TextView text = dialog.findViewById(R.id.close);
        TextView textMessage = dialog.findViewById(R.id.resultsMessage);
        text.setOnClickListener(v1 -> dialog.dismiss());
        textMessage.setText(getResources().getString(R.string.listenNotPressed));
        dialog.show();
    }
}
