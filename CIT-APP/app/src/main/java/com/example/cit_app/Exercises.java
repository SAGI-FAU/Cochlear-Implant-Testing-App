package com.example.cit_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.cit_app.other_activities.Instruction;
import com.example.cit_app.other_activities.MainActivity;

//This class is the view showing the exercise list
public class Exercises extends AppCompatActivity implements ExerciseAdapter.OnExerciseListener {

    private String s1[];
    private int images[];
    private RecyclerView exerciseList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercises);
        s1 = getResources().getStringArray(R.array.Exercises);
        images = new int[]{R.drawable.hearing, R.drawable.hearing, R.drawable.speech_results, R.drawable.speech_results, R.drawable.speech_results, R.drawable.speech_results};
        exerciseList = findViewById(R.id.element_list);
        ExerciseAdapter exAd = new ExerciseAdapter(this, s1, images, this);
        exerciseList.setAdapter(exAd);
        exerciseList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onExerciseClick(int position) {
        Intent intent;
        switch(s1[position]) {
            case "Minimal pairs":
            case "Minimalpaare":
                intent = new Intent(this, Instruction.class);
                intent.putExtra("title", getResources().getString(R.string.MinimalPairs));
                //TODO Think of some useful descriptions and instructions
                intent.putExtra("description", getResources().getString(R.string.DescriptionMinPairs));
                intent.putExtra("instruction", getResources().getString(R.string.ExplanationMinPairs));
                intent.putExtra("trainingset", false);
                //intent.putExtra("instructionPath", R.raw.minimalpairs_instruction);
                break;
            case "Minimal pairs 2":
            case "Minimalpaare2":
                intent = new Intent(this, Instruction.class);
                intent.putExtra("title", getResources().getString(R.string.MinimalPairs2));
                //TODO Think of some useful descriptions and instructions
                intent.putExtra("description", getResources().getString(R.string.DescriptionMinPairs2));
                intent.putExtra("instruction", getResources().getString(R.string.ExplanationMinPairs2));
                intent.putExtra("trainingset", false);
                //putExtra("instructionPath", R.raw.minimalpairs2_instruction);
                break;
            case "Word list":
            case "Wortliste":
                intent = new Intent(this, Instruction.class);
                intent.putExtra("title", getResources().getString(R.string.WordList));
                //TODO Think of some useful descriptions and instructions
                intent.putExtra("description", getResources().getString(R.string.DescriptionWordList));
                intent.putExtra("instruction", getResources().getString(R.string.ExplanationWordList));
                intent.putExtra("trainingset", false);
                //intent.putExtra("instructionPath", R.raw.wordlist_instruction);
                break;
            case "Sentence reading":
            case "Sätze lesen":
                intent = new Intent(this, Instruction.class);
                intent.putExtra("title", getResources().getString(R.string.SentenceReading));
                //TODO Think of some useful descriptions and instructions
                intent.putExtra("description", getResources().getString(R.string.DescriptionSentenceReading));
                intent.putExtra("instruction", getResources().getString(R.string.ExplanationSentenceReading));
                intent.putExtra("trainingset", false);
                //intent.putExtra("instructionPath", R.raw.readingofsentences_instruction);
                break;
            case "Syllable repetition":
            case "Silben wiederholen":
                intent = new Intent(this, Instruction.class);
                intent.putExtra("title", getResources().getString(R.string.SyllableRepetition));
                //TODO Think of some useful descriptions and instructions
                intent.putExtra("description", getResources().getString(R.string.DescriptionSyllableRepetition));
                intent.putExtra("instruction", getResources().getString(R.string.ExplanationSyllableRepetition));
                intent.putExtra("trainingset", false);
                //intent.putExtra("instructionPath", R.raw.syllablerepetition_instruction);
                break;
            case "Picture description":
            case "Bildbeschreibung":
                intent = new Intent(this, Instruction.class);
                intent.putExtra("title", getResources().getString(R.string.PictureDescription));
                //TODO Think of some useful descriptions and instructions
                intent.putExtra("description", getResources().getString(R.string.DescriptionPictureDescription));
                intent.putExtra("instruction", getResources().getString(R.string.ExplanationPictureDescription));
                intent.putExtra("trainingset", false);
                intent.putExtra("lololol", R.raw.minimalpairs2_instruction);
                break;
            default:
                intent = new Intent(this, MainActivity.class);
                break;
        }
        this.startActivity(intent);
    }
}
