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
import android.widget.TextView;

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

public class Gaussdistribution extends AppCompatActivity {
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
        setContentView(R.layout.activity_gaussdistribution);
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
            float hearing_value = featureDataService.get_avg_feature_for_month(featureDataService.hearing_name, c.getTime()).getFeature_value();
            hearing.add(new Entry(counter, hearing_value));
            counter ++;
        }
        intonation.add(new Entry(-5, 1.4867195147343E-06f));
        intonation.add(new Entry(-4.8f, 3.96129909103208E-06f));
        intonation.add(new Entry(-4.6f, 1.01408520654868E-05f));
        intonation.add(new Entry(-4.4f, 2.49424712900535E-05f));
        intonation.add(new Entry(-4.2f, 5.89430677565399E-05f));
        intonation.add(new Entry(-4.0f, 0.000133830225765f));
        intonation.add(new Entry(-3.8f, 0.000291946925791f));
        intonation.add(new Entry(-3.6f, 0.000611901930114f));
        intonation.add(new Entry(-3.4f, 0.001232219168473f));
        intonation.add(new Entry(-3.2f, 0.002384088201465f));
        intonation.add(new Entry(-3, 0.004431848411938f));
        intonation.add(new Entry(-2.8f, 0.00791545158298f));
        intonation.add(new Entry(-2.6f, 0.013582969233686f));
        intonation.add(new Entry(-2.4f, 0.022394530294843f));
        intonation.add(new Entry(-2.2f, 0.035474592846232f));
        intonation.add(new Entry(-2, 0.053990966513188f));
        intonation.add(new Entry(-1.8f, 0.078950158300894f));
        intonation.add(new Entry(-1.6f, 0.110920834679456f));
        intonation.add(new Entry(-1.4f, 0.149727465635745f));
        intonation.add(new Entry(-1.2f, 0.194186054983213f));
        intonation.add(new Entry(-1, 0.241970724519143f));
        intonation.add(new Entry(-0.8f, 0.289691552761483f));
        intonation.add(new Entry(-0.6f, 0.3332246028918f));
        intonation.add(new Entry(-0.4f, 0.368270140303323f));
        intonation.add(new Entry(-0.2f, 0.391042693975456f));
        intonation.add(new Entry(-0, 0.398942280401433f));
        intonation.add(new Entry(0.2f, 0.391042693975456f));
        intonation.add(new Entry(0.4f, 0.368270140303323f));
        intonation.add(new Entry(0.6f, 0.3332246028918f));
        intonation.add(new Entry(0.8f, 0.289691552761483f));
        intonation.add(new Entry(1, 0.241970724519143f));
        intonation.add(new Entry(1.2f, 0.194186054983213f));
        intonation.add(new Entry(1.4f, 0.149727465635745f));
        intonation.add(new Entry(1.6f, 0.110920834679456f));
        intonation.add(new Entry(1.8f, 0.078950158300894f));
        intonation.add(new Entry(2, 0.053990966513188f));
        intonation.add(new Entry(2.2f, 0.035474592846232f));
        intonation.add(new Entry(2.4f, 0.022394530294843f));
        intonation.add(new Entry(2.6f, 0.013582969233686f));
        intonation.add(new Entry(2.8f, 0.00791545158298f));
        intonation.add(new Entry(3, 0.004431848411938f));
        intonation.add(new Entry(3.2f, 0.002384088201465f));
        intonation.add(new Entry(3.4f, 0.001232219168473f));
        intonation.add(new Entry(3.6f, 0.000611901930114f));
        intonation.add(new Entry(3.8f, 0.000291946925791f));
        intonation.add(new Entry(4, 0.000133830225765f));
        intonation.add(new Entry(4.2f, 5.89430677565399E-05f));
        intonation.add(new Entry(4.4f, 2.49424712900535E-05f));
        intonation.add(new Entry(4.6f, 1.01408520654868E-05f));
        intonation.add(new Entry(4.8f, 3.96129909103208E-06f));
        intonation.add(new Entry(5, 1.4867195147343E-06f));
        float x = (float)(1/(Math.sqrt(2 * Math.PI)));
        float r = (float)Math.pow(2.18010445947325, 2);
        float y = (float)Math.exp((-(1f/2f)) * r);
        float z = x * y;
        TextView textMessage = findViewById(R.id.resultsMessage);
        pitch_mean_values.add(new Entry(2.18010445947325f, z));
        pitch_mean_values.add(new Entry(2.18010445947325f, 0));
        LineDataSet dataset = new LineDataSet(intonation, getResources().getString(R.string.intonation));
        LineDataSet dataset2 = new LineDataSet(hearing, getResources().getString(R.string.hearingAbility));
        LineDataSet dataset3 = new LineDataSet(speech_rate_values, getResources().getString(R.string.speechrate));
        LineDataSet dataset4 = new LineDataSet(pitch_mean_values, getResources().getString(R.string.pitch_mean));
        dataset.setColors(ColorTemplate.rgb("#000000"));
        dataset2.setColors(ColorTemplate.rgb("#FFFFFF"));
        dataset3.setColors(ColorTemplate.rgb("#0084f9"));
        dataset4.setColors(ColorTemplate.rgb("#00FF00"));
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataset.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataset2.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataset3.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataset4.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataset.setDrawCircles(false);
        dataset2.setDrawCircles(false);
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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }
}