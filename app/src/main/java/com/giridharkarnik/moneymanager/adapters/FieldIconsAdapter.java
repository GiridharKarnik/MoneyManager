package com.giridharkarnik.moneymanager.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.giridharkarnik.moneymanager.R;
import com.giridharkarnik.moneymanager.busStand.BusStand;
import com.squareup.otto.Bus;

public class FieldIconsAdapter extends BaseAdapter {

    Context testImageAdapterContext;
    public static Integer[] imageList = {R.mipmap.ic_alcohol,R.mipmap.ic_car,
                                   R.mipmap.ic_cigarette,R.mipmap.ic_dentist,
                                   R.mipmap.ic_doctor,R.mipmap.ic_electricity,
                                   R.mipmap.ic_food,R.mipmap.ic_fuel,
                                   R.mipmap.ic_gym,R.mipmap.ic_holiday,
                                   R.mipmap.ic_houserent,R.mipmap.ic_mobile,
                                   R.mipmap.ic_movie,R.mipmap.ic_petcat,
                                   R.mipmap.ic_petdog,R.mipmap.ic_pills,
                                   R.mipmap.ic_planetickets,R.mipmap.ic_shopping,
                                   R.mipmap.ic_train,R.mipmap.ic_vacation,
                                   R.mipmap.ic_water};



    public FieldIconsAdapter(Context testImageAdapterContext)
    {
        this.testImageAdapterContext = testImageAdapterContext;
    }

    @Override
    public int getCount() {
        return imageList.length;
    }

    @Override
    public Object getItem(int position) {
        return imageList[position];
    }

    @Override
    public long getItemId(int id) {
        return id;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) testImageAdapterContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.single_image_layout,viewGroup,false);

        ImageView myImageView = (ImageView)convertView.findViewById(R.id.myImageView);
        myImageView.setImageResource(imageList[position]);


        return convertView;
    }
}
