package com.giridharkarnik.moneymanager.graphingclasses;

import android.content.Context;
import android.graphics.Color;
import android.view.View;

import com.giridharkarnik.moneymanager.HelperMethodsClasses.HelperMethods;
import com.giridharkarnik.moneymanager.modelobjects.FullStatsActivity;
import com.giridharkarnik.moneymanager.modelobjects.MoneyObject;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.PercentFormatter;

import java.util.ArrayList;

public class PiePlotter {

    Context piePlotterContext;
    HelperMethods pieHelperMethods;
    ArrayList<String> MonthsOfYear;


    public PiePlotter(Context c) {
        piePlotterContext = c;
        pieHelperMethods = new HelperMethods(piePlotterContext);
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
    }

    public View getIncomePerFieldPieChart(Integer month)
    {
        ArrayList<String> fieldLabels = pieHelperMethods.getIncomeFields();

        ArrayList<MoneyObject> tempObjects = pieHelperMethods.getCurrentMonthIncomeObjects(month);
        ArrayList<Double> incomeDataSet = pieHelperMethods.getIncomePerField(tempObjects);

        return setPieChart(fieldLabels,incomeDataSet,"% income per field");
    }

    public View getExpPerFieldPieChart(Integer month)
    {
        ArrayList<String> fieldLabels = pieHelperMethods.getExpFields();

        ArrayList<MoneyObject> tempObjects = pieHelperMethods.getCurrentMonthExpObjects(month);
        ArrayList<Double> expDataSet = pieHelperMethods.getExpPerField(tempObjects);

        return setPieChart(fieldLabels,expDataSet,"% expenditure per field");
    }

    public View getIncomeForEachMonthPieChart()
    {
        ArrayList<Double> tempDoubleList = pieHelperMethods.getTotalIncomeForEachMonth();
        return setPieChart(MonthsOfYear,tempDoubleList,"Income of Each Month");
    }

    public View getExpForEachMonthPieChart()
    {
        ArrayList<Double> tempDoubleList = pieHelperMethods.getTotalExpForEachMonth();
        return setPieChart(MonthsOfYear,tempDoubleList,"Expenditure of Each Month");
    }



    private View setPieChart(ArrayList<String> X_List,ArrayList<Double> Y_List,String chartTitle)
    {
        ArrayList<Entry> entries = new ArrayList<>();
        for(int i=0; i<Y_List.size(); i++)
        {
            String tempString = String.valueOf(Y_List.get(i));
            Float temp = Float.parseFloat(tempString);
            entries.add(new BarEntry(temp, i));
        }

        PieChart pieChart = setThingsForPieChart();

        //now create dataSets
        PieDataSet pieDataSet = new PieDataSet(entries,chartTitle);

        //instantiate pie data object now
        PieData pieData = makePieDataObject(X_List,pieDataSet);

        pieChart.setData(pieData);

        pieChart.highlightValues(null);
        pieChart.invalidate();
        makeSecondPieChart(pieData);
        return pieChart;
    }

    private void makeSecondPieChart(PieData pieData)
    {
        PieChart pieChart = setThingsForPieChart();
        pieChart.setData(pieData);
        pieChart.animateY(1500);
        FullStatsActivity.showThis = pieChart;
    }

    private PieChart setThingsForPieChart()
    {
        //method is will create a PieChart and set the various associated values related to the PieChart.
        PieChart pieChart = new PieChart(piePlotterContext);
        pieChart.setUsePercentValues(true);
        pieChart.setDescription("Income Distribution Per Income Field");
        pieChart.setDescriptionPosition(0, 0);

        //enable hole and configure
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColorTransparent(true);
        pieChart.setHoleRadius(70f);
        pieChart.setTransparentCircleRadius(10);
        pieChart.setCenterText("% Income per field");
        pieChart.setCenterTextSize(15f);

        Legend l = pieChart.getLegend();
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        //enable rotation of chart by touch
        pieChart.setRotationAngle(0);
        pieChart.setRotationEnabled(true);
        pieChart.spin(5000,0,-360f, Easing.EasingOption.EaseInOutQuad);

        return pieChart;
    }

    private PieData makePieDataObject(ArrayList<String> X_List,PieDataSet pieDataSet)
    {
        //This method will create a PieData object and will set the various values related to it.
        pieDataSet.setSliceSpace(3);
        pieDataSet.setSelectionShift(5);

        //add many colors
        ArrayList<Integer> colors = new ArrayList<>();
        for(int c: ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for(int c:ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for(int c:ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for(int c:ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for(int c:ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());
        pieDataSet.setColors(colors);

        //instantiate pie data object now
        PieData pieData = new PieData(X_List,pieDataSet);
        pieData.setValueFormatter(new PercentFormatter());
        pieData.setValueTextSize(11f);
        pieData.setValueTextColor(Color.GRAY);

        return pieData;
    }
}
