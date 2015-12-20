package com.giridharkarnik.moneymanager.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.giridharkarnik.moneymanager.R;
import com.giridharkarnik.moneymanager.modelobjects.FieldObject;

import java.util.ArrayList;

public class FieldsSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {

    Context incomeDFSpinnerContext;
    ArrayList<FieldObject> incomeFieldObjectList;

    public FieldsSpinnerAdapter(Context incomeDFSpinnerContext, ArrayList<FieldObject> incomeFieldObjectList) {
        this.incomeDFSpinnerContext = incomeDFSpinnerContext;
        this.incomeFieldObjectList = incomeFieldObjectList;
    }

    @Override
    public int getCount() {
        return incomeFieldObjectList.size();
    }

    @Override
    public Object getItem(int position) {
        return incomeFieldObjectList.get(position);
    }

    @Override
    public long getItemId(int Id) {
        return Id;
    }

    @Override
    public View getView(int position, View rootView, ViewGroup viewGroup) {

        if(rootView == null)
        {
            LayoutInflater inflater = (LayoutInflater)incomeDFSpinnerContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rootView = inflater.inflate(R.layout.income_df_spinner_row,null);
        }

        ImageView incomeDFSpinnerRowImageView = (ImageView)rootView.findViewById(R.id.incomeDFSpinnerRowImageView);
        TextView incomeDFSpinnerTextView = (TextView)rootView.findViewById(R.id.incomeDFSpinnerTextView);

        incomeDFSpinnerRowImageView.setImageResource(FieldIconsAdapter.imageList[incomeFieldObjectList.get(position).getIcon_number()]);
        incomeDFSpinnerTextView.setText(incomeFieldObjectList.get(position).getField_title());
        return rootView;
    }
}
