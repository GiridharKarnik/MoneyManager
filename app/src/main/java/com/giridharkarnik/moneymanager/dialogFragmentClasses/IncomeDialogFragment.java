package com.giridharkarnik.moneymanager.dialogFragmentClasses;

import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.giridharkarnik.moneymanager.HelperMethodsClasses.HelperMethods;
import com.giridharkarnik.moneymanager.R;
import com.giridharkarnik.moneymanager.adapters.FieldsSpinnerAdapter;
import com.giridharkarnik.moneymanager.busStand.BusStand;
import com.giridharkarnik.moneymanager.commObjects.RefreshObject;
import com.giridharkarnik.moneymanager.customViewClasses.CustomSpinner;
import com.giridharkarnik.moneymanager.modelobjects.FieldObject;
import com.github.clans.fab.FloatingActionButton;
import com.nineoldandroids.animation.ArgbEvaluator;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.Calendar;


public class IncomeDialogFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{


    com.rengwuxian.materialedittext.MaterialEditText incomeDialogFragmentEditText,expDialogFragmentEditText;
    com.giridharkarnik.moneymanager.customViewClasses.CustomSpinner incomeDFFieldSpinner;
    android.support.v7.widget.CardView calendarCardView;
    FloatingActionButton incomeDFOkFAB,incomeDFCancelFAB;
    ImageView calendarImageView;
    TextView dayMonthTextView,yearTextView;
    String universal_title;
    HelperMethods incomeDFHelperMethods;
    DatePickerDialog datePickerDialog;
    Calendar calendar;
    boolean isIncome = true;

