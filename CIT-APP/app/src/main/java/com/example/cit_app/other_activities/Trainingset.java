package com.example.cit_app.other_activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.example.cit_app.ExerciseAdapter;
import com.example.cit_app.R;
import com.example.cit_app.exercises.MinimalPairs;
import com.example.cit_app.exercises.MinimalPairs2;
import com.example.cit_app.exercises.Picture_Description;
import com.example.cit_app.exercises.ReadingOfSentences;
import com.example.cit_app.exercises.SyllableRepetition;
import com.example.cit_app.exercises.Word_List;
import com.example.cit_app.other_activities.Instruction;
import com.example.cit_app.other_activities.MainActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
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
    private boolean minimal_pairs, minimal_pairs_2, picture_description, reading_of_sentences, syllable_repetition, word_list;

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
        SharedPreferences exercise = getApplicationContext().getSharedPreferences("ExerciseFinished", 0);
        SharedPreferences.Editor editor1 = exercise.edit();
        minimal_pairs = exercise.getBoolean("MinimalPairs", false);
        minimal_pairs_2 = exercise.getBoolean("MinimalPairs2", false);
        syllable_repetition = exercise.getBoolean("SyllableRepetition", false);
        picture_description = exercise.getBoolean("Picture_Description", false);
        word_list = exercise.getBoolean("Word_List", false);
        reading_of_sentences = exercise.getBoolean("ReadingOfSentences", false);
        if(!weekDay.equals(state)) {
            choose = rand.nextInt(2);
            editor.putInt("MEM2", choose);
            editor.apply();
            state = weekDay;
            editor.putString("MEM1", weekDay);
            editor.commit();
            exerciseList = findViewById(R.id.trainingsetList);
            int[] intelligebility_or_articulation_images = {R.drawable.hearing, R.drawable.speech_results};
            exercise_list = new String[]{intonation_exercise[choose], hearing_exercise, speech_rate, intelligebility_or_articulation_exercise[choose]};
            List<String> list = new ArrayList<String>(Arrays.asList(exercise_list));
            editor1.putBoolean("Word_List", false);
            editor1.apply();
            editor1.putBoolean("SyllableRepetition", false);
            editor1.apply();
            editor1.putBoolean("ReadingOfSentences", false);
            editor1.apply();
            editor1.putBoolean("Picture_Description", false);
            editor1.apply();
            editor1.putBoolean("MinimalPairs", false);
            editor1.apply();
            editor1.putBoolean("MinimalPairs2", false);
            editor1.apply();
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
                exerciseList = findViewById(R.id.trainingsetList);
                int[] intelligebility_or_articulation_images = {R.drawable.hearing, R.drawable.speech_results};
                exercise_list = new String[]{intonation_exercise[choose], hearing_exercise, speech_rate, intelligebility_or_articulation_exercise[choose]};
                List<String> list = new ArrayList<String>(Arrays.asList(exercise_list));
                for (int i = 0; i < exercise_list.length; i++) {
                    switch (exercise_list[i]) {
                        case "Word_List":
                            if (word_list) {
                                list.remove(exercise_list[i]);
                            }
                            break;
                        case "SyllableRepetition":
                            if (syllable_repetition) {
                                list.remove(exercise_list[i]);
                            }
                            break;
                        case "ReadingOfSentences":
                            if (reading_of_sentences) {
                                list.remove(exercise_list[i]);
                            }
                            break;
                        case "Picture_Description":
                            if (picture_description) {
                                list.remove(exercise_list[i]);
                            }
                            break;
                        case "MinimalPairs2":
                            if (minimal_pairs_2) {
                                list.remove(exercise_list[i]);
                            }
                            break;
                        case "MinimalPairs":
                            if (minimal_pairs) {
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