package com.example.cit_app.other_activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import com.example.cit_app.R;
import com.example.cit_app.data_access.FeatureDA;
import com.example.cit_app.data_access.FeatureDataService;
import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.DefaultAxisValueFormatter;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class EvaluationOverTime extends AppCompatActivity {
    private LineChart lineChart;
    private FeatureDataService featureDataService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluation_over_time);
        lineChart = findViewById(R.id.evaluation_over_time);
        featureDataService = new FeatureDataService(this);
        lineChart.setMaxVisibleValueCount(1);
        //barChart.setDrawGridBackground(false);
        List<Entry> intonation = new ArrayList<>();
        List<Entry> hearing = new ArrayList<>();
        int counter = 1;
        Calendar c = Calendar.getInstance();
        for(int i = 0; i < 12; i++) {
            c.set(Calendar.MONTH, i);
            float intonation_value = featureDataService.get_avg_feature_for_month(featureDataService.intonation_name, c.getTime()).getFeature_value();
            intonation.add(new Entry(counter, intonation_value/100));
            float hearing_value = featureDataService.get_avg_feature_for_month(featureDataService.hearing_name, c.getTime()).getFeature_value();
            hearing.add(new Entry(counter, hearing_value));
            counter ++;
        }
        LineDataSet dataset = new LineDataSet(intonation, "Intonation");
        LineDataSet dataset2 = new LineDataSet(hearing, "hearing");
        dataset.setColors(ColorTemplate.rgb("#ff0000"));
        dataset2.setColors(ColorTemplate.rgb("#0000FF"));
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(dataset);
        dataSets.add(dataset2);
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
        c.get(Calendar.YEAR);
        String[] labels = {"01/" + c.get(Calendar.YEAR), "02/" + c.get(Calendar.YEAR), "03/" + c.get(Calendar.YEAR), "04/" + c.get(Calendar.YEAR), "05/" + c.get(Calendar.YEAR), "06/" + c.get(Calendar.YEAR), "07/" + c.get(Calendar.YEAR), "08/" + c.get(Calendar.YEAR), "09/" + c.get(Calendar.YEAR), "10/" + c.get(Calendar.YEAR), "11/" + c.get(Calendar.YEAR), "12/" + c.get(Calendar.YEAR)};
        //xAxis.setValueFormatter(new DayAxisValueFormatter(lineChart, labels));
        YAxis yAxis = lineChart.getAxisLeft();
        yAxis.setDrawGridLines(false);
        yAxis.setAxisMaximum(1.1f);
        YAxis yAxis1 = lineChart.getAxisRight();
        yAxis1.setDrawGridLines(false);
        yAxis1.setDrawAxisLine(false);
        yAxis1.setDrawLabels(false);
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

}