package com.example.cit_app.other_activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.example.cit_app.R;
import com.example.cit_app.data_access.FeatureDA;
import com.example.cit_app.data_access.FeatureDataService;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ResultsPerDay extends AppCompatActivity {

    private BarChart barChart;
    private FeatureDataService featureDataService;
    private Button home_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_results_per_day);
        getSupportActionBar().setTitle(getResources().getString(R.string.ResultsPerDay)); // for set actionbar title
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        barChart = findViewById(R.id.dailyEvaluationBarChart);
        featureDataService = new FeatureDataService(this);
        barChart.setDrawGridBackground(false);
        home_button = findViewById(R.id.home_button);
        home_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MainActivity.class);
                v.getContext().startActivity(intent);
            }
        });
        Calendar c = Calendar.getInstance();
        c.set(2020, 6, 12);
        List<FeatureDA> intonation = featureDataService.get_avg_all_feat_per_day(featureDataService.intonation_name);
        List<FeatureDA> hearing = featureDataService.get_avg_all_feat_per_day(featureDataService.hearing_name);
        List<FeatureDA> speech_rate = featureDataService.get_avg_all_feat_per_day(featureDataService.vrate_name);
        float intonation_value = 0;
        float hearingAbility = 0;
        float speech_rate_value = 0;
        if(speech_rate.size() == 0) {
            speech_rate_value = 0;
        } else {
            speech_rate_value = speech_rate.get(0).getFeature_value();
            if (speech_rate_value < 25) {
                speech_rate_value = speech_rate_value/25;
            }
            if (speech_rate_value > 55) {
                speech_rate_value = 1 - ((speech_rate_value/55) - 1);
                if (speech_rate_value < 0) {
                    speech_rate_value = 0;
                }
            }
            if (speech_rate_value <= 55 && speech_rate_value >= 25) {
                speech_rate_value = 1;
            }
        }
        if(intonation.size() == 0) {
            intonation_value = 0;
        } else {
            intonation_value = intonation.get(0).getFeature_value();
        }
        if(hearing.size() == 0) {
            hearingAbility = 0;
        } else {
            hearingAbility = hearing.get(0).getFeature_value();
        }
        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0f, hearingAbility));
        entries.add(new BarEntry(1f, speech_rate_value));
        entries.add(new BarEntry(2f, intonation_value/100));
        BarDataSet dataset = new BarDataSet(entries, "");
        dataset.setColors(new int[] {Color.BLACK, Color.WHITE, Color.BLUE});
        BarData data = new BarData(dataset);
        data.setBarWidth(0.4f);
        ValueFormatter percentageFormatter = new PercentageFormatter();
        data.setValueFormatter(percentageFormatter);
        data.setValueTextSize(20);
        barChart.setData(data);
        barChart.getDescription().setEnabled(false);
        barChart.getLegend().setEnabled(false);
        barChart.setExtraBottomOffset(50);
        String[] measurements = {getResources().getString(R.string.hearingAbility), getResources().getString(R.string.speechrate), getResources().getString(R.string.intonation)};
        ValueFormatter xAxisFormatter = new DayAxisValueFormatter(barChart, measurements);
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(7);
        xAxis.setTextSize(17);
        xAxis.setYOffset(20);
        xAxis.setValueFormatter(xAxisFormatter);
        YAxis yAxis = barChart.getAxisLeft();
        yAxis.setDrawGridLines(false);
        yAxis.setAxisMaximum(1.1f);
        yAxis.setAxisMinimum(0.f);
        yAxis.setTextSize(20);
        YAxis yAxis1 = barChart.getAxisRight();
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

    public class DayAxisValueFormatter extends ValueFormatter {
        private final BarLineChartBase<?> chart;
        private String[] values;
        public DayAxisValueFormatter(BarLineChartBase<?> chart, String[] values) {
            this.chart = chart;
            this.values = values;
        }
        @Override
        public String getFormattedValue(float value) {
            return values[(int)value];
        }
    }

    public class PercentageFormatter extends ValueFormatter {

        @Override
        public String getFormattedValue(float value) {
            return String.format ("%.2f", value) + "%";
        }
    }
}