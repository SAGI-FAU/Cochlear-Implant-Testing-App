/**
 * Created by Christoph Popp
 */

package com.example.cit_app.other_activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.io.IOException;
import java.util.ArrayList;
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
    private float real_mean_pitch_value = 0;
    private float real_speech_rate_value = 0;
    private float shown_pitch_mean = 0;
    private float shown_intonation = 0;
    private float shown_speech_rate = 0;
    private PatientDataService patientDataService;
    private Dialog dialog, explanation;
    private PatientDA patientDA;
    //These values were calculated from a personal sample
    private final float SPEECHRATEMEANMALE = 3.38095238095238f;
    private final float INTONATIONMEANMALE = 79.0333333333333f;
    private final float PITCHMEANMALE = 257.919f;
    private final float SPEECHRATEDEVMALE = 0.5133f;
    private final float INTONATIONDEVMALE = 20.7496f;
    private final float PITCHDEVMALE = 56.8301f;
    private final float SPEECHRATEMEANFEMALE = 3.1143f;
    private final float INTONATIONMEANFEMALE = 96.3429f;
    private final float PITCHMEANFEMALE = 345.4286f;
    private final float SPEECHRATEDEVFEMALE = 0.4518f;
    private final float INTONATIONDEVFEMALE = 19.9838f;
    private final float PITCHDEVFEMALE = 29.2643f;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_results_per_day);
        getSupportActionBar().setTitle(getResources().getString(R.string.ResultsPerDay)); // for set actionbar title
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //initialize
        barChart = findViewById(R.id.dailyEvaluationBarChart);
        homeImage = findViewById(R.id.homeImage);
        featureDataService = new FeatureDataService(this);
        patientDataService = new PatientDataService(this);
        barChart.setDrawGridBackground(false);
        dialog = new Dialog(this);
        explanation = new Dialog(this);
        if(getIntent().getBooleanExtra("trainingset", false)) {
            SharedPreferences prefs = getSharedPreferences("FirstTimePref", MODE_PRIVATE);
            int firstTime = prefs.getInt("FirstTime", 0);
            if (firstTime == 0) {
                showFirstTime();
            }
        }
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
        FeatureDA pitch_mean = featureDataService.get_last_feat_value(featureDataService.pitch_mean_name);
        FeatureDA real_pitch_mean = featureDataService.get_last_feat_value(featureDataService.real_pitch_mean_name);
        FeatureDA real_intonation = featureDataService.get_last_feat_value(featureDataService.real_intonation_name);
        FeatureDA real_speech_rate = featureDataService.get_last_feat_value(featureDataService.real_speech_rate_name);
        real_intonation_value = real_intonation.getFeature_value();
        real_speech_rate_value = real_speech_rate.getFeature_value();
        real_mean_pitch_value = real_pitch_mean.getFeature_value();
        speech_rate_value = speech_rate.getFeature_value();
        intonation_value = intonation.getFeature_value();
        hearingAbility = hearing.getFeature_value();
        pitch_mean_value = pitch_mean.getFeature_value();
        patientDA = patientDataService.getPatient();
        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0f, hearingAbility));
        entries.add(new BarEntry(1f, speech_rate_value));
        entries.add(new BarEntry(2f, intonation_value));
        entries.add(new BarEntry(3f, pitch_mean_value));
        BarDataSet dataset = new BarDataSet(entries, "");
        dataset.setColors(new int[] {Color.BLACK, Color.WHITE, Color.BLUE, Color.GREEN});
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

    //Show a pop up screen with infos if this is the first time daily results are used
    private void showFirstTime() {
        explanation.setContentView(R.layout.popup_results_explanation);
        SharedPreferences prefs = getSharedPreferences("FirstTimePref", MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putInt("FirstTime", 1);
        edit.apply();
        TextView text = explanation.findViewById(R.id.close);
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                explanation.dismiss();
            }
        });
        explanation.show();
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

    //Show pop up with normal distribution
    private void showPopUp(View v, float number) {

        dialog.setContentView(R.layout.popup_daily_results);
        TextView text = dialog.findViewById(R.id.close);
        TextView textMessage = dialog.findViewById(R.id.resultsMessage);
        LineChart lineChart = dialog.findViewById(R.id.gaussianDist);
        lineChart.setMaxVisibleValueCount(1);
        List<Entry> gauss = new ArrayList<>();
        List<Entry> data = new ArrayList<>();
        gauss.add(new Entry(-5, 1.4867195147343E-06f));
        gauss.add(new Entry(-4.8f, 3.96129909103208E-06f));
        gauss.add(new Entry(-4.6f, 1.01408520654868E-05f));
        gauss.add(new Entry(-4.4f, 2.49424712900535E-05f));
        gauss.add(new Entry(-4.2f, 5.89430677565399E-05f));
        gauss.add(new Entry(-4.0f, 0.000133830225765f));
        gauss.add(new Entry(-3.8f, 0.000291946925791f));
        gauss.add(new Entry(-3.6f, 0.000611901930114f));
        gauss.add(new Entry(-3.4f, 0.001232219168473f));
        gauss.add(new Entry(-3.2f, 0.002384088201465f));
        gauss.add(new Entry(-3, 0.004431848411938f));
        gauss.add(new Entry(-2.8f, 0.00791545158298f));
        gauss.add(new Entry(-2.6f, 0.013582969233686f));
        gauss.add(new Entry(-2.4f, 0.022394530294843f));
        gauss.add(new Entry(-2.2f, 0.035474592846232f));
        gauss.add(new Entry(-2, 0.053990966513188f));
        gauss.add(new Entry(-1.8f, 0.078950158300894f));
        gauss.add(new Entry(-1.6f, 0.110920834679456f));
        gauss.add(new Entry(-1.4f, 0.149727465635745f));
        gauss.add(new Entry(-1.2f, 0.194186054983213f));
        gauss.add(new Entry(-1, 0.241970724519143f));
        gauss.add(new Entry(-0.8f, 0.289691552761483f));
        gauss.add(new Entry(-0.6f, 0.3332246028918f));
        gauss.add(new Entry(-0.4f, 0.368270140303323f));
        gauss.add(new Entry(-0.2f, 0.391042693975456f));
        gauss.add(new Entry(-0, 0.398942280401433f));
        gauss.add(new Entry(0.2f, 0.391042693975456f));
        gauss.add(new Entry(0.4f, 0.368270140303323f));
        gauss.add(new Entry(0.6f, 0.3332246028918f));
        gauss.add(new Entry(0.8f, 0.289691552761483f));
        gauss.add(new Entry(1, 0.241970724519143f));
        gauss.add(new Entry(1.2f, 0.194186054983213f));
        gauss.add(new Entry(1.4f, 0.149727465635745f));
        gauss.add(new Entry(1.6f, 0.110920834679456f));
        gauss.add(new Entry(1.8f, 0.078950158300894f));
        gauss.add(new Entry(2, 0.053990966513188f));
        gauss.add(new Entry(2.2f, 0.035474592846232f));
        gauss.add(new Entry(2.4f, 0.022394530294843f));
        gauss.add(new Entry(2.6f, 0.013582969233686f));
        gauss.add(new Entry(2.8f, 0.00791545158298f));
        gauss.add(new Entry(3, 0.004431848411938f));
        gauss.add(new Entry(3.2f, 0.002384088201465f));
        gauss.add(new Entry(3.4f, 0.001232219168473f));
        gauss.add(new Entry(3.6f, 0.000611901930114f));
        gauss.add(new Entry(3.8f, 0.000291946925791f));
        gauss.add(new Entry(4, 0.000133830225765f));
        gauss.add(new Entry(4.2f, 5.89430677565399E-05f));
        gauss.add(new Entry(4.4f, 2.49424712900535E-05f));
        gauss.add(new Entry(4.6f, 1.01408520654868E-05f));
        gauss.add(new Entry(4.8f, 3.96129909103208E-06f));
        gauss.add(new Entry(5, 1.4867195147343E-06f));
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
                shown_speech_rate = (real_speech_rate_value - SPEECHRATEMEANMALE)/SPEECHRATEDEVMALE;
                float x = (float)(1/(Math.sqrt(2 * Math.PI)));
                float r = (float)Math.pow(shown_speech_rate, 2);
                float y = (float)Math.exp((-(1f/2f)) * r);
                float z = x * y;
                if(shown_speech_rate > 5) {
                    shown_speech_rate = 5;
                    data.add(new Entry(shown_speech_rate, 0));
                } else {
                    if (shown_speech_rate < -5) {
                        shown_speech_rate = -5;
                        data.add(new Entry(shown_speech_rate, 0));
                    } else {
                        data.add(new Entry(shown_speech_rate, z));
                        data.add(new Entry(shown_speech_rate, 0));
                    }
                }
            }
            if (number == 2.0) {
                textMessage.setText(getResources().getString(R.string.intonationText) + String.format("%.1f", real_intonation_value));
                shown_intonation = (real_intonation_value - INTONATIONMEANMALE)/INTONATIONDEVMALE;
                float x = (float)(1/(Math.sqrt(2 * Math.PI)));
                float r = (float)Math.pow(shown_intonation, 2);
                float y = (float)Math.exp((-(1f/2f)) * r);
                float z = x * y;
                if(shown_intonation > 5) {
                    shown_intonation = 5;
                    data.add(new Entry(shown_intonation, 0));
                } else {
                    if (shown_intonation < -5) {
                        shown_intonation = -5;
                        data.add(new Entry(shown_intonation, 0));
                    } else {
                        data.add(new Entry(shown_intonation, z));
                        data.add(new Entry(shown_intonation, 0));
                    }
                }
            }
            if (number == 3.0) {
                textMessage.setText(getResources().getString(R.string.pitchText) + String.format("%.1f", real_mean_pitch_value));
                shown_pitch_mean = (pitch_mean_value - PITCHMEANMALE)/PITCHDEVMALE;
                float x = (float)(1/(Math.sqrt(2 * Math.PI)));
                float r = (float)Math.pow(shown_pitch_mean, 2);
                float y = (float)Math.exp((-(1f/2f)) * r);
                float z = x * y;
                if(shown_pitch_mean > 5) {
                    shown_pitch_mean = 5;
                    data.add(new Entry(shown_pitch_mean, 0));
                } else {
                    if (shown_pitch_mean < -5) {
                        shown_pitch_mean = -5;
                        data.add(new Entry(shown_pitch_mean, 0));
                    } else {
                        data.add(new Entry(shown_pitch_mean, z));
                        data.add(new Entry(shown_pitch_mean, 0));
                    }
                }
            }
        } else {
            if (number == 1.0) {
                textMessage.setText(getResources().getString(R.string.speechRateTextF) + String.format("%.1f", real_speech_rate_value));
                shown_speech_rate = (real_speech_rate_value - SPEECHRATEMEANFEMALE)/SPEECHRATEDEVFEMALE;
                float x = (float)(1/(Math.sqrt(2 * Math.PI)));
                float r = (float)Math.pow(shown_speech_rate, 2);
                float y = (float)Math.exp((-(1f/2f)) * r);
                float z = x * y;
                if(shown_speech_rate > 5) {
                    shown_speech_rate = 5;
                    data.add(new Entry(shown_speech_rate, 0));
                } else {
                    if (shown_speech_rate < -5) {
                        shown_speech_rate = -5;
                        data.add(new Entry(shown_speech_rate, 0));
                    } else {
                        data.add(new Entry(shown_speech_rate, z));
                        data.add(new Entry(shown_speech_rate, 0));
                    }
                }
            }
            if (number == 2.0) {
                textMessage.setText(getResources().getString(R.string.intonationTextF) + String.format("%.1f", real_intonation_value));
                shown_intonation = (real_intonation_value - INTONATIONMEANFEMALE)/INTONATIONDEVFEMALE;
                float x = (float)(1/(Math.sqrt(2 * Math.PI)));
                float r = (float)Math.pow(shown_intonation, 2);
                float y = (float)Math.exp((-(1f/2f)) * r);
                float z = x * y;
                if(shown_intonation > 5) {
                    shown_intonation = 5;
                    data.add(new Entry(shown_intonation, 0));
                } else {
                    if (shown_intonation < -5) {
                        shown_intonation = -5;
                        data.add(new Entry(shown_intonation, 0));
                    } else {
                        data.add(new Entry(shown_intonation, z));
                        data.add(new Entry(shown_intonation, 0));
                    }
                }
            }
            if (number == 3.0) {
                textMessage.setText(getResources().getString(R.string.pitchTextF) + String.format("%.1f", real_mean_pitch_value));
                shown_pitch_mean = (pitch_mean_value - PITCHMEANFEMALE)/PITCHDEVFEMALE;
                float x = (float)(1/(Math.sqrt(2 * Math.PI)));
                float r = (float)Math.pow(shown_pitch_mean, 2);
                float y = (float)Math.exp((-(1f/2f)) * r);
                float z = x * y;
                if(shown_pitch_mean > 5) {
                    shown_pitch_mean = 5;
                    data.add(new Entry(shown_pitch_mean, 0));
                } else {
                    if (shown_pitch_mean < -5) {
                        shown_pitch_mean = -5;
                        data.add(new Entry(shown_pitch_mean, 0));
                    } else {
                        data.add(new Entry(shown_pitch_mean, z));
                        data.add(new Entry(shown_pitch_mean, 0));
                    }
                }
            }
        }
        LineDataSet dataset = new LineDataSet(gauss, getResources().getString(R.string.gauss));
        LineDataSet dataset2 = new LineDataSet(data, getResources().getString(R.string.speechrate));
        if (number == 1.0)
            dataset2 = new LineDataSet(data, getResources().getString(R.string.speechrate));
        if (number == 2.0)
            dataset2 = new LineDataSet(data, getResources().getString(R.string.intonation));
        if (number == 3.0)
            dataset2 = new LineDataSet(data, getResources().getString(R.string.pitch_mean));
        dataset.setColors(ColorTemplate.rgb("#000000"));
        dataset2.setColors(ColorTemplate.rgb("#00FF00"));
        dataset2.setCircleColor(ColorTemplate.rgb("#00FF00"));
        dataset2.setDrawCircleHole(false);
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataset.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataset2.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataset.setDrawCircles(false);
        dataSets.add(dataset);
        dataSets.add(dataset2);
        LineData dataLine = new LineData(dataSets);
        Legend legend = lineChart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setWordWrapEnabled(true);
        legend.setTextSize(18);
        lineChart.setData(dataLine);
        lineChart.getDescription().setEnabled(false);
        lineChart.setDrawGridBackground(false);
        lineChart.setExtraBottomOffset(50);
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(0.2f); // only intervals of 1 day
        xAxis.setAxisMaximum(5);
        xAxis.setAxisMinimum(-5);
        xAxis.setLabelCount(10);
        xAxis.setYOffset(20);
        xAxis.setTextSize(20);
        YAxis yAxis = lineChart.getAxisLeft();
        yAxis.setDrawGridLines(false);
        yAxis.setAxisMinimum(0.0f);
        yAxis.setAxisMaximum(0.5f);
        yAxis.setTextSize(20);
        YAxis yAxis1 = lineChart.getAxisRight();
        yAxis1.setDrawGridLines(false);
        yAxis1.setDrawAxisLine(false);
        yAxis1.setDrawLabels(false);
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
        String[] pitch_mean = {"Pitch_Mean_Real", String.format("%.1f",real_mean_pitch_value)};
        String[] pitch = {"Pitch_Mean", String.valueOf(pitch_mean_value)};
        mCSVFileWriter.write(pitch_mean);
        mCSVFileWriter.write(intonation_real);
        mCSVFileWriter.write(hearing_ability);
        mCSVFileWriter.write(intonation);
        mCSVFileWriter.write(speech_rate);
        mCSVFileWriter.write(pitch);
        mCSVFileWriter.close();

    }

    //This allows you to return to the activity before
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

}