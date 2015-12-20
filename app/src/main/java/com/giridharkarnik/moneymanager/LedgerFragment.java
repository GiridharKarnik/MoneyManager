package com.giridharkarnik.moneymanager;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.giridharkarnik.moneymanager.HelperMethodsClasses.HelperMethods;
import com.giridharkarnik.moneymanager.adapters.ListViewAdapter;
import com.giridharkarnik.moneymanager.busStand.BusStand;
import com.giridharkarnik.moneymanager.commObjects.RefreshObject;
import com.giridharkarnik.moneymanager.modelobjects.MoneyObject;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

public class LedgerFragment extends Fragment {

    Context ledgerContext;
    HelperMethods ledger_helper_methods;
    ListView listView;
    ArrayList<MoneyObject> ledger_list;
    ListViewAdapter localAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        Log.d("bus", "onCreateView is called");
        View v = inflater.inflate(R.layout.activity_ledger, container, false);
        super.onCreate(savedInstanceState);

        listView = (ListView) v.findViewById(R.id.listView);
        ledgerContext = getActivity().getApplicationContext();
        ledger_helper_methods = new HelperMethods(ledgerContext);
        updateUI();
        return v;
    }

    public void updateUI()
    {
        ledger_list = ledger_helper_methods.getAllEvents();
        localAdapter = new ListViewAdapter(ledgerContext, ledger_list, LedgerFragment.this);
        listView.setAdapter(localAdapter);
        localAdapter.notifyDataSetChanged();
    }

    public void updateUI(ArrayList<MoneyObject> filteredList)
    {
        localAdapter = new ListViewAdapter(ledgerContext, filteredList, LedgerFragment.this);
        listView.setAdapter(localAdapter);
        localAdapter.notifyDataSetChanged();
    }

    @Subscribe
    public void responseMethod(RefreshObject refreshObject)
    {
        updateUI();
    }

    @Override
    public void onPause() {
        BusStand.getInstance().unregister(this);
        super.onPause();
        Log.d("bus", "Ledger onPause is called");
    }

    //EventBus subscription methods
    public void onEvent(ArrayList<MoneyObject> filteredArrayList)
    {
        updateUI(filteredArrayList);
    }

    @Override
    public void onResume() {
        BusStand.getInstance().register(this);
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("GreenRobot", "registered");
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        Log.d("GreenRobot", "unregistered");
        super.onStop();
    }
}