package com.giridharkarnik.moneymanager.HelperMethodsClasses;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.giridharkarnik.moneymanager.modelobjects.FieldBudgetObject;
import com.giridharkarnik.moneymanager.modelobjects.FieldObject;
import com.giridharkarnik.moneymanager.modelobjects.MainBudgetObject;
import com.giridharkarnik.moneymanager.modelobjects.MoneyObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.EventObject;

public class HelperMethods {

    public static final String DATABASE_NAME = "MONEY_DATABASE";
    public static final String TABLE_MAIN = "MONEY_TABLE";
    public static final String TABLE_FIELDS = "FIELD_TABLE";
    public static final String TABLE_MAIN_BUDGET = "MAIN_BUDGET_TABLE";
    public static final String TABLE_FIELD_BUDGET = "FIELD_BUDGET_TABLE";

    public static final int DATABASE_VERSION = 8;

    public static final String KEY_ID = "_id";
    public static final String KEY_AMOUNT = "amount";
    public static final String KEY_DATE = "date_int";

    public static final String KEY_TYPE_ID = "_id";
    public static final String KEY_TYPE = "type";
    public static final String KEY_TITLE = "field_title";
    public static final String KEY_ICON_NUM = "icon_number";

    public static final String KEY_BUDGET_ID = "_id";
    public static final String KEY_BUDGET_NAME = "budgetName";
    public static final String KEY_BUDGET_FIELD = "budgetField";
    public static final String KEY_TOTAL_AMOUNT = "totalAmount";
    public static final String KEY_AMOUNT_REMAINING = "amountRemaining";
    public static final String KEY_DATE_FROM = "dateFrom";
    public static final String KEY_DATE_TILL = "dateTill";

    public static final String KEY_REMAINING_AMOUNT = "remainingAmount";

    public static final String KEY_AMOUNT_ID = "_id";
    public static final String KEY_TOTAL_FIELD_AMOUNT = "total_amount_per_field";


    // CREATE TABLE MONEY_TABLE ( _id INTEGER PRIMARY KEY AUTOINCREMENT, amount DOUBLE);

    public String query = "CREATE TABLE " + TABLE_MAIN + " ( " + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_AMOUNT + " DOUBLE, " + KEY_DATE + " DOUBLE, " + KEY_TYPE + " TEXT NOT NULL, " + KEY_TITLE + " TEXT NOT NULL);";

    public String query1 = "CREATE TABLE " + TABLE_FIELDS + " ( " + KEY_TYPE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_TYPE + " TEXT NOT NULL, " + KEY_TITLE + " TEXT NOT NULL, " + KEY_ICON_NUM + " INTEGER);";

    public String mainBudgetQuery = "CREATE TABLE " + TABLE_MAIN_BUDGET + " ( " + KEY_BUDGET_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_BUDGET_NAME + " TEXT NOT NULL, " + KEY_TOTAL_AMOUNT + " DOUBLE, "
            + KEY_AMOUNT_REMAINING + " DOUBLE, " + KEY_DATE_FROM + " DOUBLE, " + KEY_DATE_TILL + " DOUBLE);";

    public String fieldBudgetQuery = "CREATE TABLE " + TABLE_FIELD_BUDGET + " ( " + KEY_AMOUNT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_BUDGET_NAME + " TEXT NOT NULL, " + KEY_BUDGET_FIELD + " TEXT NOT NULL, " + KEY_TOTAL_FIELD_AMOUNT + " DOUBLE, "
            + KEY_REMAINING_AMOUNT + " DOUBLE);";

    public OpenHelper localOpenHelper;
    Context localContext;

    public HelperMethods(Context c) {
        this.localContext = c;
        localOpenHelper = new OpenHelper(localContext);
    }


    public SQLiteDatabase database;

    class OpenHelper extends SQLiteOpenHelper {

        Context mainHelperContext;

        public OpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            Log.d("dev", "Main Helper method object is created");
            this.mainHelperContext = context;
        }

        @Override
        public void onCreate(SQLiteDatabase database) {
            Log.d("dev", "main onCreate method is called");
            database.execSQL(query);
            database.execSQL(query1);
            database.execSQL(mainBudgetQuery);
            database.execSQL(fieldBudgetQuery);
        }

