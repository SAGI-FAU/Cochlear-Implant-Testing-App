/**
 * Created by Christoph Popp
 */

package com.example.cit_app.other_activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.example.cit_app.R;
import com.example.cit_app.data_access.FeatureDataService;
import com.example.cit_app.data_access.PatientDA;
import com.example.cit_app.data_access.PatientDataService;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ResultsPerYear extends AppCompatActivity {
    private LineChart lineChart;
    private FeatureDataService featureDataService;
    private Button home_button;
    private PatientDataService patientDataService;
    private PatientDA patientDA;
    private ImageView homeImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_results_per_year);
        getSupportActionBar().setTitle(getResources().getString(R.string.ResultsPerYear)); // for set actionbar title
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //initialize
        lineChart = findViewById(R.id.evaluation_over_time);
        featureDataService = new FeatureDataService(this);
        patientDataService = new PatientDataService(this);
        lineChart.setMaxVisibleValueCount(1);
        homeImage = findViewById(R.id.homeImage);
        homeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MainActivity.class);
                v.getContext().startActivity(intent);
            }
        });
        home_button = findViewById(R.id.home_button);
        home_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MainActivity.class);
                v.getContext().startActivity(intent);
            }
        });
        List<Entry> intonation = new ArrayList<>();
        List<Entry> hearing = new ArrayList<>();
        List<Entry> speech_rate_values = new ArrayList<>();
        List<Entry> pitch_mean_values = new ArrayList<>();
        int counter = 1;
        Calendar c = Calendar.getInstance();
        patientDA = patientDataService.getPatient();
        for(int i = 0; i < 12; i++) {
            c.set(Calendar.MONTH, i);
            float intonation_value = featureDataService.get_avg_feature_for_month(featureDataService.intonation_name, c.getTime()).getFeature_value();
            intonation.add(new Entry(counter, intonation_value));
            float hearing_value = featureDataService.get_avg_feature_for_month(featureDataService.hearing_name, c.getTime()).getFeature_value();
            hearing.add(new Entry(counter, hearing_value));
            float speech_rate = featureDataService.get_avg_feature_for_month(featureDataService.vrate_name, c.getTime()).getFeature_value();
            speech_rate_values.add(new Entry(counter, speech_rate));
            float pitch_value = featureDataService.get_avg_feature_for_month(featureDataService.pitch_mean_name, c.getTime()).getFeature_value();
            pitch_mean_values.add(new Entry(counter, pitch_value));
            counter ++;
        }
        LineDataSet dataset = new LineDataSet(intonation, getResources().getString(R.string.intonation));
        LineDataSet dataset2 = new LineDataSet(hearing, getResources().getString(R.string.hearingAbility));
        LineDataSet dataset3 = new LineDataSet(speech_rate_values, getResources().getString(R.string.speechrate));
        LineDataSet dataset4 = new LineDataSet(pitch_mean_values, getResources().getString(R.string.pitch_mean));
        dataset.setColors(ColorTemplate.rgb("#0084f9"));
        dataset2.setColors(ColorTemplate.rgb("#000000"));
        dataset3.setColors(ColorTemplate.rgb("#FFFFFF"));
        dataset4.setColors(ColorTemplate.rgb("#00FF00"));
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(dataset);
        dataSets.add(dataset2);
        dataSets.add(dataset3);
        dataSets.add(dataset4);
        LineData data = new LineData(dataSets);
        Legend legend = lineChart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setWordWrapEnabled(true);
        legend.setTextSize(18);
        lineChart.setData(data);
        lineChart.getDescription().setEnabled(false);
        lineChart.setDrawGridBackground(false);
        lineChart.setExtraBottomOffset(50);
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setAxisMaximum(12);
        xAxis.setAxisMinimum(1);
        xAxis.setLabelCount(12);
        xAxis.setYOffset(20);
        xAxis.setTextSize(20);
        YAxis yAxis = lineChart.getAxisLeft();
        yAxis.setDrawGridLines(false);
        yAxis.setAxisMinimum(0.0f);
        yAxis.setAxisMaximum(1.1f);
        yAxis.setTextSize(20);
        YAxis yAxis1 = lineChart.getAxisRight();
        yAxis1.setDrawGridLines(false);
        yAxis1.setDrawAxisLine(false);
        yAxis1.setDrawLabels(false);
    }

    //This allows you to return to the activity before
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

}