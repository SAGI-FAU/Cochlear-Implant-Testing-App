package com.example.cit_app.other_activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.example.cit_app.R;
import com.example.cit_app.data_access.FeatureDataService;
import com.github.mikephil.charting.charts.LineChart;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_results_per_year);
        getSupportActionBar().setTitle(getResources().getString(R.string.ResultsPerYear)); // for set actionbar title
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        lineChart = findViewById(R.id.evaluation_over_time);
        featureDataService = new FeatureDataService(this);
        lineChart.setMaxVisibleValueCount(1);
        List<Entry> intonation = new ArrayList<>();
        List<Entry> hearing = new ArrayList<>();
        List<Entry> speech_rate_values = new ArrayList<>();
        int counter = 1;
        Calendar c = Calendar.getInstance();
        for(int i = 0; i < 12; i++) {
            c.set(Calendar.MONTH, i);
            float intonation_value = featureDataService.get_avg_feature_for_month(featureDataService.intonation_name, c.getTime()).getFeature_value();
            intonation.add(new Entry(counter, intonation_value/100));
            float hearing_value = featureDataService.get_avg_feature_for_month(featureDataService.hearing_name, c.getTime()).getFeature_value();
            hearing.add(new Entry(counter, hearing_value));
            float speech_rate = featureDataService.get_avg_feature_for_month(featureDataService.vrate_name, c.getTime()).getFeature_value();
            if (speech_rate < 25) {
                speech_rate = speech_rate/25;
            }
            if (speech_rate > 55) {
                speech_rate = 1 - ((speech_rate/55) - 1);
                if (speech_rate < 0) {
                    speech_rate = 0;
                }
            }
            if (speech_rate <= 55 && speech_rate >= 25) {
                speech_rate = 1;
            }
            speech_rate_values.add(new Entry(counter, speech_rate));
            counter ++;
        }
        LineDataSet dataset = new LineDataSet(intonation, getResources().getString(R.string.intonation));
        LineDataSet dataset2 = new LineDataSet(hearing, getResources().getString(R.string.hearingAbility));
        LineDataSet dataset3 = new LineDataSet(speech_rate_values, getResources().getString(R.string.speechrate));
        dataset.setColors(ColorTemplate.rgb("#000000"));
        dataset2.setColors(ColorTemplate.rgb("#FFFFFF"));
        dataset3.setColors(ColorTemplate.rgb("#0084f9"));
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(dataset);
        dataSets.add(dataset2);
        dataSets.add(dataset3);
        LineData data = new LineData(dataSets);
        lineChart.setData(data);
        lineChart.getDescription().setEnabled(false);
        lineChart.setDrawGridBackground(false);
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setAxisMaximum(12);
        xAxis.setAxisMinimum(1);
        xAxis.setLabelCount(12);
        YAxis yAxis = lineChart.getAxisLeft();
        yAxis.setDrawGridLines(false);
        yAxis.setAxisMinimum(0.0f);
        yAxis.setAxisMaximum(1.1f);
        YAxis yAxis1 = lineChart.getAxisRight();
        yAxis1.setDrawGridLines(false);
        yAxis1.setDrawAxisLine(false);
        yAxis1.setDrawLabels(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }
}