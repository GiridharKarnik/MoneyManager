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


public class IncomeActivity extends ActionBarActivity {

    public int my_year,my_month,my_day;
    public long date_int;
    public TextView dateText;
    public EditText income_amount;

    public Calendar global_cal;
    private static final int DATE_DIALOG_ID = 0;
    public String universal_title;
    int path =420;
    long eventDateR = 0;

    HelperMethods income_helper = new HelperMethods(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income);

        Context c = getApplicationContext();

        Button Add_Income,Load_Sample,Clear_Database,show_Incomes;
        final Spinner income_spinner;

        Add_Income = (Button)findViewById(R.id.button6);

        dateText = (TextView)findViewById(R.id.textView);
        income_amount = (EditText)findViewById(R.id.editText);

        income_spinner = (Spinner)findViewById(R.id.spinner);
        //Connect the spinner with an ArrayAdapter which contains the value, read from the database fields table.
        //get the string array as an ArrayList object from the HelperMethods class.

        ArrayList<String> local_list = income_helper.getIncomeFields();
        path = getIntent().getIntExtra("path", 420);



      // ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,local_list);
        income_spinner.setAdapter(adapter);
        income_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int pos = income_spinner.getSelectedItemPosition();
                universal_title = (String)income_spinner.getItemAtPosition(pos);
                Log.d("dev","The selected title is " + universal_title);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if(path == 2) {
            double eventAmountR = getIntent().getDoubleExtra("Amount",1);
            eventDateR = getIntent().getLongExtra("date", 1);

            String eventTitleR = getIntent().getStringExtra("eventField");

            //set the received details in the respective views
            income_amount.setText(String.valueOf(eventAmountR));
            income_spinner.setSelection(getIndex(income_spinner,eventTitleR));


        }


        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                switch(v.getId())
                {
                    case R.id.button6:
                    {
                        //arrange all the data: income amount, date, type and title
                        String income_string = income_amount.getText().toString();
                        if(income_string.isEmpty())
                        {
                            Toast.makeText(getApplicationContext(),"Please enter a valid amount", Toast.LENGTH_SHORT).show();
                        }
                        else if(path == 2)
                        {
                            double income_amount = Double.parseDouble(income_string);
                            Log.d("dev", "The amount entered is " + income_amount);
                            try {
                                income_helper.replaceEvent(income_amount,date_int,"INCOME",universal_title,getIntent().getIntExtra("eventId",1));
                            } catch (Exception e) {
                                Log.d("dev", e.toString());
                            }
                            Intent income_back_intent = new Intent(IncomeActivity.this, MainActivity.class);
                            income_back_intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            startActivity(income_back_intent);
                        }
                        else {
                            int income_amount = Integer.parseInt(income_string);
                            Log.d("dev", "The amount entered is " + income_amount);
                            try {
                                income_helper.addEvent(income_amount, date_int, "INCOME", universal_title);
                            } catch (Exception e) {
                                Log.d("dev", e.toString());
                            }
                            Intent income_back_intent = new Intent(IncomeActivity.this, MainActivity.class);
                            income_back_intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            startActivity(income_back_intent);
                        }

                       break;
                    }

                }
            }
        };



        Add_Income.setOnClickListener(listener);


        //**************DATE RELATED **************\\

        if(path == 2)
        {
            Calendar tempCal = Calendar.getInstance();
            tempCal.setTimeInMillis(eventDateR);
            int my_yearR = tempCal.get(Calendar.YEAR);

            int my_monthR = tempCal.get(Calendar.MONTH);
            int my_dayR = tempCal.get(Calendar.DAY_OF_MONTH);
            setDate(my_yearR,my_monthR,my_dayR);
        }
        else
        {
            global_cal = Calendar.getInstance();
            int my_year = global_cal.get(Calendar.YEAR);
            int my_month = global_cal.get(Calendar.MONTH);
            int my_day = global_cal.get(Calendar.DAY_OF_MONTH);
            setDate(my_year,my_month,my_day);
        }
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

               int my_year = year;
               int my_month = month;
               int my_day = dayofmonth;
                setDate(my_year,my_month,my_day);

        }
    };

    private void setDate(int my_year,int my_month,int my_day)
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
        getMenuInflater().inflate(R.menu.menu_income, menu);
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

    private int getIndex(Spinner spinner, String myString)
    {
        int index = 0;
        for(int i=0; i<spinner.getCount(); i++)
        {
            if(spinner.getItemAtPosition(i).equals(myString))
            {
                index =i;
            }
        }
        return index;
    }
}
