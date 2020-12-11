/**
 * Created by Christoph Popp
 */

package com.example.cit_app.other_activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.cit_app.ExerciseAdapter;
import com.example.cit_app.R;
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
    private CardView startTrainingset;
    Random rand = new Random();
    private String state = "";
    private int choose = 0;
    private boolean minimal_pairs, minimal_pairs_2, picture_description, reading_of_sentences, syllable_repetition, word_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_trainingset);
        getSupportActionBar().setTitle(getResources().getString(R.string.trainingsetTitle)); // for set actionbar title
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //initialize
        exerciseList = findViewById(R.id.trainingsetList);
        startTrainingset = (CardView) findViewById(R.id.startTraining);
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

        //Check if daily session has already been done
        if(!weekDay.equals(state)) {
            choose = rand.nextInt(2);
            editor.putInt("MEM2", choose);
            editor.apply();
            state = weekDay;
            editor.putString("MEM1", weekDay);
            editor.commit();
            exerciseList = findViewById(R.id.trainingsetList);
            int[] intelligebility_or_articulation_images = {R.drawable.hearing, R.drawable.speaking};
            exercise_list = new String[]{intonation_exercise[choose], hearing_exercise, speech_rate, intelligebility_or_articulation_exercise[choose]};
            List<String> list = new ArrayList<String>(Arrays.asList(exercise_list));
            editor1.putBoolean("Finished", false);
            editor1.apply();
            exercise_list = list.toArray(new String[0]);
            if(list.isEmpty()) {
                exercise_list = new String[]{getString(R.string.listEmpty)};
            }
            images = new int[]{R.drawable.speaking, R.drawable.hearing, R.drawable.speaking, intelligebility_or_articulation_images[choose]};
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
                int[] intelligebility_or_articulation_images = {R.drawable.hearing, R.drawable.speaking};
                if(!exercise.getBoolean("Finished", false))
                    exercise_list = new String[]{intonation_exercise[choose], hearing_exercise, speech_rate, intelligebility_or_articulation_exercise[choose]};
                else
                    exercise_list = new String[]{};
                List<String> list = new ArrayList<String>(Arrays.asList(exercise_list));
                exercise_list = list.toArray(new String[0]);
                if (list.isEmpty()) {
                    exercise_list = new String[]{getString(R.string.listEmpty)};
                }
                images = new int[]{R.drawable.speaking, R.drawable.hearing, R.drawable.speaking, intelligebility_or_articulation_images[choose]};
                ExerciseAdapter exAd = new ExerciseAdapter(this, exercise_list, images, this);
                exerciseList.setAdapter(exAd);
                exerciseList.setLayoutManager(new LinearLayoutManager(this));
            }
        }

        startTrainingset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                switch(exercise_list[0]) {
                    case "MinimalPairs":
                        intent = new Intent(v.getContext(), Instruction.class);
                        intent.putExtra("title", getResources().getString(R.string.MinimalPairs));
                        intent.putExtra("description", getResources().getString(R.string.DescriptionMinPairs));
                        intent.putExtra("instruction", getResources().getString(R.string.ExplanationMinPairs));
                        intent.putExtra("exerciseList", exercise_list);
                        intent.putExtra("exerciseCounter", 0);
                        intent.putExtra("trainingset", true);
                        break;
                    case "MinimalPairs2":
                        intent = new Intent(v.getContext(), Instruction.class);
                        intent.putExtra("title", getResources().getString(R.string.MinimalPairs2));
                        intent.putExtra("description", getResources().getString(R.string.DescriptionMinPairs2));
                        intent.putExtra("instruction", getResources().getString(R.string.ExplanationMinPairs2));
                        intent.putExtra("exerciseList", exercise_list);
                        intent.putExtra("exerciseCounter", 0);
                        intent.putExtra("trainingset", true);
                        break;
                    case "Word_List":
                        intent = new Intent(v.getContext(), Instruction.class);
                        intent.putExtra("title", getResources().getString(R.string.WordList));
                        intent.putExtra("description", getResources().getString(R.string.DescriptionWordList));
                        intent.putExtra("instruction", getResources().getString(R.string.ExplanationWordList));
                        intent.putExtra("exerciseList", exercise_list);
                        intent.putExtra("exerciseCounter", 0);
                        intent.putExtra("trainingset", true);
                        break;
                    case "ReadingOfSentences":
                        intent = new Intent(v.getContext(), Instruction.class);
                        intent.putExtra("title", getResources().getString(R.string.SentenceReading));
                        intent.putExtra("description", getResources().getString(R.string.DescriptionSentenceReading));
                        intent.putExtra("instruction", getResources().getString(R.string.ExplanationSentenceReading));
                        intent.putExtra("exerciseList", exercise_list);
                        intent.putExtra("exerciseCounter", 0);
                        intent.putExtra("trainingset", true);
                        break;
                    case "Picture_Description":
                        intent = new Intent(v.getContext(), Instruction.class);
                        intent.putExtra("title", getResources().getString(R.string.PictureDescription));
                        intent.putExtra("description", getResources().getString(R.string.DescriptionPictureDescription));
                        intent.putExtra("instruction", getResources().getString(R.string.ExplanationPictureDescription));
                        intent.putExtra("exerciseList", exercise_list);
                        intent.putExtra("exerciseCounter", 0);
                        intent.putExtra("trainingset", true);
                        break;
                    case "SyllableRepetition":
                        intent = new Intent(v.getContext(), Instruction.class);
                        intent.putExtra("title", getResources().getString(R.string.SyllableRepetition));
                        intent.putExtra("description", getResources().getString(R.string.DescriptionSyllableRepetition));
                        String s;
                        String s1;
                        String s2;
                        Random r = new Random();
                        int i = r.nextInt(7);
                        switch (i) {
                            case 0:
                                s = getResources().getString(R.string.pataka);
                                s1 = getResources().getString(R.string.ExamplePataka);
                                s2 = "Pataka";
                                break;
                            case 1:
                                s = getResources().getString(R.string.petaka);
                                s1 = getResources().getString(R.string.ExamplePetaka);
                                s2 = "Petaka";
                                break;
                            case 2:
                                s = getResources().getString(R.string.pakata);
                                s1 = getResources().getString(R.string.ExamplePakata);
                                s2 = "Pakata";
                                break;
                            case 3:
                                s = getResources().getString(R.string.pa);
                                s1 = getResources().getString(R.string.ExamplePa);
                                s2 = "Pa";
                                break;
                            case 4:
                                s = getResources().getString(R.string.ta);
                                s1 = getResources().getString(R.string.ExampleTa);
                                s2 = "Ta";
                                break;
                            case 5:
                                s = getResources().getString(R.string.ka);
                                s1 = getResources().getString(R.string.ExampleKa);
                                s2 = "Ka";
                                break;
                            case 6:
                                s = getResources().getString(R.string.sifaschu);
                                s1 = getResources().getString(R.string.ExampleSifaschu);
                                s2 = "Sifaschu";
                                break;
                            default:
                                s = "";
                                s1 = "";
                                s2 = "";
                                break;
                        }
                        intent.putExtra("word", s2);
                        intent.putExtra("instruction", getResources().getString(R.string.ExplanationSyllableRepetition1) + s + getResources().getString(R.string.ExplanationSyllableRepetition2) + s1 + getResources().getString(R.string.ExplanationSyllableRepetition3));
                        intent.putExtra("exerciseList", exercise_list);
                        intent.putExtra("exerciseCounter", 0);
                        intent.putExtra("trainingset", true);
                        break;
                    default:
                        intent = new Intent(v.getContext(), MainActivity.class);
                        break;
                }
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public void onExerciseClick(int position) {
    }

    //This allows you to return to the activity before
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

}
