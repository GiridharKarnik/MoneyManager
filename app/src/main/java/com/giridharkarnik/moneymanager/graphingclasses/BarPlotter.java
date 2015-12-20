package com.giridharkarnik.moneymanager.graphingclasses;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;

import com.giridharkarnik.moneymanager.HelperMethodsClasses.HelperMethods;
import com.giridharkarnik.moneymanager.modelobjects.FullStatsActivity;
import com.giridharkarnik.moneymanager.modelobjects.MoneyObject;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class BarPlotter {

    Context StatsContext;
    HelperMethods barHelper;
    ArrayList<String> MonthsOfYear;

    public BarPlotter(Context c) {
        StatsContext = c;
        barHelper = new HelperMethods(StatsContext);

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

    public View getIncomePerFieldBarChart(Integer MonthInt)
    {
        //Create DataSet
        //First make an ArrayList of Events(Individual Entries in the graph to be plotted)
        /*
        1.get all the Income Fields
        2.get the count of Income Fields
        3.get the incomes registered per field
        4.add all the incomes of a respective field*/
        //Create Labels for X-Axis
        ArrayList<String> FieldLabels = barHelper.getIncomeFields();

        //Create the DataSet, i.e., individual entries in the graph
        ArrayList<MoneyObject> tempObjects = barHelper.getCurrentMonthIncomeObjects(MonthInt);
        ArrayList<Double> incomeDataSet = barHelper.getIncomePerField(tempObjects);

        BarChart tempChart = setChart(FieldLabels, incomeDataSet, "Income Distribution");

        BarChart returnChart = setCustomLegend(tempChart,FieldLabels);

        return returnChart;
    }

    public View getExpPerFieldBarChart(Integer MonthInt)
    {
        ArrayList<String> FieldLabels = barHelper.getExpFields();

        ArrayList<MoneyObject> tempObjects = barHelper.getCurrentMonthExpObjects(MonthInt);
        ArrayList<Double> incomeDataSet = barHelper.getExpPerField(tempObjects);
        return setChart(FieldLabels,incomeDataSet,"Expenditure Distribution");
    }

    public View getIncomeForEachMonthBarChart()
    {
        //prepare the DataSet, i.e., individual entries in the graph, y axis entries
        ArrayList<Double> tempDoubleList = barHelper.getTotalIncomeForEachMonth();
        return setChart(MonthsOfYear,tempDoubleList,"Income Per Month");
    }

    public View getExpForEachMonthBarChart()
    {
        ArrayList<Double> tempDoubleList = barHelper.getTotalExpForEachMonth();
        return setChart(MonthsOfYear,tempDoubleList,"Expenditure Per Month");
    }

    public View getIncomeVsExpBarChart()
    {
        //this method returns a bar graph which contains two bars under each month one representing income and the other representing expenditure
        //this is achieved by creating two data sets, one for income and the other for expenditure.
        ArrayList<Double> tempIncomeList = barHelper.getTotalIncomeForEachMonth();
        ArrayList<Double> tempExpList = barHelper.getTotalExpForEachMonth();

        ArrayList<BarEntry> incomeEntries = new ArrayList<>();
        for(int i=0; i<tempIncomeList.size(); i++)
        {
            String tempString = String.valueOf(tempIncomeList.get(i));
            Float temp = Float.parseFloat(tempString);
            incomeEntries.add(new BarEntry(temp,i));
        }

        ArrayList<BarEntry> expEntries = new ArrayList<>();
        for(int i=0; i<tempExpList.size(); i++)
        {
            String tempString = String.valueOf(tempExpList.get(i));
            Float temp = Float.parseFloat(tempString);
            expEntries.add(new BarEntry(temp,i));
        }

        //make two data set objects
        BarDataSet incomeDataSet = new BarDataSet(incomeEntries,"Income");
        incomeDataSet.setColor(Color.GREEN);
        BarDataSet expDataSet = new BarDataSet(expEntries,"Expenditure");
        expDataSet.setColor(Color.RED);

        //make a DataSets object
        ArrayList<BarDataSet> dataSets = new ArrayList<>();
        dataSets.add(incomeDataSet);
        dataSets.add(expDataSet);

        //create the BarData object
        BarData barData = new BarData(MonthsOfYear,dataSets);

        //make the BarChart object
        BarChart barChart = new BarChart(StatsContext);
        barChart.setData(barData);
        barChart.setDescription("Income vs Expenditure");
        barChart.animateY(2000);

        makeSecondBarChart(barData);
        return barChart;
    }

    public View getExpSetForThisMonth(int month,int year)
    {
        ArrayList<Double> expArrayList = barHelper.getExpSetForAMonth(month, year);
        Log.d("tar","The size of the list is " + expArrayList.size());
        for(int i=0 ; i< expArrayList.size();i++)
        {
            Log.d("tar","The contents of expArrayList are " + expArrayList.get(i));
        }

        ArrayList<String> X_List = new ArrayList<>();
        for(int i=0; i<expArrayList.size(); i++)
        {
            int j = i+1;
            X_List.add("" + j + "," + MonthsOfYear.get(month));
        }
        return setChart(X_List,expArrayList,"Month's Expense");
    }
    public BarChart setChart(ArrayList<String> X_List,ArrayList<Double> Y_List,String chartTitle)
    {
        //Step 1: Make an ArrayList of BarEntry objects for y-axis co-ordinates of each chart entry.
        ArrayList<BarEntry> entries = new ArrayList<>();
        for(int i=0; i<Y_List.size(); i++)
        {
            String tempString = String.valueOf(Y_List.get(i));
            Float temp = Float.parseFloat(tempString);
            entries.add(new BarEntry(temp,i));
        }

        //Step 2: Make the BarDataSet object using the ArrayList<BarEntry>.
        BarDataSet barDataSet = new BarDataSet(entries,chartTitle);
        barDataSet.setColors(ColorTemplate.JOYFUL_COLORS);

        //Step 3: Make the BarChart Object.
        BarChart barChart = new BarChart(StatsContext);

        //Step 4: Make the BarData Object
        BarData barData = new BarData(X_List,barDataSet);

        //Step 5: Set the BarData object to the BarChart object.
        barChart.setData(barData);
        barChart.setDescriptionPosition(0, 0);

        barChart.animateY(1000);

        makeSecondBarChart(barData);

        return barChart;
    }

    private void makeSecondBarChart(BarData barData)
    {
        BarChart secondBarChart = new BarChart(StatsContext);
        secondBarChart.setData(barData);
        secondBarChart.setDescriptionPosition(0, 0);

        FullStatsActivity.showThis = secondBarChart;
    }

    private BarChart setCustomLegend(BarChart barChart,ArrayList<String> FieldLabels)
    {
        Legend l = barChart.getLegend();

        String[] fieldLabelsArray = new String[FieldLabels.size()];
        int[] joyFullColorTemplate = ColorTemplate.JOYFUL_COLORS;

        int[] customColors = new int[FieldLabels.size()];

        for(int i=0; i<FieldLabels.size(); i++)
        {
            fieldLabelsArray[i] = FieldLabels.get(i);
        }

        for(int j=0; j<FieldLabels.size(); j++)
        {
            customColors[j] = joyFullColorTemplate[j];
        }

        l.setCustom(customColors,fieldLabelsArray);
        return barChart;
    }
}
