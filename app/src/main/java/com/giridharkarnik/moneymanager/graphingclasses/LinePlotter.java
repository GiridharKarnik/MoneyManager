package com.giridharkarnik.moneymanager.graphingclasses;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;

import com.giridharkarnik.moneymanager.HelperMethodsClasses.HelperMethods;
import com.giridharkarnik.moneymanager.modelobjects.FullStatsActivity;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class LinePlotter {

    Context linePlotterContext;
    HelperMethods lineHelper;
    ArrayList<String> MonthsOfYear;
    private Typeface typeface;

    public LinePlotter(Context c) {
        this.linePlotterContext = c;
        lineHelper = new HelperMethods(linePlotterContext);

        MonthsOfYear = new ArrayList<>();
        MonthsOfYear.add("Jan");
        MonthsOfYear.add("Feb");
        MonthsOfYear.add("Mar");
        MonthsOfYear.add("Apr");
        MonthsOfYear.add("May");
        MonthsOfYear.add("Jun");
        MonthsOfYear.add("Jul");
        MonthsOfYear.add("Aug");
        MonthsOfYear.add("Sep");
        MonthsOfYear.add("Oct");
        MonthsOfYear.add("Nov");
        MonthsOfYear.add("Dec");

        typeface = Typeface.DEFAULT_BOLD;
    }

    public View getIncomeVsExpLineGraph()
    {
        Log.d("target", "getIncomeVsExpLineGraph method is called");
        //this method returns a bar graph which contains two bars under each month one representing income and the other representing expenditure
        //this is achieved by creating two data sets, one for income and the other for expenditure.
        ArrayList<Double> tempIncomeList = lineHelper.getTotalIncomeForEachMonth();
        ArrayList<Double> tempExpList = lineHelper.getTotalExpForEachMonth();

        ArrayList<Entry> incomeEntries = new ArrayList<>();
        for(int i=0; i<tempIncomeList.size(); i++)
        {
            String tempString = String.valueOf(tempIncomeList.get(i));
            Float temp = Float.parseFloat(tempString);
            incomeEntries.add(new BarEntry(temp,i));
        }

        ArrayList<Entry> expEntries = new ArrayList<>();
        for(int i=0; i<tempExpList.size(); i++)
        {
            String tempString = String.valueOf(tempExpList.get(i));
            Float temp = Float.parseFloat(tempString);
            expEntries.add(new BarEntry(temp,i));
        }

        //make two data set objects
        LineDataSet incomeDataSet = new LineDataSet(incomeEntries,"Income");

        incomeDataSet.setColor(Color.WHITE);
        incomeDataSet.setCircleColor(Color.WHITE);
        incomeDataSet.setCircleColorHole(Color.WHITE);
        incomeDataSet.setCircleSize(3f);
        incomeDataSet.setLineWidth(1.75f);
        incomeDataSet.setHighLightColor(Color.WHITE);
        incomeDataSet.setDrawValues(false);

        LineDataSet expDataSet = new LineDataSet(expEntries,"Expenditure");
        expDataSet.setColor(Color.RED);
        expDataSet.setColor(Color.WHITE);
        expDataSet.setCircleColorHole(Color.WHITE);
        expDataSet.setCircleSize(3f);
        expDataSet.setLineWidth(1.75f);
        expDataSet.setCircleColors(ColorTemplate.JOYFUL_COLORS);
        expDataSet.setValueTypeface(typeface);
        expDataSet.setHighLightColor(Color.WHITE);
        expDataSet.setDrawValues(false);


        //make a DataSets object
        ArrayList<LineDataSet> dataSets = new ArrayList<>();
        dataSets.add(incomeDataSet);
        dataSets.add(expDataSet);

        //Create the LineData object
        LineData lineData = new LineData(MonthsOfYear,dataSets);
        lineData.setValueTypeface(typeface);

        //make the BarChart object
        LineChart lineChart = new LineChart(linePlotterContext);
        lineChart.setData(lineData);
        lineChart.setDescription("Income vs Expenditure");
        lineChart.animateY(2000);
        lineChart.setNoDataTextDescription("You need to provide data for the chart");//text to be displayed when there is no data to be plotted
        lineChart.setDrawGridBackground(false);//No background grid for the chart
        lineChart.setBorderColor(Color.WHITE);
        lineChart.setGridBackgroundColor(Color.TRANSPARENT);
        lineChart.setDrawBorders(false);

        Legend l = lineChart.getLegend();
        l.setEnabled(false);
        lineChart.setBackgroundColor(Color.RED);
        makeSecondBarChart(lineData);
        return lineChart;
    }

    private void makeSecondBarChart(LineData lineData)
    {
        LineChart secondLineChart = new LineChart(linePlotterContext);
        secondLineChart.setData(lineData);
        secondLineChart.setDescriptionPosition(0, 0);
        secondLineChart.animateY(2000);
        FullStatsActivity.showThis = secondLineChart;
    }
}
