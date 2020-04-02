package com.example.cit_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

//This class is the view showing the exercise list
public class Exercises extends AppCompatActivity {

    private String s1[];
    private RecyclerView exerciseList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercises);
        s1 = getResources().getStringArray(R.array.Exercises);
        exerciseList = findViewById(R.id.element_list);
        ExerciseAdapter exAd = new ExerciseAdapter(this, s1);
        exerciseList.setAdapter(exAd);
        exerciseList.setLayoutManager(new LinearLayoutManager(this));

    }
}
