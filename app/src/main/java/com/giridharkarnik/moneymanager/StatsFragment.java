package com.giridharkarnik.moneymanager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.giridharkarnik.moneymanager.adapters.ChoiceSpinnerAdapter;
import com.giridharkarnik.moneymanager.adapters.GraphSpinnerAdapter;
import com.giridharkarnik.moneymanager.adapters.MonthSpinnerAdapter;
import com.giridharkarnik.moneymanager.customViewClasses.CustomSpinner;
import com.giridharkarnik.moneymanager.customViewClasses.NextButton;
import com.giridharkarnik.moneymanager.graphingclasses.BarPlotter;
import com.giridharkarnik.moneymanager.graphingclasses.LinePlotter;
import com.giridharkarnik.moneymanager.graphingclasses.PiePlotter;
import com.giridharkarnik.moneymanager.modelobjects.FullStatsActivity;
import com.giridharkarnik.moneymanager.modelobjects.StatsInformationObject;
import com.nineoldandroids.animation.ArgbEvaluator;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class StatsFragment extends Fragment{

    CustomSpinner month_spinner, chartInfoSpinner, chartNatureSpinner;
    AdapterView.OnItemSelectedListener chartNatureListener,chartInfoListener;
    String[] infoVarieties,infoVarieties2;
    NextButton nextButton,prevButton;

    PiePlotter localPiePlotter;
    BarPlotter localBarPlotter;
    LinePlotter localLinePlotter;
    Thread radiateThread;

    SlidingUpPanelLayout fieldSlidingPanel;
    ImageView enlargeImageView;
    ViewGroup graphingArea;
    View currentStatsView;
    StatsInformationObject statsInformationObject;

    ChoiceSpinnerAdapter localChoiceSpinnerAdapter;
    MonthSpinnerAdapter monthSpinnerAdapter;
    FrameLayout dimmingLayout;
    FragmentManager fm;

    int chartNature, chartInfo, chartType;

    ArrayList<String> infoChartTypesIncome,infoChartTypes, infoChartTypesMisc;
    String[] Income_charts,Exp_charts;

    int arrowStates = 0; //0 - show only the next button, 1 -show next and previous buttons, 2 - show only previous button

    //get the current month as per the system time.
    int monthPosition = Calendar.getInstance().get(Calendar.MONTH);

    CustomSpinner.ClickCommunicator clickCommunicator;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_stats, container, false);

        final Context context = getActivity().getApplicationContext();

        radiateThread = new Thread();

        //final ViewGroup myLayout = (ViewGroup)v.findViewById(R.id.relativeLayout);
        chartNatureSpinner = (CustomSpinner)v.findViewById(R.id.type_spinner);
        chartInfoSpinner = (CustomSpinner)v.findViewById(R.id.graph_spinner);
        month_spinner = (CustomSpinner)v.findViewById(R.id.month_spinner);
        graphingArea = (ViewGroup)v.findViewById(R.id.graphing_area);
        nextButton = (NextButton)v.findViewById(R.id.nextButton);
        prevButton = (NextButton)v.findViewById(R.id.prevButton);
        fieldSlidingPanel = (SlidingUpPanelLayout)v.findViewById(R.id.fieldSlidingLayout);
        enlargeImageView = (ImageView)v.findViewById(R.id.enlarge);

        dimmingLayout = (FrameLayout)v.findViewById(R.id.dimmingLayout);

        infoVarieties = getResources().getStringArray(R.array.Type_of_charts);
        infoVarieties2 = getResources().getStringArray(R.array.Miscellaneous_Charts);

        localBarPlotter = new BarPlotter(getActivity().getApplicationContext());
        localPiePlotter = new PiePlotter(getActivity().getApplicationContext());
        localLinePlotter = new LinePlotter(getActivity().getApplicationContext());

        fm = getFragmentManager();
       ///*****************************************************************\\
        infoChartTypesMisc = new ArrayList<>();
        infoChartTypesMisc.addAll(Arrays.asList(infoVarieties2));

        Exp_charts = getResources().getStringArray(R.array.Exp_Charts);
        infoChartTypes = new ArrayList<>();
        infoChartTypes.addAll(Arrays.asList(Exp_charts));//copy the contents of the Exp_charts array into the ArrayList<String> infoChartTypes

        Income_charts = getResources().getStringArray(R.array.Income_Charts);
        infoChartTypesIncome = new ArrayList<>();
        infoChartTypesIncome.addAll(Arrays.asList(Income_charts));//add the contents of the array to the ArrayList<String>
        ///*****************************************************************\\

        //animations used in the fragment
        com.nineoldandroids.animation.ObjectAnimator radiateAnimator = com.nineoldandroids.animation.ObjectAnimator.ofFloat(nextButton,"alpha",1.0f,0.1f,1.0f);
        radiateAnimator.setDuration(2500);
        radiateAnimator.setRepeatCount(Animation.INFINITE);
        radiateAnimator.start();

        com.nineoldandroids.animation.ObjectAnimator radiateAnimator2 = com.nineoldandroids.animation.ObjectAnimator.ofFloat(prevButton,"alpha",1.0f,0.1f,1.0f);
        radiateAnimator2.setDuration(2500);
        radiateAnimator2.setRepeatCount(Animation.INFINITE);
        radiateAnimator2.start();

        enlargeImageView.setImageAlpha(54);

        final com.nineoldandroids.animation.ObjectAnimator dimBackground = com.nineoldandroids.animation.ObjectAnimator.ofObject(dimmingLayout,
                "backgroundColor",
                new ArgbEvaluator(),
                0x00000000,
                0x70000000);
        dimBackground.setDuration(400);

        final com.nineoldandroids.animation.ObjectAnimator normalBackground = com.nineoldandroids.animation.ObjectAnimator.ofObject(dimmingLayout,
                "backgroundColor",
                new ArgbEvaluator(),
                0x70000000,
                0x00000000);
        normalBackground.setDuration(400);

        //interface with the custom spinner
        clickCommunicator = new CustomSpinner.ClickCommunicator() {
            @Override
            public void isOpened() {
                dimBackground.start();
            }

            @Override
            public void isClosed() {
                normalBackground.start();
            }
        };

        //onItemSelectedListeners for CustomSpinner Class
        chartNatureListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l)
            {
                switch (position) {
                    case 0: {
                        //set up indicators first
                        chartNature = 0; //signifies Income Charts
                        //chartNatureSpinner is set to Income Charts
                        //reset the chartInfoSpinner
                        Log.d("special", "chart nature is Income charts");
                        setChartInfoSpinner(infoChartTypesIncome); //private method which sets the adapter to the chartInfoSpinner
                        chartInfoSpinner.setSelection(0);
                        break;
                    }
                    case 1: {
                        //chartNatureSpinner is set to Expenditure Charts
                        //set up indicators first
                        chartNature = 1; //signifies Expenditure Charts
                        //reset the chartInfoSpinner
                        setChartInfoSpinner(infoChartTypes); //private method which sets the adapter to the chartInfoSpinner
                        chartInfoSpinner.setSelection(0);
                        break;
                    }
                    case 2: {
                        //chartNatureSpinner is set to Balance Charts
                        //set up indicators first
                        chartNature = 2; //signifies Balance Charts
                        //reset the chartInfoSpinner
                        setChartInfoSpinner(infoChartTypesMisc); //private method which sets the adapter to the chartInfoSpinner
                        chartInfoSpinner.setSelection(0);
                        break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        };

        chartInfoListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l)
            {
                Log.d("special", "chartInfoSpinner onItemSelected method is called with position " + position);
                switch (position) {
                    case 0:
                    {
                        Log.d("special","chart info type is field distribution");
                        Log.d("special","chartType is " + chartType);
                        //set up indicators first
                        chartInfo = 0; //signifies field distribution
                        //Case where the spinner is set to Distribution among fields per month
                        //if month spinner view is not visible make it visible
                        monthSpinnerState(true); //private method to set the alter the visibility of the month_spinner
                        switch (chartNature) {
                            case 0: {
                                Log.d("special","going to show IncomeCharts");
                                getIncomePerFieldCharts();
                                break;
                            }
                            case 1: {
                                Log.d("special","going to show ExpCharts");
                                getExpPerFieldCharts();
                                break;
                            }
                            case 2: {
                                Log.d("special", "chart nature is Balance Charts ");
                                getIncomeVsExpCharts();
                                break;
                            }
                        }
                        //Make the StatsInformationActivity Object
                        break;
                    }
                    case 1:
                    {
                        Log.d("special","chart info type is monthly distribution");
                        Log.d("special","chartType is " + chartType);
                        //set up indicators first
                        chartInfo = 1; //signifies Monthly
                        //Case where the spinner is set to Monthly Distribution
                        //first hide the spinner which shows the list of months as it is unnecessary here
                        monthSpinnerState(false); //private method to set the alter the visibility of the month_spinner

                        switch (chartNature)
                        {
                            case 0:
                            {
                                Log.d("special","going to show IncomeCharts");
                                getIncomeForEachMonthCharts();
                                break;
                            }
                            case 1: {
                                Log.d("special","going to show ExpCharts");
                                getExpForEachMonthCharts();
                                break;
                            }
                            case 2: {
                                Log.d("special","chart nature is Balance Charts ");
                                getIncomeVsExpCharts();
                                break;
                            }
                        }
                        break;
                    }
                    case 2:
                    {
                        //only expenditure charts have three info types, so we can be sure that the chartNature is Expenditure Charts
                        chartInfo = 2;
                        getMonthExpSetBarChart();

                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        };

        //set up everything related to custom spinner
        month_spinner.setClickCommunicator(clickCommunicator);

        chartNatureSpinner.setClickCommunicator(clickCommunicator);
        chartNatureSpinner.setOnItemSelectedListener(chartNatureListener);
        chartNatureSpinner.setUpListener(chartNatureListener);

        chartInfoSpinner.setClickCommunicator(clickCommunicator);
        chartInfoSpinner.setOnItemSelectedListener(chartInfoListener);
        chartInfoSpinner.setUpListener(chartInfoListener);

        final ArrayList<String> types_of_charts = new ArrayList<>();
        types_of_charts.addAll(Arrays.asList(infoVarieties));


        //setting adapter for chartNatureSpinner
        localChoiceSpinnerAdapter = new ChoiceSpinnerAdapter(context,types_of_charts);
        chartNatureSpinner.setAdapter(localChoiceSpinnerAdapter);
        chartNatureSpinner.setSelection(0);

        ArrayList<String> MonthsOfYearArrayList = new ArrayList<>();
        String[] MonthsOfYearArray = getResources().getStringArray(R.array.MonthsOfYear);

        MonthsOfYearArrayList.addAll(Arrays.asList(MonthsOfYearArray));

        //setting adapter for month spinner
        monthSpinnerAdapter = new MonthSpinnerAdapter(getActivity().getApplicationContext(),MonthsOfYearArrayList);
        month_spinner.setAdapter(monthSpinnerAdapter);
        month_spinner.setSelection(monthPosition);

        month_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
              //Display the month chart related to only to the month selected in the spinner

                Log.d("special","month spinner is selected, month is " + position);
                monthPosition = position;

                chartInfoListener.onItemSelected(null,null,chartInfo,0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                switch (chartNature)
                {
                    case 0:
                    {
                        /*for income charts there are two info types, and there are only two charts for each info type, Bar and Pie*/
                        chartType = 1;
                        arrowStates = 2; //show only back arrow
                        break;
                    }
                    case 1:
                    {
                        /*for exp charts there are three info types, and there are two types of charts for the first two info types and one chart for the third info type*/
                        Log.d("tar","next button is clicked, nature is exp");
                        chartType = 1;
                        arrowStates = 2;
                        break;
                    }
                    case 2:
                    {
                        /*for miscellaneous charts there are two info types, and two chart varieties for each info type*/
                        chartType = 1;
                        arrowStates = 2;
                        break;
                    }
                }
                chartInfoListener.onItemSelected(null,null,chartInfo,0);
            }
        });

        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(chartNature == 2)
                {
                    //this is for miscellaneous charts whose behaviour is different than normal
                    arrowStates = 0;
                    chartType = 0;//for bar chart
                }
                else {
                    if (chartInfo == 0) {
                        //on previous button click the bar graph should be shown, so
                        arrowStates = 0;
                        chartType = 0;//for bar chart
                    } else {
                        switch (arrowStates) {
                            //NOTE : case 0: will never arise since the prev button won't be shown during the BarChart
                            case 1: {
                                //in first screen chartType is Bar Chart, has to show Pie Chart on click
                                chartType = 0; // for barChart
                                arrowStates = 0;
                                break;
                            }
                            case 2: {
                                //in first screen chartType is Pie Chart, has to show Line Chart on click
                                chartType = 1; // for pieChart
                                arrowStates = 1;
                                break;
                            }
                        }
                    }
                }
                chartInfoListener.onItemSelected(null,null,chartInfo,0);
            }
        });

        enlargeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                //post an event the bus
                Intent intent = new Intent(getActivity(), FullStatsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("customObject",statsInformationObject);
                intent.putExtra("bundle", bundle);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        fieldSlidingPanel.setPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffSet) {
                Log.d("panel", "sliding, offset " + slideOffSet);


            }

            @Override
            public void onPanelCollapsed(View panel) {

            }

            @Override
            public void onPanelExpanded(View panel) {

            }

            @Override
            public void onPanelAnchored(View panel) {

            }

            @Override
            public void onPanelHidden(View panel) {

            }
        });
        return v;
    }

    private void showAssociateViews()
    {
        switch (arrowStates)
        {
            case 0:
            {
                //show only the next button
                graphingArea.addView(enlargeImageView);
                enlargeImageView.bringToFront();
                graphingArea.addView(nextButton);
                nextButton.bringToFront();
                break;
            }
            case 1:
            {
                //show next and previous button
                graphingArea.addView(enlargeImageView);
                enlargeImageView.bringToFront();
                //add the next button
                graphingArea.addView(nextButton);
                nextButton.bringToFront();
                //add the previous button
                graphingArea.addView(prevButton);
                prevButton.bringToFront();
                break;
            }
            case 2:
            {
                //show only previous button
                graphingArea.addView(enlargeImageView);
                enlargeImageView.bringToFront();
                //add the previous button
                graphingArea.addView(prevButton);
                prevButton.bringToFront();
                break;
            }
        }
    }

    private void setChartInfoSpinner(ArrayList<String> chartInfoTypes)
    {
        GraphSpinnerAdapter graphSpinnerAdapter = new GraphSpinnerAdapter(getActivity().getApplicationContext(), chartInfoTypes);
        chartInfoSpinner.setAdapter(graphSpinnerAdapter);
        chartInfoSpinner.setSelection(0);
    }

    private void monthSpinnerState(boolean showSpinner)
    {
        if(showSpinner) {
            if (month_spinner.getVisibility() == View.INVISIBLE) {
                month_spinner.setVisibility(View.VISIBLE);
            }
        }
        else
        {
            if (month_spinner.getVisibility() == View.VISIBLE) {
                month_spinner.setVisibility(View.INVISIBLE);
            }
        }
    }

    private void getIncomePerFieldCharts()
    {
        Log.d("special","chart nature is Income Charts");
        //Executed if chartNatureSpinner is set to Income Charts
        graphingArea.removeAllViews();
        if(chartType == 0)
        {
            currentStatsView = localBarPlotter.getIncomePerFieldBarChart(monthPosition);
            //show Bar Chart
        }
        else
        {
            //show Pie Chart
            currentStatsView = localPiePlotter.getIncomePerFieldPieChart(monthPosition);
        }
        //graphingArea.addView(currentStatsView);
        graphingArea.addView(currentStatsView, graphingArea.getWidth(),graphingArea.getHeight());
        showAssociateViews();
    }

    private void getIncomeForEachMonthCharts()
    {
        Log.d("special","chart nature is Income Charts");
        //Executed if chartNatureSpinner is set to Income Charts
        graphingArea.removeAllViews();
        if(chartType == 0)
        {
            currentStatsView = localBarPlotter.getIncomeForEachMonthBarChart();
            //show Bar Chart
        }
        else if(chartType == 1)
        {
            //show Pie Chart
            currentStatsView = localPiePlotter.getIncomeForEachMonthPieChart();
        }
        else
        {
            //show Line Chart
            //show Line Chart
            Toast.makeText(getActivity().getApplicationContext(),"LineChart will be shown now",Toast.LENGTH_SHORT).show();
        }
        //graphingArea.addView(currentStatsView);
        graphingArea.addView(currentStatsView, graphingArea.getWidth(),graphingArea.getHeight());
        showAssociateViews();
    }

    private void getExpPerFieldCharts()
    {
        Log.d("special","chart nature is Exp Charts ");
        //Expenditure Charts should be shown
        graphingArea.removeAllViews();
        if(chartType == 0)
        {
            //show Bar Chart
            Log.d("special","going to call ExpPerFieldBarChart with montPosition " + monthPosition);
            currentStatsView = localBarPlotter.getExpPerFieldBarChart(monthPosition);
        }
        else
        {
            //show Pie Chart
            Log.d("special","going to call getExpForEachMonthPieChart with montPosition " + monthPosition);
            currentStatsView = localPiePlotter.getExpPerFieldPieChart(monthPosition);
        }

       // graphingArea.addView(currentStatsView);
        graphingArea.addView(currentStatsView, graphingArea.getWidth(),graphingArea.getHeight());
        showAssociateViews();
    }

    private void getExpForEachMonthCharts()
    {
        Log.d("special","chart nature is Exp Charts ");
        //Expenditure Charts should be shown
        graphingArea.removeAllViews();
        if(chartType == 0)
        {
            //show Bar Chart
            Log.d("special","going to call ExpPerFieldBarChart with montPosition " + monthPosition);
            currentStatsView = localBarPlotter.getExpForEachMonthBarChart();
        }
        else if(chartType == 1)
        {
            //show Pie Chart
            Log.d("special","going to call getExpForEachMonthPieChart with montPosition " + monthPosition);
            currentStatsView = localPiePlotter.getExpForEachMonthPieChart();
        }
        else
        {
            //show Line Chart
            Toast.makeText(getActivity().getApplicationContext(),"LineChart will be shown now",Toast.LENGTH_SHORT).show();
        }

       // graphingArea.addView(currentStatsView);
        graphingArea.addView(currentStatsView, graphingArea.getWidth(),graphingArea.getHeight());
        showAssociateViews();
    }

    private void getIncomeVsExpCharts()
    {
        Log.d("target", "getIncomeVsExpCharts method is called");
        graphingArea.removeAllViews();
        if(chartType == 0)
        {
            //show bar chart
            currentStatsView = localBarPlotter.getIncomeVsExpBarChart();
        }
        else
        {
            Log.d("target","Line chart should be shown");
            //show line graph
            currentStatsView = localLinePlotter.getIncomeVsExpLineGraph();
        }
        //graphingArea.addView(currentStatsView);
        graphingArea.addView(currentStatsView, graphingArea.getWidth(),graphingArea.getHeight());
        showAssociateViews();
    }

    private void getMonthExpSetBarChart() {
        Log.d("special","getMonthExpSetBarChart method is called");
        graphingArea.removeAllViews();
        if(chartType == 0)
        {
            int year = Calendar.getInstance().get(Calendar.YEAR);
            Log.d("tar","this year is " + year);
            //show bar chart
            currentStatsView = localBarPlotter.getExpSetForThisMonth(monthPosition,Calendar.getInstance().get(Calendar.YEAR));
            graphingArea.addView(currentStatsView, graphingArea.getWidth(),graphingArea.getHeight());
            showAssociateViews();
        }
    }
}


