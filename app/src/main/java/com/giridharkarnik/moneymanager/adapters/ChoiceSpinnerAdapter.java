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

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ChoiceSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {

    Context spinnerAdapterContext;
    ArrayList<String> choiceList;

    public ChoiceSpinnerAdapter(Context c, ArrayList<String> typesList)
    {
        spinnerAdapterContext = c;
        choiceList = typesList;
    }

    @Override
    public int getCount() {
        return choiceList.size();
    }

    @Override
    public Object getItem(int position) {
        return choiceList.get(position);
    }

    @Override
    public long getItemId(int Id) {
        return Id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) spinnerAdapterContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.choice_spinner_normal, null);
        }

        ImageView choiceImageView = (ImageView) convertView.findViewById(R.id.spinnerNormalImageView);
        TextView choiceTextView = (TextView) convertView.findViewById(R.id.choiceSpinnerNormalTextView);

        choiceTextView.setText(choiceList.get(position));

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
        {
           LayoutInflater inflater = (LayoutInflater) spinnerAdapterContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
           convertView = inflater.inflate(R.layout.stats_spinner_layout, null);
        }

        ImageView choiceImageView = (ImageView) convertView.findViewById(R.id.spinnerImageView);
        TextView choiceTextView = (TextView) convertView.findViewById(R.id.choiceSpinnerTextView);

        choiceTextView.setText(choiceList.get(position));

        return convertView;
    }
}
