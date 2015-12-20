package com.giridharkarnik.moneymanager.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.giridharkarnik.moneymanager.HelperMethodsClasses.HelperMethods;
import com.giridharkarnik.moneymanager.IncomeActivity;
import com.giridharkarnik.moneymanager.LedgerFragment;
import com.giridharkarnik.moneymanager.R;
import com.giridharkarnik.moneymanager.modelobjects.MoneyObject;

import java.util.ArrayList;
import java.util.Calendar;

public class ListViewAdapter extends BaseSwipeAdapter{

    private Context listViewAdapterContext;
    private ArrayList<MoneyObject> tempObjectList;
    Calendar adapter_cal = Calendar.getInstance();
    HelperMethods swipeAdapterHelperMethods;
    LedgerFragment localLedgerFragment;
    boolean isOpen = false;
    int previousPosition;


    public ListViewAdapter(Context c,ArrayList<MoneyObject> moneyObjectList,LedgerFragment tempLedgerFragmentObject) {
        listViewAdapterContext = c;
        this.tempObjectList = moneyObjectList;
        swipeAdapterHelperMethods = new HelperMethods(listViewAdapterContext);
        this.localLedgerFragment = tempLedgerFragmentObject;
    }

    public ListViewAdapter(Context c,ArrayList<MoneyObject> moneyObjectList) {
        listViewAdapterContext = c;
        this.tempObjectList = moneyObjectList;
        swipeAdapterHelperMethods = new HelperMethods(listViewAdapterContext);
    }

    @Override
    public int getSwipeLayoutResourceId(int i) {
        return R.id.swipe;
    }

    @Override
    public View generateView(int i, ViewGroup viewGroup) {
        return LayoutInflater.from(listViewAdapterContext).inflate(R.layout.swip_row,null);
    }

    @Override
    public void fillValues(final int position, View converter) {
        final MoneyObject tempObject = tempObjectList.get(position);
        final long eventDatabaseId = tempObject.getAmount_id();
        boolean isIncomeType = true;
        TextView amount_text = (TextView) converter.findViewById(R.id.amountText);
        TextView date_text = (TextView) converter.findViewById(R.id.dateText);
        TextView title_text = (TextView) converter.findViewById(R.id.titleText);
        ImageView typeIconView = (ImageView)converter.findViewById(R.id.typeView);

        double temp = tempObject.getAmount();
        String temp_type = tempObject.getType();
        Log.d("dev","The temp_type is" + temp_type);
        long temp_date = tempObject.getDate_int();
        adapter_cal.setTimeInMillis(temp_date);

        date_text.setText("" + adapter_cal.get(Calendar.DAY_OF_MONTH) + "/" + (adapter_cal.get(Calendar.MONTH)+1) + "/"
                + adapter_cal.get(Calendar.YEAR));
        title_text.setText(tempObject.getTitle());

        amount_text.setText(""+temp);
        int icon_num = swipeAdapterHelperMethods.matchTitleToIconNum(tempObject.getTitle(),tempObject.getType().equals("INCOME"));
        if(icon_num != 1000) {
            typeIconView.setImageResource(FieldIconsAdapter.imageList[icon_num]);
        }else
        {
            typeIconView.setImageResource(R.drawable.ic_launcher);
        }


      /*  if(temp_type.equals("EXP"))
        {
            converter.setBackgroundColor(Color.RED);
        }
        else if(temp_type.equals("INCOME"))
        {
            converter.setBackgroundColor(Color.GREEN);
        }
        else
        {
            converter.setBackgroundColor(Color.YELLOW);
        }*/
        final SwipeLayout swipeLayout = (SwipeLayout)converter.findViewById(R.id.swipe);
        swipeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!isOpen)
                {
                    swipeLayout.open();
                    previousPosition = position;
                    isOpen = true;

                }
                else if(isOpen & (position == previousPosition))
                {
                    swipeLayout.close();
                    isOpen =false;
                }
                else if(isOpen & (position != previousPosition))
                {
                    swipeLayout.open();
                    previousPosition = position;
                    isOpen = true;
                }
            }
        });

        ImageView editSwipeButton = (ImageView)converter.findViewById(R.id.editSwipeButton);
        editSwipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(listViewAdapterContext,"The position of the row is " + position, Toast.LENGTH_SHORT).show();
                Intent editIntent = new Intent(listViewAdapterContext,IncomeActivity.class);
                editIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                editIntent.putExtra("Amount",tempObject.getAmount());
                Log.d("dev", "The amount being sent is " + tempObject.getAmount());
                editIntent.putExtra("date",tempObject.getDate_int());
                editIntent.putExtra("eventId",tempObject.getAmount_id());
                editIntent.putExtra("eventField",tempObject.getTitle());
                editIntent.putExtra("path",2);
                listViewAdapterContext.startActivity(editIntent);
            }
        });

        ImageView deleteSwipeButton = (ImageView)converter.findViewById(R.id.deleteSwipeButton);
        deleteSwipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swipeAdapterHelperMethods.deleteSpecificEntry(eventDatabaseId);
                localLedgerFragment.updateUI();
            }
        });
    }

    @Override
    public int getCount() {
        return tempObjectList.size();
    }

    @Override
    public Object getItem(int position) {
        return tempObjectList.get(position);
    }

    @Override
    public long getItemId(int id) {
        return id;
    }
}
