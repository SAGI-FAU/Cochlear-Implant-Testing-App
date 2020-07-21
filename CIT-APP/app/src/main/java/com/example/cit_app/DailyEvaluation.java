package com.example.cit_app;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DailyEvaluation#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DailyEvaluation extends Fragment {

    BarChart barChart;
    FeatureDataService featureDataService;
    public DailyEvaluation() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment DailyEvaluation.
     */
    // TODO: Rename and change types and number of parameters
    public static DailyEvaluation newInstance() {
        DailyEvaluation fragment = new DailyEvaluation();
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
        View view = inflater.inflate(R.layout.fragment_daily_evaluation, container, false);
        barChart = view.findViewById(R.id.dailyEvaluationBarChart);
        featureDataService = new FeatureDataService(getActivity().getApplicationContext());
        barChart.setDrawValueAboveBar(false);
        barChart.setMaxVisibleValueCount(1);
        barChart.setDrawGridBackground(false);
        Calendar c = Calendar.getInstance();
        c.set(2020, 6, 12);
        List<FeatureDA> intonation = featureDataService.get_avg_all_feat_per_day(featureDataService.intonation_name);
        List<FeatureDA> hearing = featureDataService.get_avg_all_feat_per_day(featureDataService.hearing_name);
        float intonation_value = 0;
        float hearingAbility = 0;
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
        entries.add(new BarEntry(1f, 0.8f));
        entries.add(new BarEntry(2f, intonation_value/100));
        BarDataSet dataset = new BarDataSet(entries, "");
        dataset.setColors(ColorTemplate.COLORFUL_COLORS);
        BarData data = new BarData(dataset);
        data.setBarWidth(0.4f);
        barChart.setData(data);
        barChart.getDescription().setEnabled(false);
        barChart.getLegend().setEnabled(false);
        String[] measurements = {"Hearing", "Speech Rate", "Intonation"};
        ValueFormatter xAxisFormatter = new DayAxisValueFormatter(barChart, measurements);
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(7);
        xAxis.setValueFormatter(xAxisFormatter);
        YAxis yAxis = barChart.getAxisLeft();
        yAxis.setDrawGridLines(false);
        yAxis.setAxisMaximum(1.1f);
        yAxis.setAxisMinimum(0.f);
        YAxis yAxis1 = barChart.getAxisRight();
        yAxis1.setDrawGridLines(false);
        yAxis1.setDrawAxisLine(false);
        yAxis1.setDrawLabels(false);
        return view;
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