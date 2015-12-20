package com.giridharkarnik.moneymanager.dialogFragmentClasses;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.giridharkarnik.moneymanager.R;
import com.giridharkarnik.moneymanager.adapters.FieldIconsAdapter;
import com.giridharkarnik.moneymanager.busStand.BusStand;
import com.giridharkarnik.moneymanager.busStand.SampleObject;


public class TestGridDialogFragment extends DialogFragment {

    GridView gridView;
    SampleObject sampleObject;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.grid_dialog_fragment,container,false);
        sampleObject = new SampleObject();
        getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        BusStand.getInstance().register(this);
        gridView = (GridView)rootView.findViewById(R.id.imageGridView);
        FieldIconsAdapter fieldIconsAdapter = new FieldIconsAdapter(getActivity().getApplicationContext());
        gridView.setAdapter(fieldIconsAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
              //  Toast.makeText(getActivity().getApplicationContext(),"Clicked " + position,Toast.LENGTH_SHORT).show();
                sampleObject.setImageID(position);
                BusStand.getInstance().post(sampleObject);
                dismiss();
            }
        });

        return rootView;
    }
}
