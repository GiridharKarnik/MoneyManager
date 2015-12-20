package com.giridharkarnik.moneymanager.budgetClases;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.giridharkarnik.moneymanager.HelperMethodsClasses.HelperMethods;
import com.giridharkarnik.moneymanager.R;
import com.giridharkarnik.moneymanager.adapters.BudgetMainListViewAdapter;
import com.giridharkarnik.moneymanager.commObjects.BudgetUpdateObject;
import com.giridharkarnik.moneymanager.commObjects.showFieldsInABudgetObject;
import com.giridharkarnik.moneymanager.dialogFragmentClasses.AddBudgetDialogFragment;
import com.giridharkarnik.moneymanager.dialogFragmentClasses.FieldInBudgetDialogFragment;
import com.giridharkarnik.moneymanager.modelobjects.MainBudgetObject;
import com.github.clans.fab.FloatingActionButton;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

public class BudgetActivityNew extends AppCompatActivity{

    Context budgetContext;
    Toolbar budgetToolbar;
    HelperMethods budgetHelperMethods;
    FloatingActionButton setNewBudgetButton;
    ListView budgetListView;
    BudgetMainListViewAdapter budgetMainListViewAdapter;
    ArrayList<MainBudgetObject> mainBudgetObjectList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget);

        budgetContext = getApplicationContext();
        budgetToolbar = (Toolbar) findViewById(R.id.budgetAppBar);
        setSupportActionBar(budgetToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        budgetHelperMethods = new HelperMethods(budgetContext);

        setNewBudgetButton = (FloatingActionButton) findViewById(R.id.setNewBudgetButton);
        budgetListView = (ListView)findViewById(R.id.budgetListView);
        setNewBudgetButton.setOnClickListener(listener);
        updateUI();
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId())
            {
                case R.id.setNewBudgetButton:
                {
                    AddBudgetDialogFragment addBudgetDialogFragment = new AddBudgetDialogFragment();
                    addBudgetDialogFragment.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
                    //addBudgetDialogFragment.setStyle(DialogFragment.STYLE_NO_FRAME,0);
                    Log.d("new", "The dialog is shown");

                    Bundle args = new Bundle();
                    args.putBoolean("isInEditMode", false);
                    addBudgetDialogFragment.setArguments(args);

                    addBudgetDialogFragment.show(getSupportFragmentManager(), "working on it");
                    //updateUI();
                    break;
                }
            }
        }
    };

    private void updateUI()
    {
        mainBudgetObjectList = budgetHelperMethods.getAllSetBudgets();
        budgetMainListViewAdapter = new BudgetMainListViewAdapter(getApplicationContext(),mainBudgetObjectList);
        budgetListView.setAdapter(budgetMainListViewAdapter);
        budgetMainListViewAdapter.notifyDataSetChanged();
    }

    public void onEvent(BudgetUpdateObject budgetUpdateObject)
    {
        //called when the mainBudgetObjectListView is to be updated or refreshed after addition or deletion of a mainBudgetObject
        updateUI();
    }

    public void onEvent(MainBudgetObject mainBudgetObject)
    {
        //called when a MainBudgetObject is to be edited,
        //Launch the AddBudgetDialogFragment in EditMode
        AddBudgetDialogFragment addBudgetDialogFragment = new AddBudgetDialogFragment();
        addBudgetDialogFragment.setStyle(DialogFragment.STYLE_NO_TITLE, 0);

        Bundle args = new Bundle();
        args.putBoolean("isInEditMode", true);
        args.putString("budgetName",mainBudgetObject.getBudgetName());
        args.putDouble("totalBudgetAmount",mainBudgetObject.getTotalAmount());
        args.putDouble("remainingAmount",mainBudgetObject.getRemainingAmount());
        args.putLong("dateFrom",mainBudgetObject.getDateFrom());
        args.putLong("dateTill",mainBudgetObject.getDateTill());
        args.putInt("budgetId",mainBudgetObject.getBudgetId());
        addBudgetDialogFragment.setArguments(args);

        addBudgetDialogFragment.show(getSupportFragmentManager(), "working on it");
    }


    //called when the fields included on the MainBudget is to be shown
    //method launches FieldInABudgetDialogFragment
    public void onEvent(showFieldsInABudgetObject tempObject)
    {
        FieldInBudgetDialogFragment fieldInBudgetDialogFragment = new FieldInBudgetDialogFragment();
        Bundle args = new Bundle();
        args.putString("mainBudgetName",tempObject.getBudgetName());
        args.putInt("mainBudgetId",tempObject.getBudgetId());
        args.putLong("budgetStartDate",tempObject.getBudgetStartDate());
        args.putLong("budgetEndDate",tempObject.getBudgetEndDate());
        fieldInBudgetDialogFragment.setArguments(args);
        fieldInBudgetDialogFragment.setStyle(DialogFragment.STYLE_NO_TITLE,0);
        fieldInBudgetDialogFragment.show(getSupportFragmentManager(),"working on it");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("GreenRobot","registered");
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        Log.d("GreenRobot", "unregistered");
        super.onStop();
    }
}
