package com.example.cit_app.other_activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cit_app.R;
import com.example.cit_app.data_access.CSVFileWriter;
import com.example.cit_app.data_access.FeatureDA;
import com.example.cit_app.data_access.FeatureDataService;
import com.example.cit_app.data_access.PatientDA;
import com.example.cit_app.data_access.PatientDataService;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ResultsPerDay extends AppCompatActivity {

    private BarChart barChart;
    private FeatureDataService featureDataService;
    private Button home_button;
    private ImageView homeImage;
    private float intonation_value = 0;
    private float hearingAbility = 0;
    private float speech_rate_value = 0;
    private float real_intonation_value = 0;
    private float pitch_mean_value = 0;
    private float real_speech_rate_value = 0;
    private float pitch_mean_entry = 0;
    private PatientDataService patientDataService;
    private Dialog dialog;
    private PatientDA patientDA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_results_per_day);
        getSupportActionBar().setTitle(getResources().getString(R.string.ResultsPerDay)); // for set actionbar title
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        barChart = findViewById(R.id.dailyEvaluationBarChart);
        homeImage = findViewById(R.id.homeImage);
        featureDataService = new FeatureDataService(this);
        patientDataService = new PatientDataService(this);
        barChart.setDrawGridBackground(false);
        dialog = new Dialog(this);
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
        FeatureDA intonation = featureDataService.get_last_feat_value(featureDataService.intonation_name);
        FeatureDA hearing = featureDataService.get_last_feat_value(featureDataService.hearing_name);
        FeatureDA speech_rate = featureDataService.get_last_feat_value(featureDataService.vrate_name);
        FeatureDA real_intonation = featureDataService.get_last_feat_value(featureDataService.real_intonation_name);
        FeatureDA pitch_mean = featureDataService.get_last_feat_value(featureDataService.pitch_mean_name);
        FeatureDA real_speech_rate = featureDataService.get_last_feat_value(featureDataService.real_speech_rate_name);
        real_intonation_value = real_intonation.getFeature_value();
        real_speech_rate_value = real_speech_rate.getFeature_value();
        pitch_mean_value = pitch_mean.getFeature_value();
        Calendar c = Calendar.getInstance();
        c.set(2020, 6, 12);
        if(speech_rate.getFeature_value() == 0) {
            speech_rate_value = 0;
        } else {
            speech_rate_value = speech_rate.getFeature_value();
        }
        if(intonation.getFeature_value() == 0) {
            intonation_value = 0;
        } else {
            intonation_value = intonation.getFeature_value();
        }
        if(hearing.getFeature_value() == 0) {
            hearingAbility = 0;
        } else {
            hearingAbility = hearing.getFeature_value();
        }
        patientDA = patientDataService.getPatient();
        if(patientDA.getGender().equals(getResources().getString(R.string.male))) {
            //120Hz is the mean value of male test speakers from the info_sentences.csv dataset
            if(pitch_mean_value >= 120) {
                pitch_mean_entry = 1;
            } else {
                pitch_mean_entry = pitch_mean_value/120;
            }
        } else {
            //120Hz is the mean value of female test speakers from the info_sentences.csv dataset
            if(pitch_mean_value >= 190) {
                pitch_mean_entry = 1;
            } else {
                pitch_mean_entry = pitch_mean_value/190;
            }
        }
        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0f, hearingAbility));
        entries.add(new BarEntry(1f, speech_rate_value));
        entries.add(new BarEntry(2f, intonation_value));
        entries.add(new BarEntry(3f, pitch_mean_entry));
        BarDataSet dataset = new BarDataSet(entries, "");
        dataset.setColors(new int[] {Color.BLACK, Color.WHITE, Color.BLUE, Color.RED});
        BarData data = new BarData(dataset);
        data.setBarWidth(0.4f);
        ValueFormatter percentageFormatter = new PercentageFormatter();
        data.setValueFormatter(percentageFormatter);
        data.setValueTextSize(20);
        barChart.setData(data);
        barChart.getDescription().setEnabled(false);
        barChart.getLegend().setEnabled(false);
        barChart.setExtraBottomOffset(50);
        barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                showPopUp(getWindow().getDecorView().getRootView(), e.getX());
            }

            @Override
            public void onNothingSelected() {

            }
        });
        String[] measurements = {getResources().getString(R.string.hearingAbility), getResources().getString(R.string.speechrate), getResources().getString(R.string.intonation), getResources().getString(R.string.pitch_mean)};
        ValueFormatter xAxisFormatter = new DayAxisValueFormatter(barChart, measurements);
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(7);
        xAxis.setTextSize(13);
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
        if(getIntent().getBooleanExtra("trainingset", false)) {
            try {
                export_data();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
            return String.format ("%.1f", value * 100) + "%";
        }
    }

    private void showPopUp(View v, float number) {

        dialog.setContentView(R.layout.popup_daily_results);
        TextView text = dialog.findViewById(R.id.close);
        TextView textMessage = dialog.findViewById(R.id.resultsMessage);
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        if (number == 0.0) {
            textMessage.setText(getResources().getString(R.string.hearingAbilityText) + String.format("%.1f", hearingAbility * 100) + "%");
        }
        if(patientDA.getGender().equals(getResources().getString(R.string.male))) {
            if (number == 1.0) {
                textMessage.setText(getResources().getString(R.string.speechRateText) + String.format("%.1f", real_speech_rate_value));
            }
            if (number == 2.0) {
                textMessage.setText(getResources().getString(R.string.intonationText) + String.format("%.1f", real_intonation_value));
            }
            if (number == 3.0) {
                textMessage.setText(getResources().getString(R.string.pitchText) + String.format("%.1f", pitch_mean_value));
            }
        } else {
            if (number == 1.0) {
                textMessage.setText(getResources().getString(R.string.speechRateTextF) + String.format("%.1f", real_speech_rate_value));
            }
            if (number == 2.0) {
                textMessage.setText(getResources().getString(R.string.intonationTextF) + String.format("%.1f", real_intonation_value));
            }
            if (number == 3.0) {
                textMessage.setText(getResources().getString(R.string.pitchTextF) + String.format("%.1f", pitch_mean_value));
            }
        }
        dialog.show();
    }

    private void export_data()  throws IOException {
        String PATH = Environment.getExternalStorageDirectory() + "/CITA/METADATA/RESULTS/";
        CSVFileWriter mCSVFileWriter = new CSVFileWriter("Results", PATH);

        String[] hearing_ability={"Hearing_Ability", String.valueOf(hearingAbility)};
        String[] intonation={"Intonation", String.valueOf(intonation_value)};
        String[] speech_rate={"Speech_Rate", String.valueOf(speech_rate_value)};
        String[] speech_rate_real = {"Speech_Rate_Real", String.format("%.1f",real_speech_rate_value)};
        mCSVFileWriter.write(speech_rate_real);
        String[] intonation_real = {"Intonation_Real", String.format("%.1f",real_intonation_value)};
        String[] pitch_mean = {"Pitch_Mean", String.format("%.1f",pitch_mean_value)};
        mCSVFileWriter.write(pitch_mean);
        mCSVFileWriter.write(intonation_real);
        mCSVFileWriter.write(hearing_ability);
        mCSVFileWriter.write(intonation);
        mCSVFileWriter.write(speech_rate);
        mCSVFileWriter.close();

    }
}