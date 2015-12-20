package com.giridharkarnik.moneymanager.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.giridharkarnik.moneymanager.R;

import java.util.ArrayList;

/**
 * Created by Bi on 7/22/2015.
 */
public class MonthSpinnerAdapter extends BaseAdapter implements SpinnerAdapter{

    Context monthSpinnerAdapterContext;
    ArrayList<String> monthList;

    public MonthSpinnerAdapter(Context monthSpinnerAdapterContext, ArrayList<String> monthList)
    {
        this.monthSpinnerAdapterContext = monthSpinnerAdapterContext;
        this.monthList = monthList;
    }

    @Override
    public int getCount() {
        return monthList.size();
    }

    @Override
    public Object getItem(int position) {
        return monthList.get(position);
    }

    @Override
    public long getItemId(int id) {
        return id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        if(convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater)monthSpinnerAdapterContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.month_spinner_normal,null);
        }

        TextView monthSpinnerTextView = (TextView)convertView.findViewById(R.id.normalMonthSpinnerTextView);
        monthSpinnerTextView.setText(monthList.get(position));

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if(convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater)monthSpinnerAdapterContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.month_selecter_spinner_row,null);
        }

        TextView monthSpinnerTextView = (TextView)convertView.findViewById(R.id.monthSpinnerTextView);
        monthSpinnerTextView.setText(monthList.get(position));

        return convertView;
    }
}
