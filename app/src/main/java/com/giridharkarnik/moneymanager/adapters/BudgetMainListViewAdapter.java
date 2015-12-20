package com.giridharkarnik.moneymanager.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.giridharkarnik.moneymanager.HelperMethodsClasses.HelperMethods;
import com.giridharkarnik.moneymanager.R;
import com.giridharkarnik.moneymanager.commObjects.BudgetUpdateObject;
import com.giridharkarnik.moneymanager.commObjects.showFieldsInABudgetObject;
import com.giridharkarnik.moneymanager.modelobjects.MainBudgetObject;

import java.util.ArrayList;
import java.util.Calendar;

import de.greenrobot.event.EventBus;


public class BudgetMainListViewAdapter extends BaseSwipeAdapter {

    private Context mainBudgetAdapterContext;
    private ArrayList<MainBudgetObject> mainBudgetObjectList;
    HelperMethods mainBudgetHelperMethods;
    Calendar mainBudgetCalendar = Calendar.getInstance();
    private float x1,x2;
    static final int MIN_DISTANCE = 150;

    public BudgetMainListViewAdapter(Context mainBudgetAdapterContext, ArrayList<MainBudgetObject> mainBudgetObjectList) {
        this.mainBudgetAdapterContext = mainBudgetAdapterContext;
        this.mainBudgetObjectList = mainBudgetObjectList;
        mainBudgetHelperMethods = new HelperMethods(mainBudgetAdapterContext);
    }

    @Override
    public int getSwipeLayoutResourceId(int i) {
        return R.id.mainBudgetSwipe;
    }

    @Override
    public View generateView(int i, ViewGroup viewGroup) {
        return LayoutInflater.from(mainBudgetAdapterContext).inflate(R.layout.budget_listview_row,null);
    }

    @Override
    public void fillValues(final int position, View rootView) {
         final MainBudgetObject mainBudgetObject = mainBudgetObjectList.get(position);

        ImageView budgetTitleImageView = (ImageView)rootView.findViewById(R.id.budgetTitleImageView);
        ImageView editSwipeButton = (ImageView)rootView.findViewById(R.id.mainBudgetEditSwipeButton);
        ImageView deleteSwipeButton = (ImageView)rootView.findViewById(R.id.mainBudgetDeleteSwipeButton);
        TextView budgeTitleTextView = (TextView)rootView.findViewById(R.id.budgeTitleTextView);
        TextView budgetDateDetailsTextView = (TextView)rootView.findViewById(R.id.budgetDateDetailsTextView);
        TextView amountDetailsTextView = (TextView)rootView.findViewById(R.id.amountDetailsTextView);
        RelativeLayout onClickRelativeLayout = (RelativeLayout)rootView.findViewById(R.id.onClickRelativeLayout);

        budgeTitleTextView.setText(mainBudgetObject.getBudgetName());

        double remainingAmount = mainBudgetObject.getRemainingAmount();
        amountDetailsTextView.setText("" + remainingAmount + "/" + mainBudgetObject.getTotalAmount());

        mainBudgetCalendar.setTimeInMillis(mainBudgetObject.getDateFrom());
        String startDateFancy = "" + mainBudgetCalendar.get(Calendar.DAY_OF_MONTH) + " " + mainBudgetAdapterContext.getResources().getStringArray(R.array.MonthsOfYear)[mainBudgetCalendar.get(Calendar.MONTH)] + " " + mainBudgetCalendar.get(Calendar.YEAR);
        mainBudgetCalendar.setTimeInMillis(mainBudgetObject.getDateTill());
        String endDateFancy = "" + mainBudgetCalendar.get(Calendar.DAY_OF_MONTH) + " " + mainBudgetAdapterContext.getResources().getStringArray(R.array.MonthsOfYear)[mainBudgetCalendar.get(Calendar.MONTH)] + " " + mainBudgetCalendar.get(Calendar.YEAR);
        budgetDateDetailsTextView.setText(startDateFancy + " - " + endDateFancy);

        onClickRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new showFieldsInABudgetObject(mainBudgetObject.getBudgetName(),mainBudgetObject.getBudgetId(),mainBudgetObject.getDateFrom(),mainBudgetObject.getDateTill()));
            }
        });


        editSwipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(mainBudgetObject);
            }
        });

        deleteSwipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainBudgetHelperMethods.deleteEntireBudget(mainBudgetObject.getBudgetId(), mainBudgetObject.getBudgetName());
                EventBus.getDefault().post(new BudgetUpdateObject());//this will refresh the mainBudgetObjectListView
            }
        });
    }


    @Override
    public int getCount() {
        return mainBudgetObjectList.size();
    }

    @Override
    public Object getItem(int position) {
        return mainBudgetObjectList.get(position);
    }

    @Override
    public long getItemId(int id) {
        return id;
    }
}
