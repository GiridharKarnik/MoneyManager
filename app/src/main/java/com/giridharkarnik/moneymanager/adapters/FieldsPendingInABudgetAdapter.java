package com.giridharkarnik.moneymanager.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.giridharkarnik.moneymanager.R;
import com.giridharkarnik.moneymanager.modelobjects.FieldObject;
import com.github.clans.fab.FloatingActionButton;

import java.util.ArrayList;

public class FieldsPendingInABudgetAdapter extends BaseAdapter {

    Context pendingFieldsInABudgetContext;
    ArrayList<FieldObject> fieldObjectList;

    public FieldsPendingInABudgetAdapter(Context pendingFieldsInABudgetContext, ArrayList<FieldObject> fieldObjectList) {
        this.pendingFieldsInABudgetContext = pendingFieldsInABudgetContext;
        this.fieldObjectList = fieldObjectList;
        Log.d("come", "In constructor");
        for (int i = 0; i < fieldObjectList.size(); i++) {
            Log.d("come", "object is " + fieldObjectList.get(i).getField_title());
        }
    }

    @Override
    public int getCount() {
        return fieldObjectList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int id) {
        return id;
    }

    @Override
    public View getView(final int position, View rootView, ViewGroup viewGroup) {
        Log.d("come", "getView method is called at position " + position);
        if (rootView == null) {
            Log.d("come", "View is null");
            LayoutInflater inflater = (LayoutInflater) pendingFieldsInABudgetContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rootView = inflater.inflate(R.layout.field_to_be_added_to_budget_row, null);
        }

        FieldObject fieldObject = fieldObjectList.get(position);
        ImageView fieldToBeAddedImageView = (ImageView) rootView.findViewById(R.id.fieldToBeAddedImageView);
        TextView fieldToBeAddedTitle = (TextView) rootView.findViewById(R.id.fieldToBeAddedTitle);
        final EditText fieldBudgetAmount = (EditText) rootView.findViewById(R.id.fieldBudgetAmount);
        FloatingActionButton fieldToBeAddedFAB = (FloatingActionButton) rootView.findViewById(R.id.fieldToBeAddedFAB);

        fieldToBeAddedImageView.setImageResource(FieldIconsAdapter.imageList[fieldObject.getIcon_number()]);

        fieldToBeAddedTitle.setText(fieldObject.getField_title());

        fieldBudgetAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fieldBudgetAmount.requestFocus();
            }
        });

        fieldToBeAddedFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(pendingFieldsInABudgetContext, "the field is clicked at positon " + position, Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }
}
