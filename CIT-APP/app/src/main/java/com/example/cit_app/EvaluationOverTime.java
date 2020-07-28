package com.example.cit_app;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EvaluationOverTime#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EvaluationOverTime extends Fragment {
    private LineChart lineChart;
    private FeatureDataService featureDataService;

    public EvaluationOverTime() {
        // Required empty public constructor
    }

    public static EvaluationOverTime newInstance() {
        return new EvaluationOverTime();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_evaluation_over_time, container, false);
        lineChart = view.findViewById(R.id.evaluation_over_time);
        featureDataService = new FeatureDataService(view.getContext());
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
        return view;
    }
}