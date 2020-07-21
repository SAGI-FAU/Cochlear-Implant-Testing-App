package com.example.cit_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.cit_app.other_activities.Settings;
import com.example.cit_app.other_activities.Trainingset;

public class MainFragment extends Fragment {

    private Button trainingSet, settings;
    public MainFragment() {
        // Required empty public constructor
    }
    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
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
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        trainingSet = (Button) view.findViewById(R.id.trainingset);
        settings = (Button) view.findViewById(R.id.settings);
        trainingSet.setText(R.string.trainingsetTitle);
        settings.setText(R.string.settings);
        SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(view.getContext());
        int hour = p.getInt("Notification Time", 9);
        Toast.makeText(view.getContext(), "" + hour, Toast.LENGTH_SHORT).show();
        trainingSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Trainingset.class);
                v.getContext().startActivity(intent);
            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Settings.class);
                v.getContext().startActivity(intent);
            }
        });
        return view;
    }
}