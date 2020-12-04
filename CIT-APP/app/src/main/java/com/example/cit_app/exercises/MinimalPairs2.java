package com.example.cit_app.exercises;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.cit_app.data_access.CSVFileWriter;
import com.example.cit_app.data_access.FeatureDataService;
import com.example.cit_app.other_activities.Instruction;
import com.example.cit_app.other_activities.MainActivity;
import com.example.cit_app.other_activities.MinimalPairsExerciseFinished;
import com.example.cit_app.R;
import com.example.cit_app.other_activities.TrainingsetExerciseFinished;
import com.example.cit_app.other_activities.TrainingsetFinished;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;
import java.util.Random;

public class MinimalPairs2 extends AppCompatActivity {

    private Random random = new Random();
    private TextView text;
    private String[] minimal_pairs = new String[80];
    private int exerciseCounter;
    private int position = 0;
    private int counter = 0;
    private int oldPos = 0;
    private int choose = 0;
    private int[] images;
    private boolean listenPressed = false;
    private MediaPlayer player;
    private String[] minimal_pairs_result;
    private String[] minimal_pairs_correct;
    private Button topTop;
    private Button botBot;
    private Button topCenter;
    private Button botCenter;
    private Dialog dialog;
    private FeatureDataService featureDataService;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minimal_pairs2);
        Objects.requireNonNull(getSupportActionBar()).setTitle(getResources().getString(R.string.MinimalPairs2)); // for set actionbar title
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //Initialize
        dialog = new Dialog(this);
        Button start = findViewById(R.id.startButton);
        topTop = findViewById(R.id.topTop);
        botBot = findViewById(R.id.topCenter);
        topCenter = findViewById(R.id.botCenter);
        botCenter = findViewById(R.id.botBot);
        text = findViewById(R.id.wordNumber2);
        images = new int[20];
        featureDataService = new FeatureDataService(this);
        if(getIntent().getExtras() != null)
            exerciseCounter = getIntent().getExtras().getInt("exerciseCounter", 0);
        ArrayList<Integer> usedNumber = new ArrayList<>();
        String[] minimal_pairs_fricatives = getResources().getStringArray(R.array.Minimal_Pairs_Fricatives);
        String[] minimal_pairs_stops = getResources().getStringArray(R.array.Minimal_Pairs_Stops);
        String[] minimal_pairs_general = getResources().getStringArray(R.array.Minimal_Pairs);
        minimal_pairs_result = new String[20];
        minimal_pairs_correct = new String[20];
        text.setText((counter + 1) + " / 20");
        //fill minimal_pairs list with 20 pairs consisting of 10 random 5 fricative and 5 stop pairs
        for(int i = 0; i < 20; i++) {
            position = 4 * random.nextInt(50);
            if(i < 10) {
                while(usedNumber.contains(position)) {
                    position = 4 * random.nextInt(50);
                }
                int corr = random.nextInt(4);
                minimal_pairs_correct[i] = minimal_pairs_general[position + corr];
                minimal_pairs[i * 4] = minimal_pairs_general[position];
                minimal_pairs[i * 4 + 1] = minimal_pairs_general[position + 1];
                minimal_pairs[i * 4 + 2] = minimal_pairs_general[position + 2];
                minimal_pairs[i * 4 + 3] = minimal_pairs_general[position + 3];

                usedNumber.add(position);
            } else {
                if(i < 15) {
                    if(i == 10) {
                        usedNumber.clear();
                    }
                    position = 4 * random.nextInt(12);
                    while(usedNumber.contains(position)) {
                        position = 4 * random.nextInt(12);
                    }
                    int corr = random.nextInt(4);
                    minimal_pairs_correct[i] = minimal_pairs_fricatives[position + corr];
                    minimal_pairs[i * 4] = minimal_pairs_fricatives[position];
                    minimal_pairs[i * 4 + 1] = minimal_pairs_fricatives[position + 1];
                    minimal_pairs[i * 4 + 2] = minimal_pairs_fricatives[position + 2];
                    minimal_pairs[i * 4 + 3] = minimal_pairs_fricatives[position + 3];
                    usedNumber.add(position);
                } else {
                    if(i == 15) {
                        usedNumber.clear();
                    }
                    position = 4 * random.nextInt(7);
                    while(usedNumber.contains(position)) {
                        position = 4 * random.nextInt(7);
                    }
                    int corr = random.nextInt(4);
                    minimal_pairs_correct[i] = minimal_pairs_stops[position + corr];
                    minimal_pairs[i * 4] = minimal_pairs_stops[position];
                    minimal_pairs[i * 4 + 1] = minimal_pairs_stops[position + 1];
                    minimal_pairs[i * 4 + 2] = minimal_pairs_stops[position + 2];
                    minimal_pairs[i * 4 + 3] = minimal_pairs_stops[position + 3];
                    usedNumber.add(position);
                }
            }
        }
        //set text and action of the buttons
        position = random.nextInt(3);
        topTop.setText(minimal_pairs[position % 4]);
        topCenter.setText(minimal_pairs[(position + 1) % 4]);
        botCenter.setText(minimal_pairs[(position + 2) % 4]);
        botBot.setText(minimal_pairs[(position + 3) % 4]);
        oldPos += 4;
        //First Button
        topTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listenPressed) {
                    if (oldPos >= 80) {
                        minimal_pairs_result[counter] = topTop.getText().toString();
                        Intent intent = new Intent(MinimalPairs2.this, MinimalPairsExerciseFinished.class);
                        int correct = 0;
                        for (int i = 0; i < 20; i++) {
                            minimal_pairs_result[i] = minimal_pairs_result[i].replace("ä", "ae");
                            minimal_pairs_result[i] = minimal_pairs_result[i].replace("ö", "oe");
                            minimal_pairs_result[i] = minimal_pairs_result[i].replace("ü", "ue");
                            minimal_pairs_result[i] = minimal_pairs_result[i].replace("ß", "ss");
                            if (minimal_pairs_result[i].equals(minimal_pairs_correct[i])) {
                                correct++;
                                images[i] = R.drawable.right;
                            } else {
                                images[i] = R.drawable.wrong;
                            }
                        }
                        intent.putExtra("correct", correct);
                        intent.putExtra("images", images);
                        intent.putExtra("cw", minimal_pairs_correct);
                        intent.putExtra("results", minimal_pairs_result);
                        float int_f0 = (float) ((double) correct / (double) 20);
                        if (getIntent().getBooleanExtra("trainingset", false)) {
                            try {
                                export_data();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            featureDataService.save_feature(featureDataService.hearing_name, Calendar.getInstance().getTime(), int_f0);
                            intent = new Intent(v.getContext(), TrainingsetExerciseFinished.class);
                            intent.putExtra("exerciseList", getIntent().getExtras().getStringArray("exerciseList"));
                            intent.putExtra("exerciseCounter", exerciseCounter);
                        }
                        v.getContext().startActivity(intent);
                    } else {
                        listenPressed = false;
                        position += random.nextInt(3);
                        minimal_pairs_result[counter] = topTop.getText().toString();
                        counter++;
                        topTop.setText(minimal_pairs[oldPos + (position % 4)]);
                        topCenter.setText(minimal_pairs[oldPos + ((position + 1) % 4)]);
                        botCenter.setText(minimal_pairs[oldPos + ((position + 2) % 4)]);
                        botBot.setText(minimal_pairs[oldPos + ((position + 3) % 4)]);
                        text.setText((counter + 1) + " / 20");
                        oldPos += 4;
                        choose += 1;
                        if (player != null) {
                            player.stop();
                            player.release();
                            player = null;
                        }
                    }
                } else {
                    showPopUp(v);
                }
            }
        });
        
        //Second Button
        topCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listenPressed) {
                    if (oldPos >= 80) {
                        minimal_pairs_result[counter] = topCenter.getText().toString();
                        Intent intent = new Intent(MinimalPairs2.this, MinimalPairsExerciseFinished.class);
                        int correct = 0;
                        for (int i = 0; i < 20; i++) {
                            minimal_pairs_result[i] = minimal_pairs_result[i].replace("ä", "ae");
                            minimal_pairs_result[i] = minimal_pairs_result[i].replace("ö", "oe");
                            minimal_pairs_result[i] = minimal_pairs_result[i].replace("ü", "ue");
                            minimal_pairs_result[i] = minimal_pairs_result[i].replace("ß", "ss");
                            if (minimal_pairs_result[i].equals(minimal_pairs_correct[i])) {
                                correct++;
                                images[i] = R.drawable.right;
                            } else {
                                images[i] = R.drawable.wrong;
                            }
                        }
                        intent.putExtra("correct", correct);
                        intent.putExtra("images", images);
                        intent.putExtra("cw", minimal_pairs_correct);
                        intent.putExtra("results", minimal_pairs_result);
                        float int_f0 = (float) ((double) correct / (double) 20);
                        if (getIntent().getBooleanExtra("trainingset", false)) {
                            try {
                                export_data();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            featureDataService.save_feature(featureDataService.hearing_name, Calendar.getInstance().getTime(), int_f0);
                            intent = new Intent(v.getContext(), TrainingsetExerciseFinished.class);
                            intent.putExtra("exerciseList", getIntent().getExtras().getStringArray("exerciseList"));
                            intent.putExtra("exerciseCounter", exerciseCounter);
                        }
                        v.getContext().startActivity(intent);
                    } else {
                        listenPressed = false;
                        position = random.nextInt(3);
                        minimal_pairs_result[counter] = topCenter.getText().toString();
                        counter++;
                        topTop.setText(minimal_pairs[oldPos + (position % 4)]);
                        topCenter.setText(minimal_pairs[oldPos + ((position + 1) % 4)]);
                        botCenter.setText(minimal_pairs[oldPos + ((position + 2) % 4)]);
                        botBot.setText(minimal_pairs[oldPos + ((position + 3) % 4)]);
                        text.setText((counter + 1) + " / 20");
                        oldPos += 4;
                        choose += 1;
                        if (player != null) {
                            player.stop();
                            player.release();
                            player = null;
                        }
                    }
                } else {
                    showPopUp(v);
                }
            }
        });

        //Third Button
        botCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listenPressed) {
                    if (oldPos >= 80) {
                        minimal_pairs_result[counter] = botCenter.getText().toString();
                        Intent intent = new Intent(MinimalPairs2.this, MinimalPairsExerciseFinished.class);
                        int correct = 0;
                        for (int i = 0; i < 20; i++) {
                            minimal_pairs_result[i] = minimal_pairs_result[i].replace("ä", "ae");
                            minimal_pairs_result[i] = minimal_pairs_result[i].replace("ö", "oe");
                            minimal_pairs_result[i] = minimal_pairs_result[i].replace("ü", "ue");
                            minimal_pairs_result[i] = minimal_pairs_result[i].replace("ß", "ss");
                            if (minimal_pairs_result[i].equals(minimal_pairs_correct[i])) {
                                correct++;
                                images[i] = R.drawable.right;
                            } else {
                                images[i] = R.drawable.wrong;
                            }
                        }
                        intent.putExtra("correct", correct);
                        intent.putExtra("images", images);
                        intent.putExtra("cw", minimal_pairs_correct);
                        intent.putExtra("results", minimal_pairs_result);
                        float int_f0 = (float) ((double) correct / (double) 20);
                        if (getIntent().getBooleanExtra("trainingset", false)) {
                            try {
                                export_data();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            featureDataService.save_feature(featureDataService.hearing_name, Calendar.getInstance().getTime(), int_f0);
                            intent = new Intent(v.getContext(), TrainingsetExerciseFinished.class);
                            intent.putExtra("exerciseList", getIntent().getExtras().getStringArray("exerciseList"));
                            intent.putExtra("exerciseCounter", exerciseCounter);
                        }
                        v.getContext().startActivity(intent);
                    } else {
                        listenPressed = false;
                        position = random.nextInt(3);
                        minimal_pairs_result[counter] = botCenter.getText().toString();
                        counter++;
                        topTop.setText(minimal_pairs[oldPos + (position % 4)]);
                        topCenter.setText(minimal_pairs[oldPos + ((position + 1) % 4)]);
                        botCenter.setText(minimal_pairs[oldPos + ((position + 2) % 4)]);
                        botBot.setText(minimal_pairs[oldPos + ((position + 3) % 4)]);
                        text.setText((counter + 1) + " / 20");
                        oldPos += 4;
                        choose += 1;
                        if (player != null) {
                            player.stop();
                            player.release();
                            player = null;
                        }
                    }
                } else {
                    showPopUp(v);
                }
            }
        });

        //Fourth Button
        botBot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listenPressed) {
                    if (oldPos >= 80) {
                        minimal_pairs_result[counter] = botBot.getText().toString();
                        Intent intent = new Intent(MinimalPairs2.this, MinimalPairsExerciseFinished.class);
                        int correct = 0;
                        for (int i = 0; i < 20; i++) {
                            minimal_pairs_result[i] = minimal_pairs_result[i].replace("ä", "ae");
                            minimal_pairs_result[i] = minimal_pairs_result[i].replace("ö", "oe");
                            minimal_pairs_result[i] = minimal_pairs_result[i].replace("ü", "ue");
                            minimal_pairs_result[i] = minimal_pairs_result[i].replace("ß", "ss");
                            if (minimal_pairs_result[i].equals(minimal_pairs_correct[i])) {
                                correct++;
                                images[i] = R.drawable.right;
                            } else {
                                images[i] = R.drawable.wrong;
                            }
                        }
                        intent.putExtra("correct", correct);
                        intent.putExtra("images", images);
                        intent.putExtra("cw", minimal_pairs_correct);
                        intent.putExtra("results", minimal_pairs_result);
                        float int_f0 = (float) ((double) correct / (double) 20);
                        if (getIntent().getBooleanExtra("trainingset", false)) {
                            try {
                                export_data();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            featureDataService.save_feature(featureDataService.hearing_name, Calendar.getInstance().getTime(), int_f0);
                            intent = new Intent(v.getContext(), TrainingsetExerciseFinished.class);
                            intent.putExtra("exerciseList", getIntent().getExtras().getStringArray("exerciseList"));
                            intent.putExtra("exerciseCounter", exerciseCounter);
                        }
                        v.getContext().startActivity(intent);
                    } else {
                        position = random.nextInt(3);
                        minimal_pairs_result[counter] = botBot.getText().toString();
                        counter++;
                        topTop.setText(minimal_pairs[oldPos + (position % 4)]);
                        topCenter.setText(minimal_pairs[oldPos + ((position + 1) % 4)]);
                        botCenter.setText(minimal_pairs[oldPos + ((position + 2) % 4)]);
                        botBot.setText(minimal_pairs[oldPos + ((position + 3) % 4)]);
                        text.setText((counter + 1) + " / 20");
                        oldPos += 4;
                        choose += 1;
                        listenPressed = false;
                        if (player != null) {
                            player.stop();
                            player.release();
                            player = null;
                        }
                    }
                } else {
                    showPopUp(v);
                }
            }
        });

        //Listen Button
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listenPressed = true;
                if(player != null) {
                    player.seekTo(0);
                    player.start();
                } else {
                    minimal_pairs_correct[choose] = minimal_pairs_correct[choose].replace("ä", "ae");
                    minimal_pairs_correct[choose] = minimal_pairs_correct[choose].replace("ö", "oe");
                    minimal_pairs_correct[choose] = minimal_pairs_correct[choose].replace("ü", "ue");
                    minimal_pairs_correct[choose] = minimal_pairs_correct[choose].replace("ß", "ss");
                    String file = minimal_pairs_correct[choose].toLowerCase();
                    int resId = getResources().getIdentifier(file, "raw", getPackageName());
                    String path = "a" + resId;
                    String path2 = path.substring(1);
                    player = MediaPlayer.create(v.getContext(), Integer.parseInt(path2));
                    player.start();
                }
            }
        });
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

    private void export_data()  throws IOException {
        String PATH = Environment.getExternalStorageDirectory() + "/CITA/METADATA/RESULTS/";
        CSVFileWriter mCSVFileWriter = new CSVFileWriter("MinimalPairs2", PATH);
        String[] start = {"correct_word", "chosen_word"};
        mCSVFileWriter.write(start);
        for(int i = 0; i < minimal_pairs_correct.length; i++) {
            String[] correct_result = {minimal_pairs_correct[i], minimal_pairs_result[i]};
            mCSVFileWriter.write(correct_result);
        }
        mCSVFileWriter.close();

    }
}
