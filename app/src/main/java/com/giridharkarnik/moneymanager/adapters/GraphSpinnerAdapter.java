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

import java.util.ArrayList;

public class GraphSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {

    Context graphSpinnerAdapter;
    ArrayList<String> typesOfGraph;

    public GraphSpinnerAdapter(Context graphSpinnerAdapter, ArrayList<String> typesOfGraph)
    {
        this.graphSpinnerAdapter = graphSpinnerAdapter;
        this.typesOfGraph = typesOfGraph;
    }

    @Override
    public int getCount() {
        return typesOfGraph.size();
    }

    @Override
    public Object getItem(int position) {
        return typesOfGraph.get(position);
    }

    @Override
    public long getItemId(int id) {
        return id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        if (convertView == null)
    {
        LayoutInflater inflater = (LayoutInflater) graphSpinnerAdapter.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.graph_spinner_normal, null);
    }
        TextView graphTypeTextView = (TextView) convertView.findViewById(R.id.graphTypeNormalTextView);
        graphTypeTextView.setText(typesOfGraph.get(position));

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) graphSpinnerAdapter.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.graph_spinner_row, null);
        }
        TextView graphTypeTextView = (TextView) convertView.findViewById(R.id.graphTypeTextView);
        graphTypeTextView.setText(typesOfGraph.get(position));

        return convertView;
    }
}
