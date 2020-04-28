package com.example.cit_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

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
                intent = new Intent(this, Instruction.class);
                intent.putExtra("title", "Minimal pairs");
                //TODO Think of some useful descriptions and instructions
                intent.putExtra("description", getResources().getString(R.string.DescriptionMinPairs));
                intent.putExtra("instruction", getResources().getString(R.string.ExplanationMinPairs));
                break;
            case "Minimal pairs 2":
                intent = new Intent(this, Instruction.class);
                intent.putExtra("title", "Minimal pairs 2");
                //TODO Think of some useful descriptions and instructions
                intent.putExtra("description", getResources().getString(R.string.DescriptionMinPairs2));
                intent.putExtra("instruction", getResources().getString(R.string.ExplanationMinPairs2));
                break;
            case "Word list":
                intent = new Intent(this, Instruction.class);
                intent.putExtra("title", "Word list");
                //TODO Think of some useful descriptions and instructions
                intent.putExtra("description", getResources().getString(R.string.DescriptionWordList));
                intent.putExtra("instruction", getResources().getString(R.string.ExplanationWordList));
                break;
            case "Sentence reading":
                intent = new Intent(this, Instruction.class);
                intent.putExtra("title", "Sentence reading");
                //TODO Think of some useful descriptions and instructions
                intent.putExtra("description", getResources().getString(R.string.DescriptionSentenceReading));
                intent.putExtra("instruction", getResources().getString(R.string.ExplanationSentenceReading));
                break;
            case "Syllable repetition":
                intent = new Intent(this, Instruction.class);
                intent.putExtra("title", "Syllable repetition");
                //TODO Think of some useful descriptions and instructions
                intent.putExtra("description", getResources().getString(R.string.DescriptionSyllableRepetition));
                intent.putExtra("instruction", getResources().getString(R.string.ExplanationSyllableRepetition));
                break;
            case "Picture description":
                intent = new Intent(this, Instruction.class);
                intent.putExtra("title", "Picture description");
                //TODO Think of some useful descriptions and instructions
                intent.putExtra("description", getResources().getString(R.string.DescriptionPictureDescription));
                intent.putExtra("instruction", getResources().getString(R.string.ExplanationPictureDescription));
                break;
            default:
                intent = new Intent(this, MainActivity.class);
                break;
        }
        this.startActivity(intent);
    }
}
