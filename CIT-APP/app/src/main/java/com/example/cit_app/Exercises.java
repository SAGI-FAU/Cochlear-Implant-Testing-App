package com.example.cit_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

//This class is the view showing the exercise list
public class Exercises extends AppCompatActivity implements ExerciseAdapter.OnExerciseListener {

    private String s1[];
    private RecyclerView exerciseList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercises);
        s1 = getResources().getStringArray(R.array.Exercises);
        exerciseList = findViewById(R.id.element_list);
        ExerciseAdapter exAd = new ExerciseAdapter(this, s1, this);
        exerciseList.setAdapter(exAd);
        exerciseList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onExerciseClick(int position) {
        Intent intent;
        Toast.makeText(this, "GOING HERE", Toast.LENGTH_SHORT).show();
        switch(s1[position]) {
            case "Minimal pairs":
                intent = new Intent(this, Instruction.class);
                intent.putExtra("title", "Minimal pairs");
                //TODO Think of some useful descriptions and instructions
                intent.putExtra("description", "SOME DESCRIPTION");
                intent.putExtra("instruction", "SOME INSTRUCTION");
                break;
            case "Minimal pairs 2":
                intent = new Intent(this, Instruction.class);
                intent.putExtra("title", "Minimal pairs 2");
                //TODO Think of some useful descriptions and instructions
                intent.putExtra("description", "SOME DESCRIPTION");
                intent.putExtra("instruction", "SOME INSTRUCTION");
                break;
            case "Word list":
                intent = new Intent(this, Instruction.class);
                intent.putExtra("title", "Word list");
                //TODO Think of some useful descriptions and instructions
                intent.putExtra("description", "SOME DESCRIPTION");
                intent.putExtra("instruction", "SOME INSTRUCTION");
                break;
            case "Sentence reading":
                intent = new Intent(this, Instruction.class);
                intent.putExtra("title", "Sentence reading");
                //TODO Think of some useful descriptions and instructions
                intent.putExtra("description", "SOME DESCRIPTION");
                intent.putExtra("instruction", "SOME INSTRUCTION");
                break;
            case "Syllable repetition":
                intent = new Intent(this, Instruction.class);
                intent.putExtra("title", "Syllable repetition");
                //TODO Think of some useful descriptions and instructions
                intent.putExtra("description", "SOME DESCRIPTION");
                intent.putExtra("instruction", "SOME INSTRUCTION");
                break;
            case "Picture description":
                intent = new Intent(this, Instruction.class);
                intent.putExtra("title", "Picture description");
                //TODO Think of some useful descriptions and instructions
                intent.putExtra("description", "SOME DESCRIPTION");
                intent.putExtra("instruction", "SOME INSTRUCTION");
                break;
            default:
                intent = new Intent(this, MainActivity.class);
                break;
        }
        this.startActivity(intent);
    }
}