    com.nineoldandroids.animation.ObjectAnimator dimBackground,normalBackground;
    TransitionDrawable normalToDim,dimToNormal;
    private long currentDateInMilliSeconds;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.income_dialog_fragment,container,false);
        getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        //first get the passed arguments
        isIncome = getArguments().getBoolean("isIncome");

        incomeDFHelperMethods = new HelperMethods(getActivity().getApplicationContext());

        incomeDialogFragmentEditText = (MaterialEditText)rootView.findViewById(R.id.incomeDialogFragmentEditText);
        expDialogFragmentEditText = (MaterialEditText)rootView.findViewById(R.id.expDialogFragmentEditText);

        incomeDFFieldSpinner = (CustomSpinner)rootView.findViewById(R.id.incomeDFFieldSpinner);
        calendarCardView = (CardView)rootView.findViewById(R.id.calendarCardView);
        incomeDFOkFAB = (FloatingActionButton)rootView.findViewById(R.id.incomeDFOkFAB);
        incomeDFCancelFAB = (FloatingActionButton)rootView.findViewById(R.id.incomeDFCancelFAB);
        dayMonthTextView = (TextView)rootView.findViewById(R.id.dayMonthTextView);
        yearTextView = (TextView)rootView.findViewById(R.id.yearTextView);
        calendarImageView = (ImageView)rootView.findViewById(R.id.calendarImageView);

        prepLayout();
        setCurrentDate();

        calendarCardView.setOnClickListener(listener);
        incomeDFOkFAB.setOnClickListener(listener);
        incomeDFCancelFAB.setOnClickListener(listener);

        ArrayList<FieldObject> localFieldObjectList = getIncomeOrExpFieldObjects();

        //Otto Bus registration

        calendar = Calendar.getInstance();
        datePickerDialog = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        // Set the adapter to the spinner
        FieldsSpinnerAdapter fieldsSpinnerAdapter = new FieldsSpinnerAdapter(getActivity().getApplicationContext(),localFieldObjectList);
        incomeDFFieldSpinner.setAdapter(fieldsSpinnerAdapter);
        incomeDFFieldSpinner.callOnClick();
        incomeDFFieldSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int pos = incomeDFFieldSpinner.getSelectedItemPosition();
                FieldObject tempObjectField = (FieldObject) incomeDFFieldSpinner.getItemAtPosition(pos);
                universal_title = tempObjectField.getField_title();
                Log.d("dev", "The selected title is " + universal_title);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        dimBackground = com.nineoldandroids.animation.ObjectAnimator.ofObject(calendarCardView,
                "backgroundColor",
                new ArgbEvaluator(),
                0x00000000,
                0x70000000);
        dimBackground.setDuration(400);

        normalBackground = com.nineoldandroids.animation.ObjectAnimator.ofObject(calendarCardView,
                "backgroundColor",
                new ArgbEvaluator(),
                0x70000000,
                0x00000000);
        normalBackground.setDuration(400);

        normalToDim = (TransitionDrawable)getResources().getDrawable(R.drawable.transition_normal_to_dim);
        dimToNormal = (TransitionDrawable)getResources().getDrawable(R.drawable.transition_dim_to_normal);

        return rootView;
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId())
            {
                case R.id.incomeDFOkFAB:
                {
                        addAsIncomeOrExp();
                        dismiss();
                   break;
                }

                case R.id.incomeDFCancelFAB:
                {
                    dismiss();
                    break;
                }
                case R.id.calendarCardView:
                {
                    showDatePicker();
                    break;
                }
            }
        }
    };

    public void showDatePicker() {
        Toast.makeText(getActivity().getApplicationContext(), "show the date picker", Toast.LENGTH_SHORT).show();

        datePickerDialog.setYearRange(1985, 2028);
        datePickerDialog.show(getFragmentManager(), "Date Picker");
    }

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
        String dayMonthText = day + " " + getResources().getStringArray(R.array.MonthsOfYear)[month];
        dayMonthTextView.setText(dayMonthText);
        yearTextView.setText(year + "");
        Calendar localCalendar = Calendar.getInstance();
        localCalendar.set(year, month, day);
        currentDateInMilliSeconds = localCalendar.getTimeInMillis();
    }

    private void addAsIncomeOrExp()
    {
            if (isIncome)
            {
                String tempText = incomeDialogFragmentEditText.getText().toString();
                if(tempText.isEmpty())
                {
                    Toast.makeText(getActivity().getApplicationContext(), "Enter a valid amount", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    double tempAmount = Double.parseDouble(tempText);
                    incomeDFHelperMethods.addEvent(tempAmount, currentDateInMilliSeconds, "INCOME", universal_title);
                }
            }
            else
            {
                String tempText = expDialogFragmentEditText.getText().toString();
                if(tempText.isEmpty())
                {
                    Toast.makeText(getActivity().getApplicationContext(), "Enter a valid amount", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    double tempAmount = Double.parseDouble(tempText);
                    incomeDFHelperMethods.addEvent(tempAmount, currentDateInMilliSeconds, "EXP", universal_title);
                    incomeDFHelperMethods.updateExpenseInBudgetFieldsInAllBudgets(universal_title, currentDateInMilliSeconds, tempAmount);
                }
            }
        BusStand.getInstance().post(new RefreshObject(true));
        Log.d("bus", "event is posted");
    }

    private ArrayList<FieldObject> getIncomeOrExpFieldObjects()
    {
        if(isIncome)
        {
            return incomeDFHelperMethods.getAllIncomeFieldObjects();
        }
        else
        {
            return incomeDFHelperMethods.getAllExpFieldObjects();
        }
    }

    private void prepLayout()
    {
        if(isIncome)
        {
            expDialogFragmentEditText.setVisibility(View.INVISIBLE);
            incomeDialogFragmentEditText.setHint("Enter the Income Amount");
            incomeDialogFragmentEditText.setFloatingLabelText("Income Amount");
        }
        else
        {
            incomeDialogFragmentEditText.setVisibility(View.INVISIBLE);
            expDialogFragmentEditText.setHint("Enter the Expenditure Amount");
            expDialogFragmentEditText.setFloatingLabelText("Expenditure Amount");
            calendarImageView.setImageResource(R.mipmap.ic_calendarred);
        }
    }

    private void setCurrentDate()
    {
        Calendar tempCalendar = Calendar.getInstance();
        currentDateInMilliSeconds = tempCalendar.getTimeInMillis();
    }

    @Override
    public void onResume() {
        BusStand.getInstance().register(this);
        super.onResume();
    }

    @Override
    public void onPause() {
        BusStand.getInstance().unregister(this);
        super.onPause();
    }

}
