package com.giridharkarnik.moneymanager;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.giridharkarnik.moneymanager.HelperMethodsClasses.HelperMethods;

import java.util.ArrayList;
import java.util.Calendar;


public class ExpenditureActivity extends ActionBarActivity {

    Button addExpenditure,testButton;
    TextView dateText;
    EditText expAmount;
    Spinner expSpinner;

    ArrayList<String> expList;


    public Calendar global_cal;
    private static final int DATE_DIALOG_ID = 0;
    public int my_year,my_month,my_day;
    public long date_int;
    public String universal_title;

    HelperMethods exp_helper = new HelperMethods(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenditure);
        Context c = getApplicationContext();

        addExpenditure = (Button)findViewById(R.id.button);
        testButton = (Button)findViewById(R.id.button2);
        dateText = (TextView)findViewById(R.id.textView5);
        expAmount = (EditText)findViewById(R.id.editText3);
        expSpinner = (Spinner)findViewById(R.id.spinner2);


        //Attach the spinner with an ArrayAdapter
        //ArrayAdapter interfaces the spinner with an ArrayList of Strings whose values are read from the database.
        expList = exp_helper.getExpFields();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,expList);
        expSpinner.setAdapter(adapter);

        expSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int pos = expSpinner.getSelectedItemPosition();
                universal_title = (String)expSpinner.getItemAtPosition(pos);
                Log.d("dev", "The selected title is " + universal_title);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                switch(v.getId())
                {
                    case R.id.button:
                    {
                        String expString = expAmount.getText().toString();
                        if(expString.isEmpty())
                        {
                            Toast.makeText(getApplicationContext(), "Please enter a valid amount", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            int expAmountInt = Integer.parseInt(expString);
                            exp_helper.addEvent(expAmountInt, date_int, "EXP", universal_title);
                            Intent income_back_intent = new Intent(ExpenditureActivity.this, MainActivity.class);
                            income_back_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(income_back_intent);
                        }

                        //budget tracking part
                        exp_helper.updateExpenseInBudgetFieldsInAllBudgets(universal_title, date_int, Integer.parseInt(expString));
                        Log.d("custom","event happened");
                        Log.d("custom","The event's field is " + universal_title);
                        break;
                    }
                    case R.id.button2:
                    {

                        break;
                    }

                }
            }
        };

        addExpenditure.setOnClickListener(listener);

        //**************DATE RELATED **************\\

        global_cal = Calendar.getInstance();
        my_year = global_cal.get(Calendar.YEAR);
        my_month = global_cal.get(Calendar.MONTH);
        my_day = global_cal.get(Calendar.DAY_OF_MONTH);
        setDate();
        global_cal = Calendar.getInstance();

        dateText.setOnTouchListener(new View.OnTouchListener() {
            @SuppressWarnings("deprecation")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                showDialog(DATE_DIALOG_ID);
                /* this method calls onCreateDialog method, which create a dialog
                ** for a specific id passed to it; in this case it simply creates and return a dialog to pop up
                ** in the screen. */
                return false;
            }
        });
    }


    @SuppressWarnings("deprecation")
    @Override
    protected Dialog onCreateDialog(int id)
    {
        return new DatePickerDialog(this, dl, my_year, my_month,
                my_day);
    }

    private DatePickerDialog.OnDateSetListener dl = new DatePickerDialog.OnDateSetListener()
    {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayofmonth)
        {
            my_year = year;
            my_month = month;
            my_day = dayofmonth;
            setDate();
        }
    };

    private void setDate()
    {
        dateText.setText(new StringBuilder().append(my_day).append("-").append(my_month+1).append("-")
                .append(my_year));
        Calendar local_cal = Calendar.getInstance();
        local_cal.set(my_year, my_month, my_day);
        date_int = local_cal.getTimeInMillis();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_expenditure, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
