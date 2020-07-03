package com.example.cit_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class Trainingset extends AppCompatActivity implements ExerciseAdapter.OnExerciseListener {

    private String exercise_list[];
    private int images[];
    private RecyclerView exerciseList;
    Random rand = new Random();
    private String state = "";
    private int choose = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainingset);
        exerciseList = findViewById(R.id.element_list);
        String[] intonation_exercise = {"Picture_Description", "ReadingOfSentences"};
        String hearing_exercise = "MinimalPairs2";
        String speech_rate = "SyllableRepetition";
        String[] intelligebility_or_articulation_exercise = {"Word_List", "MinimalPairs"};
        String weekDay;
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.GERMANY);
        Calendar calendar = Calendar.getInstance();
        weekDay = dayFormat.format(calendar.getTime());
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        SharedPreferences.Editor editor = pref.edit();
        state = pref.getString("MEM1", "");
        choose = pref.getInt("MEM2", -1);
        if(!weekDay.equals(state)) {
            choose = rand.nextInt(2);
            editor.putInt("MEM2", choose);
            editor.apply();
            Toast.makeText(this, "went in first", Toast.LENGTH_SHORT).show();
            state = weekDay;
            Toast.makeText(this, state, Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "" + choose, Toast.LENGTH_SHORT).show();
            editor.putString("MEM1", weekDay);
            editor.commit();
            Toast.makeText(this, "" + choose, Toast.LENGTH_SHORT).show();
            exerciseList = findViewById(R.id.trainingsetList);
            int[] intelligebility_or_articulation_images = {R.drawable.hearing, R.drawable.speech_results};
            exercise_list = new String[]{intonation_exercise[choose], hearing_exercise, speech_rate, intelligebility_or_articulation_exercise[choose]};
            List<String> list = new ArrayList<String>(Arrays.asList(exercise_list));
            Word_List.exerciseFinished = false;
            SyllableRepetition.exerciseFinished = false;
            ReadingOfSentences.exerciseFinished = false;
            Picture_Description.exerciseFinished = false;
            MinimalPairs.exerciseFinished = false;
            MinimalPairs2.exerciseFinished = false;
            exercise_list = list.toArray(new String[0]);
            if(list.isEmpty()) {
                exercise_list = new String[]{getString(R.string.listEmpty)};
            }
            images = new int[]{R.drawable.speech_results, R.drawable.hearing, R.drawable.speech_results, intelligebility_or_articulation_images[choose]};
            ExerciseAdapter exAd = new ExerciseAdapter(this, exercise_list, images, this);
            exerciseList.setAdapter(exAd);
            exerciseList.setLayoutManager(new LinearLayoutManager(this));
            editor.putString("key_name", "string value");
            editor.commit();
        } else {
            if(choose == -1) {
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, state + "went in this thing", Toast.LENGTH_SHORT).show();
                exerciseList = findViewById(R.id.trainingsetList);
                int[] intelligebility_or_articulation_images = {R.drawable.hearing, R.drawable.speech_results};
                Toast.makeText(this, "" + Word_List.exerciseFinished, Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "" + choose, Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "" + SyllableRepetition.exerciseFinished, Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "" + ReadingOfSentences.exerciseFinished, Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "" + Picture_Description.exerciseFinished, Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "" + MinimalPairs.exerciseFinished, Toast.LENGTH_SHORT).show();
                exercise_list = new String[]{intonation_exercise[choose], hearing_exercise, speech_rate, intelligebility_or_articulation_exercise[choose]};
                List<String> list = new ArrayList<String>(Arrays.asList(exercise_list));
                for (int i = 0; i < exercise_list.length; i++) {
                    switch (exercise_list[i]) {
                        case "Word_List":
                            if (Word_List.exerciseFinished) {
                                list.remove(exercise_list[i]);
                            }
                            break;
                        case "SyllableRepetition":
                            if (SyllableRepetition.exerciseFinished) {
                                list.remove(exercise_list[i]);
                            }
                            break;
                        case "ReadingOfSentences":
                            if (ReadingOfSentences.exerciseFinished) {
                                list.remove(exercise_list[i]);
                            }
                            break;
                        case "Picture_Description":
                            if (Picture_Description.exerciseFinished) {
                                list.remove(exercise_list[i]);
                            }
                            break;
                        case "MinimalPairs2":
                            if (MinimalPairs2.exerciseFinished) {
                                list.remove(exercise_list[i]);
                            }
                            break;
                        case "MinimalPairs":
                            if (MinimalPairs.exerciseFinished) {
                                list.remove(exercise_list[i]);
                            }
                            break;
                        default:
                            break;
                    }
                }
                Toast.makeText(this, "" + list, Toast.LENGTH_SHORT).show();
                exercise_list = list.toArray(new String[0]);
                if (list.isEmpty()) {
                    exercise_list = new String[]{getString(R.string.listEmpty)};
                }
                images = new int[]{R.drawable.speech_results, R.drawable.hearing, R.drawable.speech_results, intelligebility_or_articulation_images[choose]};
                ExerciseAdapter exAd = new ExerciseAdapter(this, exercise_list, images, this);
                exerciseList.setAdapter(exAd);
                exerciseList.setLayoutManager(new LinearLayoutManager(this));
            }
        }
    }

    @Override
    public void onExerciseClick(int position) {
        Intent intent;
        switch(exercise_list[position]) {
            case "MinimalPairs":
                intent = new Intent(this, Instruction.class);
                intent.putExtra("title", getResources().getString(R.string.MinimalPairs));
                //TODO Think of some useful descriptions and instructions
                intent.putExtra("description", getResources().getString(R.string.DescriptionMinPairs));
                intent.putExtra("instruction", getResources().getString(R.string.ExplanationMinPairs));
                intent.putExtra("trainingset", true);
                break;
            case "MinimalPairs2":
                intent = new Intent(this, Instruction.class);
                intent.putExtra("title", getResources().getString(R.string.MinimalPairs2));
                //TODO Think of some useful descriptions and instructions
                intent.putExtra("description", getResources().getString(R.string.DescriptionMinPairs2));
                intent.putExtra("instruction", getResources().getString(R.string.ExplanationMinPairs2));
                intent.putExtra("trainingset", true);
                break;
            case "Word_List":
                intent = new Intent(this, Instruction.class);
                intent.putExtra("title", getResources().getString(R.string.WordList));
                //TODO Think of some useful descriptions and instructions
                intent.putExtra("description", getResources().getString(R.string.DescriptionWordList));
                intent.putExtra("instruction", getResources().getString(R.string.ExplanationWordList));
                intent.putExtra("trainingset", true);
                break;
            case "ReadingOfSentences":
                intent = new Intent(this, Instruction.class);
                intent.putExtra("title", getResources().getString(R.string.SentenceReading));
                //TODO Think of some useful descriptions and instructions
                intent.putExtra("description", getResources().getString(R.string.DescriptionSentenceReading));
                intent.putExtra("instruction", getResources().getString(R.string.ExplanationSentenceReading));
                intent.putExtra("trainingset", true);
                break;
            case "SyllableRepetition":
                intent = new Intent(this, Instruction.class);
                intent.putExtra("title", getResources().getString(R.string.SyllableRepetition));
                //TODO Think of some useful descriptions and instructions
                intent.putExtra("description", getResources().getString(R.string.DescriptionSyllableRepetition));
                intent.putExtra("instruction", getResources().getString(R.string.ExplanationSyllableRepetition));
                intent.putExtra("trainingset", true);
                break;
            case "Picture_Description":
                intent = new Intent(this, Instruction.class);
                intent.putExtra("title", getResources().getString(R.string.PictureDescription));
                //TODO Think of some useful descriptions and instructions
                intent.putExtra("description", getResources().getString(R.string.DescriptionPictureDescription));
                intent.putExtra("instruction", getResources().getString(R.string.ExplanationPictureDescription));
                intent.putExtra("trainingset", true);
                break;
            default:
                intent = new Intent(this, MainActivity.class);
                break;
        }
        this.startActivity(intent);
    }
}
