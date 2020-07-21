package com.example.cit_app;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cit_app.other_activities.Instruction;
import com.example.cit_app.other_activities.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExercisesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExercisesFragment extends Fragment implements ExerciseAdapter.OnExerciseListener {
    private String s1[];
    private int images[];
    private RecyclerView exerciseList;

    public ExercisesFragment() {
        // Required empty public constructor
    }
    public static ExercisesFragment newInstance(String param1, String param2) {
        ExercisesFragment fragment = new ExercisesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_exercises, container, false);
        s1 = getResources().getStringArray(R.array.Exercises);
        images = new int[]{R.drawable.hearing, R.drawable.hearing, R.drawable.speech_results, R.drawable.speech_results, R.drawable.speech_results, R.drawable.speech_results};
        exerciseList = view.findViewById(R.id.element_list);
        ExerciseAdapter exAd = new ExerciseAdapter(view.getContext(), s1, images, this);
        exerciseList.setAdapter(exAd);
        exerciseList.setLayoutManager(new LinearLayoutManager(view.getContext()));
        return view;
    }

    @Override
    public void onExerciseClick(int position) {
        Intent intent;
        switch(s1[position]) {
            case "Minimal pairs":
            case "Minimalpaare":
                intent = new Intent(getContext(), Instruction.class);
                intent.putExtra("title", getResources().getString(R.string.MinimalPairs));
                //TODO Think of some useful descriptions and instructions
                intent.putExtra("description", getResources().getString(R.string.DescriptionMinPairs));
                intent.putExtra("instruction", getResources().getString(R.string.ExplanationMinPairs));
                intent.putExtra("trainingset", false);
                break;
            case "Minimal pairs 2":
            case "Minimalpaare2":
                intent = new Intent(getContext(), Instruction.class);
                intent.putExtra("title", getResources().getString(R.string.MinimalPairs2));
                //TODO Think of some useful descriptions and instructions
                intent.putExtra("description", getResources().getString(R.string.DescriptionMinPairs2));
                intent.putExtra("instruction", getResources().getString(R.string.ExplanationMinPairs2));
                intent.putExtra("trainingset", false);
                break;
            case "Word list":
            case "Wortliste":
                intent = new Intent(getContext(), Instruction.class);
                intent.putExtra("title", getResources().getString(R.string.WordList));
                //TODO Think of some useful descriptions and instructions
                intent.putExtra("description", getResources().getString(R.string.DescriptionWordList));
                intent.putExtra("instruction", getResources().getString(R.string.ExplanationWordList));
                intent.putExtra("trainingset", false);
                break;
            case "Sentence reading":
            case "Sätze lesen":
                intent = new Intent(getContext(), Instruction.class);
                intent.putExtra("title", getResources().getString(R.string.SentenceReading));
                //TODO Think of some useful descriptions and instructions
                intent.putExtra("description", getResources().getString(R.string.DescriptionSentenceReading));
                intent.putExtra("instruction", getResources().getString(R.string.ExplanationSentenceReading));
                intent.putExtra("trainingset", false);
                break;
            case "Syllable repetition":
            case "Silben wiederholen":
                intent = new Intent(getContext(), Instruction.class);
                intent.putExtra("title", getResources().getString(R.string.SyllableRepetition));
                //TODO Think of some useful descriptions and instructions
                intent.putExtra("description", getResources().getString(R.string.DescriptionSyllableRepetition));
                intent.putExtra("instruction", getResources().getString(R.string.ExplanationSyllableRepetition));
                intent.putExtra("trainingset", false);
                break;
            case "Picture description":
            case "Bildbeschreibung":
                intent = new Intent(getContext(), Instruction.class);
                intent.putExtra("title", getResources().getString(R.string.PictureDescription));
                //TODO Think of some useful descriptions and instructions
                intent.putExtra("description", getResources().getString(R.string.DescriptionPictureDescription));
                intent.putExtra("instruction", getResources().getString(R.string.ExplanationPictureDescription));
                intent.putExtra("trainingset", false);
                break;
            default:
                intent = new Intent(getContext(), MainActivity.class);
                break;
        }
        this.startActivity(intent);
    }
}