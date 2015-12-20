package com.giridharkarnik.moneymanager.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.giridharkarnik.moneymanager.HelperMethodsClasses.HelperMethods;
import com.giridharkarnik.moneymanager.R;
import com.giridharkarnik.moneymanager.busStand.BusStand;
import com.giridharkarnik.moneymanager.fieldRelatedClasses.FieldActivityNew;
import com.giridharkarnik.moneymanager.modelobjects.FieldObject;

import java.util.ArrayList;


public class FieldsAdapter extends BaseSwipeAdapter{

    private Context fieldsAdapterContext;
    private ArrayList<FieldObject> localFieldObjectList;
    HelperMethods fieldAdapterHelperMethods;
    FieldActivityNew localFieldActivityNew;

    boolean isOpen = false;
    int previousPosition;

    public FieldsAdapter(Context fieldsAdapterContext, ArrayList<FieldObject> localFieldObjectList, FieldActivityNew localFieldActivityNew) {
        this.fieldsAdapterContext = fieldsAdapterContext;
        this.localFieldObjectList = localFieldObjectList;
        this.fieldAdapterHelperMethods = new HelperMethods(fieldsAdapterContext);
        this.localFieldActivityNew = localFieldActivityNew;
        BusStand.getInstance().register(this);
    }

    @Override
    public int getSwipeLayoutResourceId(int i) {
        return R.id.fieldSwipe;
    }

    @Override
    public View generateView(int position, ViewGroup viewGroup) {
        return LayoutInflater.from(fieldsAdapterContext).inflate(R.layout.row,null);
    }

    @Override
    public void fillValues(final int position, View converter) {

        final FieldObject tempFieldObject = localFieldObjectList.get(position);

        final TextView fieldTitleText = (TextView)converter.findViewById(R.id.incomeTitleView);
        final ImageView fieldIconImageView = (ImageView)converter.findViewById(R.id.incomeImageView);
        ImageView fieldEditSwipeButton = (ImageView)converter.findViewById(R.id.fieldEditSwipeButton);
        ImageView fieldDeleteSwipeButton = (ImageView)converter.findViewById(R.id.fieldDeleteSwipeButton);

        fieldTitleText.setText(tempFieldObject.getField_title());
        Log.d("field","The icons number is " + tempFieldObject.getIcon_number() );

        fieldIconImageView.setImageResource(FieldIconsAdapter.imageList[tempFieldObject.getIcon_number()]);

        final SwipeLayout swipeLayout = (SwipeLayout)converter.findViewById(R.id.fieldSwipe);
        swipeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

        fieldEditSwipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                swipeLayout.close();
                BusStand.getInstance().post(tempFieldObject);
            }
        });

        fieldDeleteSwipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fieldAdapterHelperMethods.deleteSpecificFieldEntry(tempFieldObject.getField_id());
                localFieldActivityNew.updateListView();
            }
        });
    }

    @Override
    public int getCount() {
        return localFieldObjectList.size();
    }

    @Override
    public Object getItem(int position) {
        return localFieldObjectList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
