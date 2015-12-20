package com.giridharkarnik.moneymanager.dialogFragmentClasses;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.giridharkarnik.moneymanager.HelperMethodsClasses.HelperMethods;
import com.giridharkarnik.moneymanager.R;
import com.giridharkarnik.moneymanager.busStand.BusStand;
import com.giridharkarnik.moneymanager.commObjects.BudgetUpdateObject;
import com.giridharkarnik.moneymanager.commObjects.HolyObject;
import com.github.clans.fab.FloatingActionButton;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.Calendar;

import de.greenrobot.event.EventBus;

public class AddBudgetDialogFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{

    MaterialEditText budgetTitleFDEditText,budgetAmountDFEditText;
    TextView dateMonthBudgetSD,yearBudgetSD,dateMonthBudgetED,yearBudgetED;
    FloatingActionButton budgetDFOkFAB,budgetDFCancelFAB;
    android.support.v7.widget.CardView startingDateCardView,endingDateCardView;

    private static final String DATEPICKER_TAG = "datepicker";
    private boolean startDatePicked = false, endDatePicked = false;
    private boolean clickedFromDate = false;
    private long startDate,endDate;
    private DatePickerDialog datePickerDialog;
    private HelperMethods addBudgetDialogFragmentHelperMethods;
    private Calendar startCalendar, endCalendar;
    private int startDay, startMonth, startYear, endDay, endMonth, endYear;
    private boolean isInEditMode = false;
    private int budgetId;
    private String oldBudgetName;
    private double oldTotalBudgetAmount,oldRemainingAmount;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.budget_dialog_fragment_layout,container,false);

        getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        addBudgetDialogFragmentHelperMethods = new HelperMethods(getActivity().getApplicationContext());

        budgetTitleFDEditText = (MaterialEditText)rootView.findViewById(R.id.budgetTitleFDEditText);
        budgetAmountDFEditText = (MaterialEditText)rootView.findViewById(R.id.budgetAmountDFEditText);
        budgetTitleFDEditText.setErrorColor(getResources().getColor(R.color.materialRed));
        budgetAmountDFEditText.setErrorColor(getResources().getColor(R.color.materialRed));
        dateMonthBudgetSD = (TextView)rootView.findViewById(R.id.dateMonthBudgetSD);
        yearBudgetSD = (TextView)rootView.findViewById(R.id.yearBudgetSD);
        dateMonthBudgetED = (TextView)rootView.findViewById(R.id.dateMonthBudgetED);
        yearBudgetED = (TextView)rootView.findViewById(R.id.yearBudgetED);
        budgetDFOkFAB = (FloatingActionButton)rootView.findViewById(R.id.budgetDFOkFAB);
        budgetDFCancelFAB = (FloatingActionButton)rootView.findViewById(R.id.budgetDFCancelFAB);

        startingDateCardView = (CardView)rootView.findViewById(R.id.startingDateCardView);
        endingDateCardView = (CardView)rootView.findViewById(R.id.endingDateCardView);

        //get the arguments passed to the fragment
        isInEditMode = getArguments().getBoolean("isInEditMode");



        if(isInEditMode)
        {
            acceptAdditionalArguments();
        }

        prepareCalendarCards();

        budgetDFOkFAB.setOnClickListener(listener);
        budgetDFCancelFAB.setOnClickListener(listener);
        startingDateCardView.setOnClickListener(listener);
        endingDateCardView.setOnClickListener(listener);

        BusStand.getInstance().register(AddBudgetDialogFragment.class);

        return rootView;
    }
    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId())
            {
                case R.id.budgetDFOkFAB:
                {
                    String budgetTitle = budgetTitleFDEditText.getText().toString();
                    String budgetAmount = budgetAmountDFEditText.getText().toString();

                    if(budgetTitle.isEmpty())
                    {
                        budgetTitleFDEditText.setError("Please enter a valid title for the Budget");                  }
                    else
                    {
                        if(budgetAmount.isEmpty())
                        {
                            budgetAmountDFEditText.setError("Enter a valid budget amount");
                        }
                        else
                        {
                            if(startDatePicked)
                            {
                                if(endDatePicked)
                                {
                                    if(isInEditMode)
                                    {
                                        Double budgetAmountDouble = Double.parseDouble(budgetAmount);
                                        //calculate remainingAmount for the mainBudgetObject and see if the new total amount is less than the already allocated amount,
                                        //if so issue a warning under the material EditText
                                        if(budgetAmountDouble < oldRemainingAmount)
                                        {
                                            budgetAmountDFEditText.setError("Budget amount is less than already allocated amount, Please increase the budget amount");
                                        }
                                        else {
                                            addBudgetDialogFragmentHelperMethods.updateEntireBudget(budgetId, oldBudgetName, budgetTitle, oldTotalBudgetAmount, budgetAmountDouble, oldRemainingAmount, startDate, endDate);
                                            //int budgetId, String oldBudgetName, String budgetName,double oldTotalBudgetAmount, double newTotalBudgetAmount, double oldRemainingAmount, double dateFrom, double dateTill
                                            Toast.makeText(getActivity().getApplicationContext(), "Budget is updated", Toast.LENGTH_SHORT).show();

                                            EventBus.getDefault().post(new BudgetUpdateObject());//To update the ListView
                                            dismiss();
                                        }
                                    }
                                    else {
                                        //public void setBudget(String budgetName, double totalBudgetAmount, double dateFrom, double dateTill)
                                        Double budgetAmountDouble = Double.parseDouble(budgetAmount);
                                        addBudgetDialogFragmentHelperMethods.setBudget(budgetTitle, budgetAmountDouble, startDate, endDate);

                                        EventBus.getDefault().post(new BudgetUpdateObject());
                                        Log.d("new", "Event happened in the DialogFragment");
                                        dismiss();
                                    }
                                }
                                else
                                {
                                    Toast.makeText(getActivity().getApplicationContext(), "Enter the budget's finish date", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else
                            {
                                Toast.makeText(getActivity().getApplicationContext(), "Enter the budget's start date", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    break;
                }
                case R.id.budgetDFCancelFAB:
                {
                    EventBus.getDefault().post(new HolyObject());
                    Log.d("GreenRobot","Event happened in the DialogFragment");
                    dismiss();
                    break;
                }
                case R.id.startingDateCardView:
                {
                    clickedFromDate = true;
                    showDatePicker();
                    break;
                }
                case R.id.endingDateCardView:
                {
                    clickedFromDate = false;
                    showDatePicker();
                    break;
                }
            }
        }
    };

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
        if(clickedFromDate)
        {
            startCalendar.set(year, month, day);
            startDate = startCalendar.getTimeInMillis();
            fixStartCalendar();
        }
        else
        {
            endCalendar.set(year, month, day);
            endDate = endCalendar.getTimeInMillis();

            endDatePicked = true;
            fixEndCalendar();
        }
    }

    public void showDatePicker() {
        if(clickedFromDate)
        {
            datePickerDialog = DatePickerDialog.newInstance(this, startYear, startMonth, startDay);
        }
        else
        {
            datePickerDialog = DatePickerDialog.newInstance(this, endYear, endMonth, endDay);
        }
        datePickerDialog.setYearRange(1985, 2037);
        datePickerDialog.show(getFragmentManager(), DATEPICKER_TAG);
    }

    private void acceptAdditionalArguments()
    {
        /*args.putString("budgetName",mainBudgetObject.getBudgetName());
        args.putDouble("totalBudgetAmount",mainBudgetObject.getTotalAmount());
        args.putLong("dateFrom",mainBudgetObject.getDateFrom());
        args.putLong("dateTill",mainBudgetObject.getDateTill());
        args.putInt("budgetId",mainBudgetObject.getBudgetId());*/
        oldBudgetName = getArguments().getString("budgetName");
        oldTotalBudgetAmount = getArguments().getDouble("totalBudgetAmount");
        oldRemainingAmount = getArguments().getDouble("remainingAmount");
        budgetTitleFDEditText.setText(getArguments().getString("budgetName"));
        budgetAmountDFEditText.setText("" + getArguments().getDouble("totalBudgetAmount"));
        startDate = getArguments().getLong("dateFrom");
        endDate = getArguments().getLong("dateTill");
        budgetId = getArguments().getInt("budgetId");
    }

    private void prepareCalendarCards()
    {
        startCalendar = Calendar.getInstance();
        endCalendar = Calendar.getInstance();

        if(isInEditMode) {
            //fix start Calendar
            startCalendar.setTimeInMillis(startDate);

            //fix end Calendar
            endCalendar.setTimeInMillis(endDate);
        }

        fixStartCalendar();
        fixEndCalendar();
    }

    private void fixStartCalendar()
    {
        startDay = startCalendar.get(Calendar.DAY_OF_MONTH);
        startMonth = startCalendar.get(Calendar.MONTH);
        startYear = startCalendar.get(Calendar.YEAR);

        dateMonthBudgetSD.setText(startDay + " " + getResources().getStringArray(R.array.MonthsOfYear)[startMonth]);
        yearBudgetSD.setText(startYear + "");
        startDatePicked = true;

        startDate = startCalendar.getTimeInMillis();
    }

    private void fixEndCalendar()
    {
        endDay = endCalendar.get(Calendar.DAY_OF_MONTH);
        endMonth = endCalendar.get(Calendar.MONTH);
        endYear = endCalendar.get(Calendar.YEAR);

        dateMonthBudgetED.setText(endDay + " " + getResources().getStringArray(R.array.MonthsOfYear)[endMonth]);
        yearBudgetED.setText(endYear + "");

        endDate = endCalendar.getTimeInMillis();
    }
}

