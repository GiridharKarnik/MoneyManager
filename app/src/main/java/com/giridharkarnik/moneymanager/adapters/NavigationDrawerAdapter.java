package com.giridharkarnik.moneymanager.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.giridharkarnik.moneymanager.NavigationSectionModel;
import com.giridharkarnik.moneymanager.R;

import java.util.ArrayList;

public class NavigationDrawerAdapter extends BaseAdapter {

    ArrayList<NavigationSectionModel> tempNavigationSectionModelList;
    Context navAdapterContext;

    public NavigationDrawerAdapter(Context c,ArrayList<NavigationSectionModel> tempNavigationSectionModelList) {
        this.navAdapterContext = c;
        this.tempNavigationSectionModelList = tempNavigationSectionModelList;
    }

    @Override
    public int getCount() {
        return tempNavigationSectionModelList.size();
    }

    @Override
    public Object getItem(int position) {
        return tempNavigationSectionModelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if(view == null)
        {
            LayoutInflater mInflater = (LayoutInflater)navAdapterContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            view = mInflater.inflate(R.layout.nav_section_row, null);
        }
        NavigationSectionModel localNavSectionModelObject = tempNavigationSectionModelList.get(position);

        ImageView sectionImageView = (ImageView)view.findViewById(R.id.navSectionImageView);
        TextView sectionTextView = (TextView)view.findViewById(R.id.navSectionTextView);

        sectionImageView.setImageResource(localNavSectionModelObject.getSectionImage());
        sectionTextView.setText(localNavSectionModelObject.getSectionTitle());
        return view;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getViewTypeCount() {
        return super.getViewTypeCount();
    }
}