        @Override
        public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
            database.execSQL("DROP TABLE IF EXISTS " + TABLE_MAIN);
            database.execSQL("DROP TABLE IF EXISTS " + TABLE_FIELDS);
            database.execSQL("DROP TABLE IF EXISTS " + TABLE_MAIN_BUDGET);
            database.execSQL("DROP TABLE IF EXISTS " + TABLE_FIELD_BUDGET);
            onCreate(database);
        }

        @Override
        public void onDowngrade(SQLiteDatabase database, int oldVersion, int newVersion) {
            database.execSQL("DROP TABLE IF EXISTS " + TABLE_MAIN);
            database.execSQL("DROP TABLE IF EXISTS " + TABLE_FIELDS);
            database.execSQL("DROP TABLE IF EXISTS " + TABLE_MAIN_BUDGET);
            database.execSQL("DROP TABLE IF EXISTS " + TABLE_FIELD_BUDGET);
            onCreate(database);
        }
    }

    //******** Field Table Related **************\\
    public void addField(String type, String title, int icon_number) {
        Log.d("dev", "addField method is called");
        ContentValues fieldCV = new ContentValues();
        fieldCV.put(KEY_TYPE, type);
        fieldCV.put(KEY_TITLE, title);
        fieldCV.put(KEY_ICON_NUM, icon_number);
        database = localOpenHelper.getWritableDatabase();
        Log.d("new", "Database is opened in writable form");
        database.insert(TABLE_FIELDS, null, fieldCV);
        Log.d("new", " insert complete ");
    }

    public boolean doesFieldNameExist(String fieldName,boolean isIncome)
    {
        ArrayList<FieldObject> existingFieldObjectList;
        if(isIncome)
        {
            existingFieldObjectList = getAllIncomeFieldObjects();
        }
        else
        {
            existingFieldObjectList = getAllExpFieldObjects();
        }

        for(int i=0; i<existingFieldObjectList.size(); i++)
        {
            if(existingFieldObjectList.get(i).getField_title().equals(fieldName))
            {
                return true;
            }
        }
        return false;
    }

    public void editField(String type, String title, int icon_number, int fieldId)
    {
        Log.d("field","editField method is called");
        ContentValues fieldCV = new ContentValues();
        fieldCV.put(KEY_TYPE, type);
        fieldCV.put(KEY_TITLE, title);
        fieldCV.put(KEY_ICON_NUM, icon_number);
        Log.d("field", "The passed type is " + type);
        Log.d("field", "The passed title is " + title);
        Log.d("field", "The passed icon_number is " + icon_number);
        Log.d("field","The passed fieldId is " + fieldId);
        database = null;
        database = localOpenHelper.getWritableDatabase();
        //database.update(TABLE_MAIN, cv, KEY_ID + " = " + eventId, null);
        database.update(TABLE_FIELDS, fieldCV, KEY_TYPE_ID + " = " + fieldId, null);
    }

    public void deleteAllFields()
    {
        database = localOpenHelper.getWritableDatabase();
        database.delete(TABLE_FIELDS, null, null);
    }

    public int isIncomeFieldEntered() {
        database = localOpenHelper.getReadableDatabase();
        Cursor fieldCursor = database.query(TABLE_FIELDS,
                new String[]{KEY_TYPE, KEY_TITLE}, "type = ?", new String[]{"INCOME"}, null, null, null, null);
        Log.d("dev", "The cursor size is " + fieldCursor.getCount());
        if (fieldCursor.getCount() == 0) {
            fieldCursor.close();
            return 0;
        } else {
            fieldCursor.close();
            return 1;
        }
    }

    public int isExpFieldEntered() {
        database = localOpenHelper.getReadableDatabase();
        Cursor fieldCursor = database.query(TABLE_FIELDS,
                new String[]{KEY_TYPE, KEY_TITLE}, "type = ?", new String[]{"EXP"}, null, null, null, null);
        if (fieldCursor.getCount() == 0) {
            fieldCursor.close();
            return 0;
        } else {
            fieldCursor.close();
            return 1;
        }
    }

    public ArrayList<String> getIncomeFields() {
        Log.d("dev", "getIncomeFields method is called");

        database = localOpenHelper.getReadableDatabase();

        Log.d("dev", "database opened in readable form");
        ArrayList<String> field_array = new ArrayList<>();
        //SELECT * FROM TABLE_NAME WHERE KEY_AMOUNT < '30'.
        try {
            // String income_query = "SELECT * FROM " + TABLE_FIELDS + " WHERE " + KEY_TYPE + " = INCOME";
            //Cursor income_cursor = database.rawQuery(income_query, null);
            Cursor income_cursor = database.query(TABLE_FIELDS,
                    new String[]{KEY_TYPE, KEY_TITLE}, "type = ?", new String[]{"INCOME"}, null, null, null, null);

            Log.d("dev", "query successful, cursor object created " + income_cursor.getCount());
            if (income_cursor != null & income_cursor.getCount() != 0) {
                if (income_cursor.moveToFirst()) {
                    do {
                        field_array.add(income_cursor.getString(income_cursor.getColumnIndex(KEY_TITLE)));
                        Log.d("dev", income_cursor.getString(income_cursor.getColumnIndex(KEY_TITLE)));
                    } while (income_cursor.moveToNext());
                }
            }
            income_cursor.close();
        } catch (Exception e) {
            Log.d("dev", "not able to query " + e.toString());
        }

        return field_array;
    }

    public ArrayList<String> getExpFields() {
        Log.d("dev", "getExpFields method is called");

        database = localOpenHelper.getReadableDatabase();
        Log.d("dev", "database opened in readable form");
        ArrayList<String> field_array = new ArrayList<>();

        try {
            Cursor exp_cursor = database.query(TABLE_FIELDS,
                    new String[]{KEY_TYPE, KEY_TITLE}, "type = ?", new String[]{"EXP"}, null, null, null, null);
            Log.d("dev", "query successful, cursor object created " + exp_cursor.getCount());
            if (exp_cursor != null & exp_cursor.getCount() != 0) {
                if (exp_cursor.moveToFirst()) {
                    do {
                        field_array.add(exp_cursor.getString(exp_cursor.getColumnIndex(KEY_TITLE)));
                        Log.d("dev", exp_cursor.getString(exp_cursor.getColumnIndex(KEY_TITLE)));
                    } while (exp_cursor.moveToNext());
                }
            }
            exp_cursor.close();
        } catch (Exception e) {
            Log.d("dev", "not able to query " + e.toString());
        }
        return field_array;
    }

    public ArrayList<FieldObject> getAllIncomeFieldObjects() {
        Log.d("dev", "getAllIncomeFieldObjects method is called");
        ArrayList<FieldObject> tempObjectList = new ArrayList<>();
        database = localOpenHelper.getReadableDatabase();
        Log.d("dev", "database opened in readable form");

        try {
            Cursor exp_cursor = database.query(TABLE_FIELDS,
                    new String[]{KEY_TYPE_ID, KEY_TYPE, KEY_TITLE, KEY_ICON_NUM}, "type = ?", new String[]{"INCOME"}, null, null, null, null);
            Log.d("dev", "query successful, cursor object created " + exp_cursor.getCount());
            if (exp_cursor != null & exp_cursor.getCount() != 0) {
                if (exp_cursor.moveToFirst()) {
                    do {
                        int tempId = exp_cursor.getInt(exp_cursor.getColumnIndex(KEY_TYPE_ID));
                        String tempTitle = exp_cursor.getString(exp_cursor.getColumnIndex(KEY_TITLE));
                        Log.d("dev", exp_cursor.getString(exp_cursor.getColumnIndex(KEY_TITLE)));
                        int icon_number = exp_cursor.getInt(exp_cursor.getColumnIndex(KEY_ICON_NUM));
                        FieldObject tempObject = new FieldObject(tempId, "INCOME", tempTitle, icon_number);
                        tempObjectList.add(tempObject);
                    } while (exp_cursor.moveToNext());
                }
            }
            exp_cursor.close();
        } catch (Exception e) {
            Log.d("dev", "not able to query " + e.toString());
        }

        return tempObjectList;
    }

    public ArrayList<FieldObject> getAllExpFieldObjects() {
        Log.d("dev", "getAllExpFieldObject method is called");
        ArrayList<FieldObject> tempObjectList = new ArrayList<>();
        database = localOpenHelper.getReadableDatabase();
        Log.d("dev", "database opened in readable form");

        try {
            Cursor exp_cursor = database.query(TABLE_FIELDS,
                    new String[]{KEY_TYPE_ID, KEY_TYPE, KEY_TITLE, KEY_ICON_NUM}, "type = ?", new String[]{"EXP"}, null, null, null, null);
            Log.d("dev", "query successful, cursor object created " + exp_cursor.getCount());
            if (exp_cursor != null & exp_cursor.getCount() != 0) {
                if (exp_cursor.moveToFirst()) {
                    do {
                        int tempId = exp_cursor.getInt(exp_cursor.getColumnIndex(KEY_TYPE_ID));
                        String tempTitle = exp_cursor.getString(exp_cursor.getColumnIndex(KEY_TITLE));
                        Log.d("dev", exp_cursor.getString(exp_cursor.getColumnIndex(KEY_TITLE)));
                        int icon_number = exp_cursor.getInt(exp_cursor.getColumnIndex(KEY_ICON_NUM));
                        FieldObject tempObject = new FieldObject(tempId, "INCOME", tempTitle, icon_number);
                        tempObjectList.add(tempObject);
                    } while (exp_cursor.moveToNext());
                }
            }
            exp_cursor.close();
        } catch (Exception e) {
            Log.d("dev", "not able to query " + e.toString());
        }
        return tempObjectList;
    }

    public int matchTitleToIconNum(String title, boolean isIncome) {
        //*****************SOMETHING IS SERIOUSLY WRONG HERE********************\\
        /*This method accepts a string title and return the icon number to which that title is associated with
        * Works as follows:
        * 1. Get all the list of field objects stored in the database
        * 2. loop through th list each time comparing the title string passed with the title of the field object
        * 3. if the an object matches the criteria then return the icon number of that object */
        ArrayList<FieldObject> tempFieldObjectList1;
        if(isIncome) {
            tempFieldObjectList1 = getAllIncomeFieldObjects();
        }
        else
        {
            tempFieldObjectList1 = getAllExpFieldObjects();
        }
        Log.d("new2", "The title being compared is " + title);
        for (int i = 0; i < tempFieldObjectList1.size(); i++) {
            Log.d("new2", "The size of the list is " + tempFieldObjectList1.size());
            FieldObject tempFieldObject = tempFieldObjectList1.get(i);
            String tempString = tempFieldObject.getField_title();

            if (tempString.equals(title)) {
                return tempFieldObject.getIcon_number();
            }
        }
        return 1000;
    }

    public FieldObject matchFieldObjectToFieldBudgetObject(FieldBudgetObject fieldBudgetObject)
    {
       ArrayList<FieldObject> expFieldObjectList = getAllExpFieldObjects();
        String temp = fieldBudgetObject.getBudget_field();
        for(int i=0; i<expFieldObjectList.size(); i++)
        {
            if(expFieldObjectList.get(i).getField_title().equals(temp))
            {
                return expFieldObjectList.get(i);
            }
        }
        return null;
    }
    //******** Field Table Related **************\\

    public void addEvent(double income, long date_int, String type, String fieldName) {
        try {
            ContentValues cv = new ContentValues();
            cv.put(KEY_AMOUNT, income);
            cv.put(KEY_DATE, date_int);
            cv.put(KEY_TYPE, type);
            cv.put(KEY_TITLE, fieldName);
            database = localOpenHelper.getWritableDatabase();
            Log.d("dev", "database opened in writable form");
            Log.d("dev", "addEvent() method is called");
            database.insert(TABLE_MAIN, null, cv);
            Log.d("dev", "amount is inserted successfully");
        } catch (Exception e) {
            Log.d("dev", e.toString());
        }
    }

    public void replaceEvent(double income, double date_int, String type, String title, int eventId) {
        try {
            ContentValues cv = new ContentValues();
            cv.put(KEY_AMOUNT, income);
            cv.put(KEY_DATE, date_int);
            cv.put(KEY_TYPE, type);
            cv.put(KEY_TITLE, title);

            database = localOpenHelper.getWritableDatabase();
            Log.d("dev", "database opened in writable form");
            Log.d("dev", "addEvent() method is called");
            database.update(TABLE_MAIN, cv, KEY_ID + " = " + eventId, null);
            Log.d("dev", "amount is inserted successfully");
        } catch (Exception e) {
            Log.d("dev", e.toString());
        }
    }

    public ArrayList<MoneyObject> getAllEvents() {
        Log.d("dev", "getAllEvents() method is called");
        String[] columns = {KEY_ID, KEY_AMOUNT, KEY_DATE, KEY_TYPE, KEY_TITLE};
        Cursor events_cursor;
        ArrayList<MoneyObject> incomes = new ArrayList<>();
        database = localOpenHelper.getReadableDatabase();
        Log.d("dev", "database opened in readable form");


        events_cursor = database.query(TABLE_MAIN, columns, null, null, null, null, null);
        Log.d("dev", "query successful" + events_cursor.getCount());

        if (events_cursor != null & events_cursor.getCount() != 0) {
            if (events_cursor.moveToFirst()) {
                //Log.d("dev","about to enter do while loop");
                do {
                    // Log.d("dev","loop entered");
                    int income_id = events_cursor.getInt(events_cursor.getColumnIndex(KEY_ID));
                    Log.d("dev", "id is " + income_id);
                    double income_amount = events_cursor.getDouble(events_cursor.getColumnIndex(KEY_AMOUNT));
                    Log.d("dev", "amount is " + income_amount);
                    long date_value = events_cursor.getLong(events_cursor.getColumnIndex(KEY_DATE));
                    Log.d("dev", "value of date in milli seconds is" + date_value);
                    String type = events_cursor.getString(events_cursor.getColumnIndex(KEY_TYPE));
                    Log.d("dev", "type of entry is " + type);
                    String title = events_cursor.getString(events_cursor.getColumnIndex(KEY_TITLE));
                    Log.d("dev", "title of entry is " + title);

                    MoneyObject temp = new MoneyObject(income_id, income_amount, date_value, type, title);
                    //Log.d("dev","object created");
                    //Log.d("dev","amount and id is "+temp.getAmount()+" "+temp.getAmount_id());

                    incomes.add(temp);
                    //Log.d("dev","arraylist size is "+ incomes.size());
                } while (events_cursor.moveToNext());
            }
        }

        localOpenHelper.close();
        Log.d("dev", "reading complete");
        Log.d("dev", "final size of the list is " + incomes.size());
        events_cursor.close();
        return incomes;
    }

    public ArrayList<MoneyObject> getAllEventsForAField(String fieldName)
    {
        //Each transaction, income or expenditure is noted down as an MoneyObject in the database. Each row in the SQLiteDatabase is
        //a MoneyObject
        SQLiteDatabase localDatabase = localOpenHelper.getWritableDatabase();
        Log.d("dev", "getExpEventsForAField() method is called");
        String[] columns = {KEY_ID, KEY_AMOUNT, KEY_DATE, KEY_TYPE, KEY_TITLE};
        Cursor events_cursor;
        ArrayList<MoneyObject> expEventListForAField = new ArrayList<>();
        database = localOpenHelper.getReadableDatabase();
        Log.d("dev", "database opened in readable form");
        Cursor exp_cursor = localDatabase.query(TABLE_MAIN,columns,"field_title = ?",new String[]{fieldName},null,null,null,null);
        //Cursor exp_cursor = database.query(TABLE_FIELDS,
        //columns, "type = ?", new String[]{"INCOME"}, null, null, null, null);

        events_cursor = database.query(TABLE_MAIN, columns, null, null, null, null, null);
        Log.d("dev", "query successful" + events_cursor.getCount());

        if (events_cursor != null & events_cursor.getCount() != 0) {
            if (events_cursor.moveToFirst()) {
                //Log.d("dev","about to enter do while loop");
                do {
                    // Log.d("dev","loop entered");
                    int income_id = events_cursor.getInt(events_cursor.getColumnIndex(KEY_ID));
                    Log.d("dev", "id is " + income_id);
                    double income_amount = events_cursor.getDouble(events_cursor.getColumnIndex(KEY_AMOUNT));
                    Log.d("dev", "amount is " + income_amount);
                    long date_value = events_cursor.getLong(events_cursor.getColumnIndex(KEY_DATE));
                    Log.d("dev", "value of date in milli seconds is" + date_value);
                    String type = events_cursor.getString(events_cursor.getColumnIndex(KEY_TYPE));
                    Log.d("dev", "type of entry is " + type);
                    String title = events_cursor.getString(events_cursor.getColumnIndex(KEY_TITLE));
                    Log.d("dev", "title of entry is " + title);

                    MoneyObject temp = new MoneyObject(income_id, income_amount, date_value, type, title);
                    //Log.d("dev","object created");
                    //Log.d("dev","amount and id is "+temp.getAmount()+" "+temp.getAmount_id());

                    expEventListForAField.add(temp);
                    //Log.d("dev","arraylist size is "+ incomes.size());
                } while (events_cursor.moveToNext());
            }
        }

        localOpenHelper.close();
        Log.d("dev", "reading complete");
        Log.d("dev", "final size of the list is " + expEventListForAField.size());
        events_cursor.close();
        return expEventListForAField;
    }

    public ArrayList<MoneyObject> getExpEventsForAField(String fieldName)
    {
        SQLiteDatabase localDatabase = localOpenHelper.getWritableDatabase();
        Log.d("dev", "getExpEventsForAField() method is called");
        String[] columns = {KEY_ID, KEY_AMOUNT, KEY_DATE, KEY_TYPE, KEY_TITLE};
        Cursor events_cursor;
        ArrayList<MoneyObject> expEventListForAField = new ArrayList<>();
        database = localOpenHelper.getReadableDatabase();
        Log.d("dev", "database opened in readable form");
        Cursor exp_cursor = localDatabase.query(TABLE_MAIN,columns,"type = ? AND field_title = ?",new String[]{"EXP",fieldName},null,null,null,null);
        //Cursor exp_cursor = database.query(TABLE_FIELDS,
        //columns, "type = ?", new String[]{"INCOME"}, null, null, null, null);

        events_cursor = database.query(TABLE_MAIN, columns, null, null, null, null, null);
        Log.d("dev", "query successful" + events_cursor.getCount());

        if (events_cursor != null & events_cursor.getCount() != 0) {
            if (events_cursor.moveToFirst()) {
                //Log.d("dev","about to enter do while loop");
                do {
                    // Log.d("dev","loop entered");
                    int income_id = events_cursor.getInt(events_cursor.getColumnIndex(KEY_ID));
                    Log.d("dev", "id is " + income_id);
                    double income_amount = events_cursor.getDouble(events_cursor.getColumnIndex(KEY_AMOUNT));
                    Log.d("dev", "amount is " + income_amount);
                    long date_value = events_cursor.getLong(events_cursor.getColumnIndex(KEY_DATE));
                    Log.d("dev", "value of date in milli seconds is" + date_value);
                    String type = events_cursor.getString(events_cursor.getColumnIndex(KEY_TYPE));
                    Log.d("dev", "type of entry is " + type);
                    String title = events_cursor.getString(events_cursor.getColumnIndex(KEY_TITLE));
                    Log.d("dev", "title of entry is " + title);

                    MoneyObject temp = new MoneyObject(income_id, income_amount, date_value, type, title);
                    //Log.d("dev","object created");
                    //Log.d("dev","amount and id is "+temp.getAmount()+" "+temp.getAmount_id());

                    expEventListForAField.add(temp);
                    //Log.d("dev","arraylist size is "+ incomes.size());
                } while (events_cursor.moveToNext());
            }
        }

        localOpenHelper.close();
        Log.d("dev", "reading complete");
        Log.d("dev", "final size of the list is " + expEventListForAField.size());
        events_cursor.close();
        return expEventListForAField;
    }

 /*   public void deleteAllEvents()
    {
        database = localOpenHelper.getWritableDatabase();
        database.delete(TABLE_MAIN,null,null);
        localOpenHelper.close();
    }

    public ArrayList<MoneyObject> getEventsInAscending()
    {
        ArrayList<MoneyObject> sorted_list = new ArrayList<>();
        sorted_list = getAllEvents();
        Comparator<MoneyObject> comparator  = new Comparator<MoneyObject>()
        {
            @Override
            public int compare(MoneyObject temp1, MoneyObject temp2)
            {
                if(temp1.getDate_int() > temp2.getDate_int())
                {
                    return 1;
                }
                else if(temp1.getDate_int() < temp2.getDate_int())
                {
                    return -1;
                }
                else
                {
                    return 0;
                }
            }
        };
        Collections.sort(sorted_list,comparator);
        Log.d("dev","list is sorted");
        return sorted_list;

    }*/
    public ArrayList<MoneyObject> getCurrentMonthAllEventObjects(Integer MonthInt)
    {
        ArrayList<MoneyObject> CurrentMonthObjects = new ArrayList<>();
        database = localOpenHelper.getReadableDatabase();
        Log.d("dev", "database opened in readable form");
        Cursor events_cursor = database.query(TABLE_MAIN,
                new String[]{KEY_ID, KEY_AMOUNT, KEY_DATE, KEY_TYPE, KEY_TITLE}, null, null, null, null, null);
        Log.d("dev", "The size of the cursor is " + events_cursor.getCount());

        if (events_cursor != null & events_cursor.getCount() != 0) {
            if (events_cursor.moveToFirst()) {
                //Log.d("dev","about to enter do while loop");
                do {
                    // Log.d("dev","loop entered");
                    int income_id = events_cursor.getInt(events_cursor.getColumnIndex(KEY_ID));
                    Log.d("dev", "id is " + income_id);
                    double income_amount = events_cursor.getDouble(events_cursor.getColumnIndex(KEY_AMOUNT));
                    Log.d("dev", "amount is " + income_amount);
                    long date_value = events_cursor.getLong(events_cursor.getColumnIndex(KEY_DATE));
                    Log.d("dev", "value of date in milli seconds is" + date_value);
                    Calendar temp_cal = Calendar.getInstance();
                    temp_cal.setTimeInMillis(date_value);
                    Integer Extracted_Month = temp_cal.get(Calendar.MONTH);
                    Log.d("dev", "Integer associated with the month is " + Extracted_Month);
                    String type = events_cursor.getString(events_cursor.getColumnIndex(KEY_TYPE));
                    Log.d("dev", "type of entry is " + type);
                    String title = events_cursor.getString(events_cursor.getColumnIndex(KEY_TITLE));
                    Log.d("dev", "title of entry is " + title);

                    MoneyObject temp = new MoneyObject(income_id, income_amount, date_value, type, title);
                    //Log.d("dev","object created");
                    //Log.d("dev","amount and id is "+temp.getAmount()+" "+temp.getAmount_id());
                    if (Extracted_Month == MonthInt) {
                        CurrentMonthObjects.add(temp);
                    }

                    //Log.d("dev","arraylist size is "+ incomes.size());
                } while (events_cursor.moveToNext());
            }
        }
        Log.d("dev", "the count of objects is " + CurrentMonthObjects.size());
        events_cursor.close();
        return CurrentMonthObjects;
    }
    public ArrayList<MoneyObject> getCurrentMonthIncomeObjects(Integer MonthInt) {
        //will return all the Income fields of the current month

        ArrayList<MoneyObject> CurrentMonthObjects = new ArrayList<>();
        database = localOpenHelper.getReadableDatabase();
        Log.d("dev", "database opened in readable form");
        Cursor events_cursor = database.query(TABLE_MAIN,
                new String[]{KEY_ID, KEY_AMOUNT, KEY_DATE, KEY_TYPE, KEY_TITLE}, "type = ?", new String[]{"INCOME"}, null, null, null, null);
        Log.d("dev", "The size of the cursor is " + events_cursor.getCount());

        if (events_cursor != null & events_cursor.getCount() != 0) {
            if (events_cursor.moveToFirst()) {
                //Log.d("dev","about to enter do while loop");
                do {
                    // Log.d("dev","loop entered");
                    int income_id = events_cursor.getInt(events_cursor.getColumnIndex(KEY_ID));
                    Log.d("dev", "id is " + income_id);
                    double income_amount = events_cursor.getDouble(events_cursor.getColumnIndex(KEY_AMOUNT));
                    Log.d("dev", "amount is " + income_amount);
                    long date_value = events_cursor.getLong(events_cursor.getColumnIndex(KEY_DATE));
                    Log.d("dev", "value of date in milli seconds is" + date_value);
                    Calendar temp_cal = Calendar.getInstance();
                    temp_cal.setTimeInMillis(date_value);
                    Integer Extracted_Month = temp_cal.get(Calendar.MONTH);
                    Log.d("dev", "Integer associated with the month is " + Extracted_Month);
                    String type = events_cursor.getString(events_cursor.getColumnIndex(KEY_TYPE));
                    Log.d("dev", "type of entry is " + type);
                    String title = events_cursor.getString(events_cursor.getColumnIndex(KEY_TITLE));
                    Log.d("dev", "title of entry is " + title);

                    MoneyObject temp = new MoneyObject(income_id, income_amount, date_value, type, title);
                    //Log.d("dev","object created");
                    //Log.d("dev","amount and id is "+temp.getAmount()+" "+temp.getAmount_id());
                    if (Extracted_Month == MonthInt) {
                        CurrentMonthObjects.add(temp);
                    }

                    //Log.d("dev","arraylist size is "+ incomes.size());
                } while (events_cursor.moveToNext());
            }
        }
        Log.d("dev", "the count of objects is " + CurrentMonthObjects.size());
        events_cursor.close();
        return CurrentMonthObjects;
    }

    public ArrayList<MoneyObject> getCurrentMonthExpObjects(Integer MonthInt) {
        //will return all the Income fields of the current month

        ArrayList<MoneyObject> CurrentMonthObjects = new ArrayList<>();
        database = localOpenHelper.getReadableDatabase();
        Log.d("dev", "database opened in readable form");
        Cursor events_cursor = database.query(TABLE_MAIN,
                new String[]{KEY_ID, KEY_AMOUNT, KEY_DATE, KEY_TYPE, KEY_TITLE}, "type = ?", new String[]{"EXP"}, null, null, null, null);
        Log.d("dev", "The size of the cursor is " + events_cursor.getCount());

        if (events_cursor != null & events_cursor.getCount() != 0) {
            if (events_cursor.moveToFirst()) {
                //Log.d("dev","about to enter do while loop");
                do {
                    // Log.d("dev","loop entered");
                    int income_id = events_cursor.getInt(events_cursor.getColumnIndex(KEY_ID));
                    Log.d("dev", "id is " + income_id);
                    double income_amount = events_cursor.getDouble(events_cursor.getColumnIndex(KEY_AMOUNT));
                    Log.d("dev", "amount is " + income_amount);
                    long date_value = events_cursor.getLong(events_cursor.getColumnIndex(KEY_DATE));
                    Log.d("dev", "value of date in milli seconds is" + date_value);
                    Calendar temp_cal = Calendar.getInstance();
                    temp_cal.setTimeInMillis(date_value);
                    Integer Extracted_Month = temp_cal.get(Calendar.MONTH);
                    Log.d("dev", "Integer associated with the month is " + Extracted_Month);
                    String type = events_cursor.getString(events_cursor.getColumnIndex(KEY_TYPE));
                    Log.d("dev", "type of entry is " + type);
                    String title = events_cursor.getString(events_cursor.getColumnIndex(KEY_TITLE));
                    Log.d("dev", "title of entry is " + title);

                    MoneyObject temp = new MoneyObject(income_id, income_amount, date_value, type, title);
                    //Log.d("dev","object created");
                    //Log.d("dev","amount and id is "+temp.getAmount()+" "+temp.getAmount_id());
                    if (Extracted_Month == MonthInt) {
                        CurrentMonthObjects.add(temp);
                    }

                    //Log.d("dev","arraylist size is "+ incomes.size());
                } while (events_cursor.moveToNext());
            }
        }
        Log.d("dev", "the count of objects is " + CurrentMonthObjects.size());
        return CurrentMonthObjects;
    }

    public ArrayList<Double> getIncomePerField(ArrayList<MoneyObject> tempObjectList) {
        ArrayList<Double> IncomeAmounts = new ArrayList<>();
        ArrayList<String> FieldsOfMonth = getIncomeFields();
        for (int i = 0; i < FieldsOfMonth.size(); i++) {
            //query the database for each field separately and add the obtained Income values.
            String temp = FieldsOfMonth.get(i);
            double z = 0;
            for (int j = 0; j < tempObjectList.size(); j++) {
                MoneyObject localtemp = tempObjectList.get(j);
                String objecttitle = localtemp.getTitle();
                if (temp.equals(objecttitle)) {
                    double k = localtemp.getAmount();
                    z = z + k;
                }
            }
            IncomeAmounts.add(z);
            Log.d("dev", "Income Field is " + temp + "Total amount for that field in the current month is " + z);
        }
        return IncomeAmounts;
    }

    public ArrayList<Double> getExpPerField(ArrayList<MoneyObject> tempObjectList) {
        ArrayList<Double> ExpAmounts = new ArrayList<>();
        ArrayList<String> FieldsOfMonth = getExpFields();
        for (int i = 0; i < FieldsOfMonth.size(); i++) {
            //query the database for each field separately and add the obtained Exp values.
            String temp = FieldsOfMonth.get(i);
            double z = 0;
            for (int j = 0; j < tempObjectList.size(); j++) {
                MoneyObject localtemp = tempObjectList.get(j);
                String objecttitle = localtemp.getTitle();
                if (temp.equals(objecttitle)) {
                    double k = localtemp.getAmount();
                    z = z + k;
                }
            }
            ExpAmounts.add(z);
            Log.d("dev", "Income Field is " + temp + "Total amount for that field in the current month is " + z);
        }
        return ExpAmounts;
    }

    public ArrayList<Double> getTotalIncomeForEachMonth() {
        //method WILL return the total income in a month for all twelve months
        //*****WORKING******\\
        /*
        * 1. first all the event objects are obtained
        * 2. then all the expenditure objects are filtered out and only income objects remain
        * 3. then the months of individual income events are sensed and associated with the respective months*/
        ArrayList<MoneyObject> localTempObjectList = getAllEvents();
        ArrayList<MoneyObject> IncomeObjectsList = new ArrayList<>();
        ArrayList<Double> TotalIncomePerMonthList = new ArrayList<>();
        for (int i = 0; i < localTempObjectList.size(); i++) {
            MoneyObject tempObject = localTempObjectList.get(i);
            String tempString = tempObject.getType();
            if (tempString.equals("INCOME")) {
                IncomeObjectsList.add(tempObject);
            }
        }
        Log.d("stats", "The size of the IncomeObjectsList is " + IncomeObjectsList.size());

        for (int j = 0; j < 12; j++)
        {
            double z = 0;
            for (int k = 0; k < IncomeObjectsList.size(); k++)
            {
                MoneyObject tempObject = IncomeObjectsList.get(k);
                Long tempDate = tempObject.getDate_int();
                Calendar tempCalendar = Calendar.getInstance();
                tempCalendar.setTimeInMillis(tempDate);
                if (tempCalendar.get(Calendar.MONTH) == j)
                {
                    z = z + tempObject.getAmount();
                }
            }
            TotalIncomePerMonthList.add(z);
            Log.d("stats", "The total income for the month " + j + " is " + z);
        }
        Log.d("stats", "The length of the TotalIncomePerMonthList is " + TotalIncomePerMonthList.size());
        return TotalIncomePerMonthList;
    }

    public ArrayList<Double> getTotalExpForEachMonth()
    {
        ArrayList<Double> totalExpOfEachMonthList = new ArrayList<>();

        ArrayList<MoneyObject> localTempObjectList = getAllEvents();
        ArrayList<MoneyObject> ExpObjectsList = new ArrayList<>();

        for (int i = 0; i < localTempObjectList.size(); i++) {
            MoneyObject tempObject = localTempObjectList.get(i);
            String tempString = tempObject.getType();
            if (tempString.equals("EXP")) {
                ExpObjectsList.add(tempObject);
            }
        }

        for (int j = 0; j < 12; j++)
        {
            double z = 0;
            for (int k = 0; k < ExpObjectsList.size(); k++)
            {
                MoneyObject tempObject = ExpObjectsList.get(k);
                Long tempDate = tempObject.getDate_int();
                Calendar tempCalendar = Calendar.getInstance();
                tempCalendar.setTimeInMillis(tempDate);
                if (tempCalendar.get(Calendar.MONTH) == j)
                {
                    z = z + tempObject.getAmount();
                }
            }
            totalExpOfEachMonthList.add(z);
            Log.d("stats", "The total income for the month " + j + " is " + z);
        }
        return totalExpOfEachMonthList;
    }

    public ArrayList<Double> getExpSetForAMonth(int month, int year)
    {
        ArrayList<MoneyObject> currentMonthExpObjects = getCurrentMonthExpObjects(month); //method to obtain exp objects of current month.
        //set up a Calendar for the current month
        Calendar calendar = Calendar.getInstance();
        Log.d("tar","Month sent is " + month);
        calendar.set(year, month + 1, 0);

        int noOfDaysInThisMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        Toast.makeText(localContext,"The no of days in this month is " + noOfDaysInThisMonth,Toast.LENGTH_SHORT).show();
        //initialize the array
        Double[] expSet = new Double[noOfDaysInThisMonth];
        Arrays.fill(expSet,0d);
        Log.d("tar","The size of the array is " + expSet.length);
        for(int i=0; i< currentMonthExpObjects.size(); i++)
        {
            MoneyObject tempObject = currentMonthExpObjects.get(i);
            long dateInt = tempObject.getDate_int();
            calendar.setTimeInMillis(dateInt);

            int objectDay = calendar.get(Calendar.DAY_OF_MONTH);
            //add the object's exp to the array in the position of the objectMonth
            expSet[objectDay-1] = expSet[objectDay-1] + tempObject.getAmount();
            Log.d("tar","an expenditure has happened on the day of " +  objectDay);
        }

        ArrayList<Double> expArrayList = new ArrayList<>();
        expArrayList.addAll(Arrays.asList(expSet));
        return expArrayList;
    }

    public ArrayList<MoneyObject> getAllIncomeObjects() {
        ArrayList<MoneyObject> income_list = new ArrayList<>();
        database = localOpenHelper.getReadableDatabase();
        Log.d("dev", "database opened in readable form");
        Cursor income_cursor = database.query(TABLE_MAIN,
                new String[]{KEY_ID, KEY_AMOUNT, KEY_DATE, KEY_TYPE, KEY_TITLE}, "type = ?", new String[]{"INCOME"}, null, null, null, null);
        Log.d("dev", "The size of the cursor is " + income_cursor.getCount());

        if (income_cursor != null & income_cursor.getCount() != 0) {
            if (income_cursor.moveToFirst()) {
                Log.d("dev", "about to enter do while loop");
                do {
                    Log.d("dev", "loop entered");
                    int income_id = income_cursor.getInt(income_cursor.getColumnIndex(KEY_ID));
                    Log.d("dev", "id is " + income_id);
                    double income_amount = income_cursor.getDouble(income_cursor.getColumnIndex(KEY_AMOUNT));
                    Log.d("dev", "amount is " + income_amount);
                    long date_value = income_cursor.getLong(income_cursor.getColumnIndex(KEY_DATE));
                    Log.d("dev", "value of date in milli seconds is" + date_value);
                    String type = income_cursor.getString(income_cursor.getColumnIndex(KEY_TYPE));
                    Log.d("dev", "type of entry is " + type);
                    String title = income_cursor.getString(income_cursor.getColumnIndex(KEY_TITLE));
                    Log.d("dev", "title of entry is " + title);

                    MoneyObject temp = new MoneyObject(income_id, income_amount, date_value, type, title);
                    //Log.d("dev","object created");
                    //Log.d("dev","amount and id is "+temp.getAmount()+" "+temp.getAmount_id());

                    income_list.add(temp);
                    //Log.d("dev","arraylist size is "+ incomes.size());
                } while (income_cursor.moveToNext());
            }
        }
        Log.d("dev", "The length of the list is " + income_list.size());
        return income_list;
    }

    public ArrayList<MoneyObject> getAllExpObjects() {
        ArrayList<MoneyObject> exp_list = new ArrayList<>();
        database = localOpenHelper.getReadableDatabase();
        Log.d("dev", "database opened in readable form");
        Cursor exp_cursor = database.query(TABLE_MAIN,
                new String[]{KEY_ID, KEY_AMOUNT, KEY_DATE, KEY_TYPE, KEY_TITLE}, "type = ?", new String[]{"EXP"}, null, null, null, null);
        Log.d("dev", "The size of the cursor is " + exp_cursor.getCount());

        if (exp_cursor != null & exp_cursor.getCount() != 0) {
            if (exp_cursor.moveToFirst()) {
                Log.d("dev", "about to enter do while loop");
                do {
                    Log.d("dev", "loop entered");
                    int income_id = exp_cursor.getInt(exp_cursor.getColumnIndex(KEY_ID));
                    Log.d("dev", "id is " + income_id);
                    double income_amount = exp_cursor.getDouble(exp_cursor.getColumnIndex(KEY_AMOUNT));
                    Log.d("dev", "amount is " + income_amount);
                    long date_value = exp_cursor.getLong(exp_cursor.getColumnIndex(KEY_DATE));
                    Log.d("dev", "value of date in milli seconds is" + date_value);
                    String type = exp_cursor.getString(exp_cursor.getColumnIndex(KEY_TYPE));
                    Log.d("dev", "type of entry is " + type);
                    String title = exp_cursor.getString(exp_cursor.getColumnIndex(KEY_TITLE));
                    Log.d("dev", "title of entry is " + title);

                    MoneyObject temp = new MoneyObject(income_id, income_amount, date_value, type, title);
                    //Log.d("dev","object created");
                    //Log.d("dev","amount and id is "+temp.getAmount()+" "+temp.getAmount_id());

                    exp_list.add(temp);
                    //Log.d("dev","arraylist size is "+ incomes.size());
                } while (exp_cursor.moveToNext());
            }
        }
        Log.d("dev", "The length of the list is " + exp_list.size());
        return exp_list;
    }

    public Double getSavings() {
        //get the current year and current month
        int currentMonth = Calendar.getInstance().get(Calendar.MONTH);
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        Log.d("dev", "The current year is " + currentYear + " current month is " + currentMonth);
        Calendar fixedCalendar = Calendar.getInstance();
        fixedCalendar.set(currentYear, currentMonth, 0, 0, 0, 0);
        double IncomeAmount = 0, ExpAmount = 0, SavingsAmount = 0;


        //get the total income before this month
        ArrayList<MoneyObject> savingsIncomeObject = getAllIncomeObjects();

        for (int i = 0; i < savingsIncomeObject.size(); i++) {
            MoneyObject tempObject = savingsIncomeObject.get(i);
            Calendar tempCalendar = Calendar.getInstance();
            tempCalendar.setTimeInMillis(tempObject.getDate_int());

            if (tempCalendar.before(fixedCalendar)) {
                double tempAmount = tempObject.getAmount();
                IncomeAmount = IncomeAmount + tempAmount;
            }
        }
        Log.d("dev", "The Income Amount is " + IncomeAmount);

        //get the total expenditure before this month
        ArrayList<MoneyObject> savingExpObjects = getAllExpObjects();
        for (int i = 0; i < savingExpObjects.size(); i++) {
            MoneyObject tempObject = savingExpObjects.get(i);
            Calendar tempCalendar = Calendar.getInstance();
            tempCalendar.setTimeInMillis(tempObject.getDate_int());

            if (tempCalendar.before(fixedCalendar)) {
                double tempAmount = tempObject.getAmount();
                ExpAmount = ExpAmount + tempAmount;
            }
        }

        Log.d("dev", "The Income Amount is " + IncomeAmount);
        Log.d("dev", "The Total Expenditure Amount before this month is " + ExpAmount);
        SavingsAmount = Math.abs(IncomeAmount - ExpAmount);

        return SavingsAmount;
    }

    public Double getCurrentMonthTotalIncome(int month) {
        double TotalIncome = 0;
        ArrayList<MoneyObject> tempTotalIncomeObjects = getCurrentMonthIncomeObjects(month);
        for (int i = 0; i < tempTotalIncomeObjects.size(); i++) {
            MoneyObject tempObject = tempTotalIncomeObjects.get(i);
            double tempIncome = tempObject.getAmount();
            TotalIncome = TotalIncome + tempIncome;
        }
        Log.d("dev", "Current Months's Total Income is " + TotalIncome);
        return TotalIncome;
    }

    public Double getCurrentMonthTotalExpenditure(int month) {
        double TotalExp = 0;
        ArrayList<MoneyObject> tempTotalExpObjects = getCurrentMonthExpObjects(month);
        for (int i = 0; i < tempTotalExpObjects.size(); i++) {
            MoneyObject tempObject = tempTotalExpObjects.get(i);
            double tempIncome = tempObject.getAmount();
            TotalExp = TotalExp + tempIncome;
        }
        Log.d("dev", "Current Months's Total Income is " + TotalExp);
        return TotalExp;
    }

    public void deleteSpecificEntry(long event_id) {
        SQLiteDatabase localdatabase = null;
        Log.d("dev", "deleteSpecificEntry method is called ");
        localdatabase = localOpenHelper.getWritableDatabase();

        Log.d("dev", "database opened in writable form");
        try {
            localdatabase.delete(TABLE_MAIN, KEY_ID + " = " + event_id, null);
        } catch (Exception e) {
            if (database.isOpen()) {
                Log.d("dev", "the database is open");
            } else {
                Log.d("dev", "the database is closed");

            }
            Log.d("dev", "Not able to delete " + e.toString());
        }
    }

    public void deleteSpecificFieldEntry(long field_id) {
        SQLiteDatabase localdatabase;
        Log.d("dev", "deleteSpecificEntry method is called ");
        localdatabase = localOpenHelper.getWritableDatabase();

        Log.d("dev", "database opened in writable form");
        try {
            localdatabase.delete(TABLE_FIELDS, KEY_ID + " = " + field_id, null);
        } catch (Exception e) {
            if (database.isOpen()) {
                Log.d("dev", "the database is open");
            } else {
                Log.d("dev", "the database is closed");
            }
            Log.d("dev", "Not able to delete " + e.toString());
        }
    }

    public int mapFieldTitleToIcon(String fieldTitle)
    {
        //**************PROBLEM DETECTED USE WITH CAUTION *****************\\
        // MAKE ICON SELECTION INDEPENDENT OF EXP FIELD OBJECTS /////////
        Log.d("wr","mapFieldtitleToIcon() method is called with fieldTitle " + fieldTitle);
        ArrayList<FieldObject> tempList = getAllExpFieldObjects();
        for(int i=0; i<tempList.size(); i++)
        {
            if(tempList.get(i).getField_title().equals(fieldTitle))
            {
                return tempList.get(i).getIcon_number();
            }
            else
            {
                Log.d("wr",tempList.get(i).getField_title() + " is not equal to " + fieldTitle);
            }
        }
        return 1;
    }

    //********************* budget table related methods **************************\\

    public void setBudget(String budgetName, double totalBudgetAmount, double dateFrom, double dateTill) {
        SQLiteDatabase localdatabase;
        Log.d("budget", "deleteSpecificEntry method is called ");

        try {
            ContentValues cv = new ContentValues();
            cv.put(KEY_BUDGET_NAME, budgetName);
            cv.put(KEY_TOTAL_AMOUNT, totalBudgetAmount);
            cv.put(KEY_AMOUNT_REMAINING, totalBudgetAmount);
            cv.put(KEY_DATE_FROM, dateFrom);
            cv.put(KEY_DATE_TILL, dateTill);
            localdatabase = localOpenHelper.getWritableDatabase();
            Log.d("budget", "Database opened in writable form");
            localdatabase.insert(TABLE_MAIN_BUDGET, null, cv);
            Log.d("budget", " insert complete ");
        } catch (Exception e) {
            Log.d("budget", "Something went wrong " + e.toString());
        }
    }

    public void updateEntireBudget(int budgetId, String oldBudgetName, String budgetName,double oldTotalBudgetAmount, double newTotalBudgetAmount, double oldRemainingAmount, double dateFrom, double dateTill)
    {
        try {
            double newRemainingAmount;
            SQLiteDatabase localdatabase;
            ContentValues cv = new ContentValues();
            cv.put(KEY_BUDGET_NAME, budgetName);
            cv.put(KEY_TOTAL_AMOUNT, newTotalBudgetAmount);
            cv.put(KEY_DATE_FROM, dateFrom);
            cv.put(KEY_DATE_TILL, dateTill);
            //calculate amount remaining
            //calculate the amount difference between the present total amount and new total amount
            double amountDiff = newTotalBudgetAmount - oldTotalBudgetAmount;
            if(newTotalBudgetAmount >= oldTotalBudgetAmount)
            {
                newRemainingAmount = oldRemainingAmount +  amountDiff;
            }
            else
            {
                newRemainingAmount = oldRemainingAmount - amountDiff;
            }
            cv.put(KEY_AMOUNT_REMAINING,newRemainingAmount);
            localdatabase = localOpenHelper.getWritableDatabase();
            Log.d("budget", "Database opened in writable form");
            localdatabase.update(TABLE_MAIN_BUDGET, cv, KEY_BUDGET_ID + " = " + budgetId, null);

            updateMainBudgetNameInBudgetField(oldBudgetName, budgetName);
        }
        catch (Exception e)
        {
            Log.d("update","something is wrong " + e.toString());
        }
    }

    public void updateMainBudgetNameInBudgetField(String oldBudgetName,String newBudgetName)
    {
        //Now we have to update the parent budget name in each budget field under this MainBudgetObject in the database
        SQLiteDatabase localdatabase;
        localdatabase = localOpenHelper.getWritableDatabase();
        ArrayList<FieldBudgetObject> fieldBudgetObjectListToBeUpdated = getFieldsInABudget(oldBudgetName);
        Log.d("update", "The no of fields under old budget name is " + fieldBudgetObjectListToBeUpdated.size());
        ContentValues cv = new ContentValues();
        cv.put(KEY_BUDGET_NAME, newBudgetName);
        for(int i=0; i<fieldBudgetObjectListToBeUpdated.size(); i++)
        {
                 localdatabase.update(TABLE_FIELD_BUDGET, cv, KEY_AMOUNT_ID + " = " + fieldBudgetObjectListToBeUpdated.get(i).getField_budget_object_id(), null);
        }
        Log.d("update","The no of fields under old budget after update is " + getFieldsInABudget(oldBudgetName).size());
        Log.d("update", "The no of fields under new budget name is " + getFieldsInABudget(newBudgetName).size());
    }

    public double getAmountRemainingInBudget(String mainBudgetName)
    {
        ArrayList<MainBudgetObject> tempObjectList = getAllSetBudgets();
        for(int i=0; i<tempObjectList.size(); i++)
        {
            if(tempObjectList.get(i).getBudgetName().equals(mainBudgetName))
            {
                return tempObjectList.get(i).getRemainingAmount();
            }
        }
        return 0;
    }

    public double getAmountRemainingInBudget(int mainBudgetId)
    {
        ArrayList<MainBudgetObject> tempObjectList = getAllSetBudgets();
        for(int i=0; i<tempObjectList.size(); i++)
        {
            if((tempObjectList.get(i).getBudgetId()) == mainBudgetId)
            {
                return tempObjectList.get(i).getRemainingAmount();
            }
        }
        return 0;
    }

    public ArrayList<MainBudgetObject> getAllSetBudgets() {
        SQLiteDatabase localdatabase;
        Log.d("budget", "getAllSetBudgets method is called ");

        localdatabase = localOpenHelper.getWritableDatabase();

        //query the database
        String[] columns = {KEY_BUDGET_ID, KEY_BUDGET_NAME, KEY_TOTAL_AMOUNT, KEY_AMOUNT_REMAINING, KEY_DATE_FROM, KEY_DATE_TILL};
        Cursor mainBudgetCursor;
        ArrayList<MainBudgetObject> tempObjectList = new ArrayList<>();
        mainBudgetCursor = localdatabase.query(TABLE_MAIN_BUDGET, columns, null, null, null, null, null);
        Log.d("budget", "query successful " + mainBudgetCursor.getCount());

        if (mainBudgetCursor != null & mainBudgetCursor.getCount() != 0) {
            if (mainBudgetCursor.moveToFirst()) {
                //Log.d("dev","about to enter do while loop");
                do {
                    // Log.d("dev","loop entered");
                    int main_budget_id = mainBudgetCursor.getInt(mainBudgetCursor.getColumnIndex(KEY_BUDGET_ID));
                    Log.d("budget", "id is " + main_budget_id);
                    String budget_name = mainBudgetCursor.getString(mainBudgetCursor.getColumnIndex(KEY_BUDGET_NAME));
                    Log.d("budget", "budget_name is " + budget_name);
                    double total_budget_amount = mainBudgetCursor.getDouble(mainBudgetCursor.getColumnIndex(KEY_TOTAL_AMOUNT));
                    Log.d("budget", "amount is " + total_budget_amount);
                    double total_amount_remaining = mainBudgetCursor.getDouble(mainBudgetCursor.getColumnIndex(KEY_AMOUNT_REMAINING));
                    Log.d("budget", "remaining amount is" + total_amount_remaining);
                    long dateFromTemp = mainBudgetCursor.getLong(mainBudgetCursor.getColumnIndex(KEY_DATE_FROM));
                    long dateTillTemp = mainBudgetCursor.getLong(mainBudgetCursor.getColumnIndex(KEY_DATE_TILL));
                    MainBudgetObject tempObject = new MainBudgetObject(main_budget_id, budget_name, total_budget_amount, total_amount_remaining, dateFromTemp, dateTillTemp);
                    tempObjectList.add(tempObject);

                    Log.d("budget", "List size is " + tempObjectList.size());
                } while (mainBudgetCursor.moveToNext());
            }
        }
        return tempObjectList;
    }

    public void setFieldForABudget(int mainBudgetId, String budget_name, String field_name, double total_amount_per_field, double remaining_amount) {
        SQLiteDatabase localdatabase;
        Log.d("update", "setFieldForABudget method is called ");

        try {
            ContentValues cv = new ContentValues();
            cv.put(KEY_BUDGET_NAME, budget_name);
            cv.put(KEY_BUDGET_FIELD, field_name);
            cv.put(KEY_TOTAL_FIELD_AMOUNT, total_amount_per_field);
            cv.put(KEY_REMAINING_AMOUNT, remaining_amount);
            localdatabase = localOpenHelper.getWritableDatabase();
            Log.d("budget", "Database opened in writable form");
            localdatabase.insert(TABLE_FIELD_BUDGET, null, cv);
            Log.d("budget", " insert complete ");
        } catch (Exception e) {
            Log.d("budget", "Something went wrong " + e.toString());
        }
        double mainRemainingAmount = getAmountRemainingInBudget(budget_name) - total_amount_per_field;
        Log.d("debug", "mainRemainingAmount is " + mainRemainingAmount);
        updateRemainingAmountForMainBudget(mainBudgetId, mainRemainingAmount);
    }

    public void editFieldInABudget(int mainBudgetId, String budget_name,int fieldBudgetObjectId, String fieldName, double newTotalFieldAmount, double oldTotalFieldAmount,double oldRemainingAmount)
    {
        double mainRemainingAmount,fieldRemainingAmount;
        SQLiteDatabase localdatabase;
        Log.d("update", "setFieldForABudget method is called ");
        Log.d("update", " budget Name is " + budget_name + "fieldBudgetObjectId :" + fieldBudgetObjectId);
        Log.d("update", "fieldName is  " + fieldName);
        Log.d("update", "newTotalFieldAmount is " + newTotalFieldAmount);
        Log.d("update", "oldTotalFieldAmount is " + oldTotalFieldAmount);
        Log.d("update", "oldRemainingAmount is " + oldRemainingAmount);

        try {
            ContentValues cv = new ContentValues();
            cv.put(KEY_BUDGET_NAME, budget_name);
            cv.put(KEY_BUDGET_FIELD, fieldName);
            cv.put(KEY_TOTAL_FIELD_AMOUNT, newTotalFieldAmount);
            cv.put(KEY_REMAINING_AMOUNT, 0);
            localdatabase = localOpenHelper.getWritableDatabase();
            Log.d("budget", "Database opened in writable form");
            localdatabase.update(TABLE_FIELD_BUDGET,cv,KEY_AMOUNT_ID + " = " + fieldBudgetObjectId,null);
            Log.d("budget", " insert complete ");
        } catch (Exception e) {
            Log.d("budget", "Something went wrong " + e.toString());
        }
        double difference = newTotalFieldAmount - oldTotalFieldAmount;
        if(difference >= 0)
        {
             Log.d("update","new is greater than old");
             mainRemainingAmount = getAmountRemainingInBudget(budget_name) - Math.abs(difference);
             fieldRemainingAmount = oldRemainingAmount + Math.abs(difference);
        }
        else
        {
            Log.d("update","old is greater than new");
            mainRemainingAmount = getAmountRemainingInBudget(budget_name) + Math.abs(difference);
            fieldRemainingAmount = oldRemainingAmount - Math.abs(difference);
        }
        Log.d("update","The difference is " + difference);

        Log.d("debug","mainRemainingAmount is " + mainRemainingAmount);
        updateRemainingAmountForMainBudget(mainBudgetId, mainRemainingAmount);
        updateRemainingAmountInBudgetField(fieldBudgetObjectId, fieldRemainingAmount);
    }

    public void updateRemainingAmountInBudgetField(int id, double remaining_amount)
    {
        Log.d("custom","updateRemainingAmountInBudgetField method is called");
        SQLiteDatabase localdatabase;
        Log.d("budget", "setFieldForABudget method is called ");
         try {
                 ContentValues cv = new ContentValues();
                 cv.put(KEY_REMAINING_AMOUNT, remaining_amount);
                 localdatabase = localOpenHelper.getWritableDatabase();
                 localdatabase.update(TABLE_FIELD_BUDGET, cv, KEY_AMOUNT_ID + " = " + id, null);
             }
         catch (Exception e)
         {
             Log.d("custom"," " + e.toString());
         }
    }

    public void updateRemainingAmountForMainBudget(int budgetId, double remainingAmount)
    {
        Log.d("dubug","updateRemainingAmountForMainBudget method is called");
        SQLiteDatabase localdatabase;
        try {
            ContentValues cv = new ContentValues();
            cv.put(KEY_AMOUNT_REMAINING, remainingAmount);
            localdatabase = localOpenHelper.getWritableDatabase();
            localdatabase.update(TABLE_MAIN_BUDGET, cv, KEY_BUDGET_ID + " = " + budgetId, null);
        }
        catch (Exception e)
        {
            Log.d("debug"," " + e.toString());
        }
    }

    public void refreshEventsForABudgetField(int budgetFieldId,double totalFieldAmount,String budgetFieldName,long budgetStartDateInMillis,long budgetEndDateInMillis)
    {
        Log.d("refresh","refreshEventsForABudgetField() method is called");
        ArrayList<MoneyObject> expEventsWithinTimeSpan = getEventsWithinTimeSpan(getExpEventsForAField(budgetFieldName),budgetStartDateInMillis,budgetEndDateInMillis);
        Log.d("refresh","No of expEvents within the budget period is " + expEventsWithinTimeSpan.size());
        double totalAmountRemaining = totalFieldAmount;
        for(int i=0; i < expEventsWithinTimeSpan.size(); i++)
        {
            totalAmountRemaining = totalAmountRemaining - expEventsWithinTimeSpan.get(i).getAmount();
        }
        Log.d("refresh","total amount remaining for the field " + budgetFieldName + " is " + totalAmountRemaining);
        //now refresh the remaining amount in budgetField
        updateRemainingAmountInBudgetField(budgetFieldId,totalAmountRemaining);
    }

    public ArrayList<MoneyObject> getEventsWithinTimeSpan(ArrayList<MoneyObject> moneyObjectArrayList, long startDateInMillis,long endDateInMillis)
    {
        ArrayList<MoneyObject> eventsWithinTimePeriodList = new ArrayList<>();

        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();
        Calendar eventCalendar = Calendar.getInstance();

        startCalendar.setTimeInMillis(startDateInMillis);
        endCalendar.setTimeInMillis(endDateInMillis);

        for(int i=0; i<moneyObjectArrayList.size(); i++)
        {
            MoneyObject temp = moneyObjectArrayList.get(i);
            eventCalendar.setTimeInMillis(temp.getDate_int());
            if(eventCalendar.after(startCalendar))
            {
                if(eventCalendar.before(endCalendar))
                {
                    eventsWithinTimePeriodList.add(temp);
                }
            }
        }

        return eventsWithinTimePeriodList;
    }

    public ArrayList<FieldBudgetObject> getFieldsInABudget(String budget_name) {
        //query the table TABLE_FIELD_BUDGET for fields whose parent budget is budget_name and return them as s list of FieldBudgetObjects
        /*Cursor income_cursor = database.query(TABLE_MAIN,
                new String[]{KEY_ID, KEY_AMOUNT, KEY_DATE, KEY_TYPE, KEY_TITLE}, "type = ?", new String[]{"INCOME"}, null, null, null, null);*/
        Log.d("budget","getFieldsInABudget method is called");
        SQLiteDatabase localdatabase;
        localdatabase = localOpenHelper.getWritableDatabase();
        ArrayList<FieldBudgetObject> tempObjectList = new ArrayList<>();
        Log.d("budget","the fields being searched for is under the database " + budget_name);

        Cursor tempCursor = localdatabase.query(TABLE_FIELD_BUDGET, new String[]{KEY_AMOUNT_ID, KEY_BUDGET_NAME,
                KEY_BUDGET_FIELD, KEY_TOTAL_FIELD_AMOUNT, KEY_REMAINING_AMOUNT}, "budgetName = ?", new String[]{budget_name}, null, null, null, null);
        /*public String fieldBudgetQuery = "CREATE TABLE " + TABLE_FIELD_BUDGET + " ( " + KEY_AMOUNT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_BUDGET_NAME + " TEXT NOT NULL, " + KEY_BUDGET_FIELD + " TEXT NOT NULL, " + KEY_TOTAL_FIELD_AMOUNT + " DOUBLE, "
            + KEY_REMAINING_AMOUNT + " DOUBLE);";*/
        Log.d("budget", "query is successful " + tempCursor.getCount());
        try {
            if (tempCursor != null & tempCursor.getCount() != 0) {
                if (tempCursor.moveToFirst()) {
                    //Log.d("dev","about to enter do while loop");
                    do {
                        // Log.d("dev","loop entered");
                        int field_budget_id = tempCursor.getInt(tempCursor.getColumnIndex(KEY_AMOUNT_ID));
                        Log.d("budget", "id is " + field_budget_id);
                        String field_budget_name = tempCursor.getString(tempCursor.getColumnIndex(KEY_BUDGET_NAME));
                        Log.d("budget", "budget_name is " + field_budget_name);
                        String field_name = tempCursor.getString(tempCursor.getColumnIndex(KEY_BUDGET_FIELD));
                        Log.d("budget", "The field is " + field_name);
                        double field_budget_amount = tempCursor.getDouble(tempCursor.getColumnIndex(KEY_TOTAL_FIELD_AMOUNT));
                        Log.d("budget", "amount is " + field_budget_amount);
                        double total_amount_remaining = tempCursor.getDouble(tempCursor.getColumnIndex(KEY_REMAINING_AMOUNT));
                        Log.d("budget", "remaining amount is" + total_amount_remaining);
                        FieldBudgetObject tempObject = new FieldBudgetObject(field_budget_id, field_budget_name, field_name, field_budget_amount, total_amount_remaining);
                        tempObjectList.add(tempObject);

                        Log.d("budget", "List size is " + tempObjectList.size());
                    } while (tempCursor.moveToNext());
                }
            }
        } catch (Exception e) {
            Log.d("budget"," " + e.toString());
        }
        tempCursor.close();
        return tempObjectList;
    }

    public ArrayList<FieldObject> getAllExpFieldsNotInBudget(String budgetName)
    {
        ArrayList<String> expFieldObjectList = getExpFields();
        ArrayList<String> fieldsInBudgetList = getFieldTitlesInABudget(budgetName);
        expFieldObjectList.removeAll(fieldsInBudgetList);

        ArrayList<FieldObject> tempList1 = getAllExpFieldObjects();
        ArrayList<FieldObject> returnList = new ArrayList<>();
        for(int i=0; i<tempList1.size(); i++)
        {
            String tempListString = tempList1.get(i).getField_title();
            for(int j=0 ;j< expFieldObjectList.size(); j++)
            {
                if(tempListString.equals(expFieldObjectList.get(j)))
                {
                    returnList.add(tempList1.get(i));
                    break;
                }
            }
        }
        return returnList;
    }

    private ArrayList<String> getFieldTitlesInABudget(String budgetName)
    {
        ArrayList<String> fieldsInABudgetList = new ArrayList<>();
        ArrayList<FieldBudgetObject> fieldBudgetObjectArrayList = getFieldsInABudget(budgetName);
        for(int i=0; i<fieldBudgetObjectArrayList.size(); i++)
        {
            fieldsInABudgetList.add(fieldBudgetObjectArrayList.get(i).getBudget_field());
        }
        return fieldsInABudgetList;
    }

    public void deleteEntireBudget(long budget_id, String budgetName) {
        SQLiteDatabase localdatabase;
        localdatabase = localOpenHelper.getWritableDatabase();
        localdatabase.delete(TABLE_MAIN_BUDGET, KEY_BUDGET_ID + " = " + budget_id, null);
        //get the fields in this budget
        ArrayList<FieldBudgetObject> temp = getFieldsInABudget(budgetName);
        Toast.makeText(localContext, "The no of fields in th current budget is " + temp.size(), Toast.LENGTH_SHORT).show();
        for (int i = 0; i < temp.size(); i++) {
            int temp_id = temp.get(i).getField_budget_object_id();
            localdatabase.delete(TABLE_FIELD_BUDGET, KEY_AMOUNT_ID + " = " + temp_id, null);
        }
    }

    public int getIdForAMainBudget(String mainBudgetName)
    {
        ArrayList<MainBudgetObject> tempObjectList = getAllSetBudgets();
        for(int i=0; i<tempObjectList.size(); i++)
        {
            if(mainBudgetName.equals(tempObjectList.get(i).getBudgetName()))
            {
                return tempObjectList.get(i).getBudgetId();
            }
        }
        return 0;
    }

    public void deleteFieldInABudget(int id,double totalFieldAmount,int mainBudgetId) {
        SQLiteDatabase localdatabase;
        localdatabase = localOpenHelper.getWritableDatabase();
        localdatabase.delete(TABLE_FIELD_BUDGET, KEY_AMOUNT_ID + " = " + id, null);

        //update the remaining amount in this field's parent budget
        double newAmountRemaining = getAmountRemainingInBudget(mainBudgetId) + totalFieldAmount;
        updateRemainingAmountForMainBudget(mainBudgetId,newAmountRemaining);
    }

    public ArrayList<MainBudgetObject> getBudgetsForAField(String fieldName) {
        //this method returns the list of budget Objects which includes the field the was passed to it
        //*************** WORKING **************\\
        /*1. Query the database for the budget objects containing the field passed*/
        SQLiteDatabase localdatabase = null;
        localdatabase = localOpenHelper.getWritableDatabase();

        String[] columns = {KEY_BUDGET_ID, KEY_BUDGET_NAME, KEY_TOTAL_AMOUNT, KEY_AMOUNT_REMAINING, KEY_DATE_FROM, KEY_DATE_TILL};

        ArrayList<MainBudgetObject> tempMainBudgetObjectList = new ArrayList<>();
        try {
             Cursor budgetForAFieldCursor = localdatabase.query(TABLE_MAIN_BUDGET, columns, null, null, null, null, null);
            Log.d("dev", "query successful, cursor object created " + budgetForAFieldCursor.getCount());
            if (budgetForAFieldCursor != null & budgetForAFieldCursor.getCount() != 0) {
                if (budgetForAFieldCursor.moveToFirst()) {
                    do {
                        int tempId = budgetForAFieldCursor.getInt(budgetForAFieldCursor.getColumnIndex(KEY_BUDGET_ID));
                        String tempBudgetName = budgetForAFieldCursor.getString(budgetForAFieldCursor.getColumnIndex(KEY_BUDGET_NAME));
                        double tempTotalAmount = budgetForAFieldCursor.getDouble(budgetForAFieldCursor.getColumnIndex(KEY_TOTAL_AMOUNT));
                        double tempRemainingAmount = budgetForAFieldCursor.getDouble(budgetForAFieldCursor.getColumnIndex(KEY_AMOUNT_REMAINING));
                        long tempDateFrom = budgetForAFieldCursor.getLong(budgetForAFieldCursor.getColumnIndex(KEY_DATE_FROM));
                        long tempDateTill = budgetForAFieldCursor.getLong(budgetForAFieldCursor.getColumnIndex(KEY_DATE_TILL));
                        MainBudgetObject tempObject = new MainBudgetObject(tempId, tempBudgetName, tempTotalAmount, tempRemainingAmount, tempDateFrom, tempDateTill);
                        tempMainBudgetObjectList.add(tempObject);
                    } while (budgetForAFieldCursor.moveToNext());
                }
            }
        } catch (Exception e) {
            Log.d("custom", " " + e.toString());
        }
       return  tempMainBudgetObjectList;
    }

    public void getAllocatedAmountForABudget(String budgetName)
    {

    }

    public void updateExpenseInBudgetFieldsInAllBudgets(String fieldName, long eventDate, double spentAmount)
    {
        //This method is used to update the spent amount in fields included under the various budgets when an expenditure even occurs
        //********************* WORKING ******************\\
        /*1. At First we check if the field passed to this function is included in any main budget objects
        * 2. If it is included then we loop through the main budget objects which contains the field under consideration,
        *     each time we check if the expenditure event under consideration happened within the tracking period of specified for the
        *     budget.*/
        Log.d("com","updateExpenseInBudgetFieldsInAllBudgets method is called");
        Calendar fromCalendar,tillCalendar,eventCalendar;
        fromCalendar = Calendar.getInstance();
        tillCalendar = Calendar.getInstance();
        eventCalendar = Calendar.getInstance();

        ArrayList<MainBudgetObject> tempList = getBudgetsForAField(fieldName);
        Log.d("com","The events's field " + fieldName + " is included in " + tempList.size() + " no of main budget objects");

        if(tempList.isEmpty())
        {
            return;
        }
        else
        {
            for(int i=0; i<tempList.size(); i++)
            {
                MainBudgetObject tempMainBudgetObject = tempList.get(i);
                //form the tracking period
                fromCalendar.setTimeInMillis(tempMainBudgetObject.getDateFrom());
                tillCalendar.setTimeInMillis(tempMainBudgetObject.getDateTill());

                //get the time date set by the user in the expenditure event
                eventCalendar.setTimeInMillis(eventDate);

                //check if the date is after the fromCalendar date
                if(eventCalendar.after(fromCalendar))
                {
                    if(eventCalendar.before(tillCalendar))
                    {
                        //event has happened within the tracking period of the present main budget object
                        Log.d("com","The event happened within the tracking period of the " + tempMainBudgetObject.getBudgetName() + " budget");
                        ArrayList<FieldBudgetObject> tempFieldBudgetObjectList = getFieldsInABudget(tempMainBudgetObject.getBudgetName());
                        for(int j=0; j<tempFieldBudgetObjectList.size(); j++)
                        {
                            String temp = tempFieldBudgetObjectList.get(j).getBudget_field();
                            if(temp.equals(fieldName))
                            {
                                Log.d("com", "found the budget field object");
                                //add the spentAmount with the amount already spent by the user
                                double remainingFieldAmount =  tempFieldBudgetObjectList.get(j).getField_remaining_amount() - spentAmount;
                                updateRemainingAmountInBudgetField(tempFieldBudgetObjectList.get(j).getField_budget_object_id(), remainingFieldAmount);
                                /*int mainBudgetObjectId = tempMainBudgetObject.getBudgetId();
                                Log.d("update","The remaining amount before update is " + tempMainBudgetObject.getRemainingAmount());
                                double remainingAmount = tempMainBudgetObject.getRemainingAmount() - spentAmount;
                                Log.d("update","The remaining amount in helper methods class is " + remainingAmount);
                                updateRemainingAmountForMainBudget(mainBudgetObjectId,remainingAmount);*/
                            }
                        }
                    }
                }
            }
        }
    }
}
