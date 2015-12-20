package com.giridharkarnik.moneymanager.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.giridharkarnik.moneymanager.HelperMethodsClasses.HelperMethods;
import com.giridharkarnik.moneymanager.R;
import com.giridharkarnik.moneymanager.commObjects.BudgetUpdateObject;
import com.giridharkarnik.moneymanager.commObjects.RefreshFieldInABudgetLV;
import com.giridharkarnik.moneymanager.modelobjects.FieldBudgetObject;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

public class FieldsInABudgetAdapter extends BaseSwipeAdapter {

    private Context fieldInABudgetAdapterContext;
    private ArrayList<FieldBudgetObject> fieldsInABudgetList;
    HelperMethods fieldInABudgetAdapterHelperMethods;
    boolean isOpen = false;
    private int mainBudgetId;
    String mainBudgetName;
    Resources res;

    public FieldsInABudgetAdapter(Context fieldInABudgetAdapterContext, ArrayList<FieldBudgetObject> fieldsInABudgetList, String mainBudgetName, int mainBudgetId) {
        this.fieldInABudgetAdapterContext = fieldInABudgetAdapterContext;
        this.fieldsInABudgetList = fieldsInABudgetList;
        fieldInABudgetAdapterHelperMethods = new HelperMethods(fieldInABudgetAdapterContext);
        this.mainBudgetName = mainBudgetName;
        this.mainBudgetId = mainBudgetId;
        res = fieldInABudgetAdapterContext.getResources();
    }

    @Override
    public int getSwipeLayoutResourceId(int i) {
        return R.id.fieldInABudgetSwipe;
    }

    @Override
    public View generateView(int position, ViewGroup viewGroup) {
        return LayoutInflater.from(fieldInABudgetAdapterContext).inflate(R.layout.field_in_budget_row,null);
    }

    @Override
    public void fillValues(final int position, View rootView) {
        Log.d("wr", "fillValues() is called");

        final FieldBudgetObject fieldBudgetObject = fieldsInABudgetList.get(position);
        double totalAmountTemp = fieldBudgetObject.getField_total_amount();
        double remainingAmountTemp = fieldBudgetObject.getField_remaining_amount();
        double spentAmount = totalAmountTemp - remainingAmountTemp;

        ImageView fieldsInABudgetEditButton = (ImageView)rootView.findViewById(R.id.fieldsInABudgetEditButton);
        ImageView fieldsInABudgetDeleteButton = (ImageView)rootView.findViewById(R.id.fieldsInABudgetDeleteButton);
        ImageView fieldInABudgetImageView = (ImageView)rootView.findViewById(R.id.fieldInABudgetImageView);
        TextView fieldInABudgetFieldTitle = (TextView)rootView.findViewById(R.id.fieldInABudgetFieldTitle);
        TextView fieldAmountRemainingTextView = (TextView)rootView.findViewById(R.id.budgetFieldAmountRemaingTextView);
        TextView fieldTotalAmountTextView = (TextView)rootView.findViewById(R.id.budgetFieldTotalAmountTextView);
        ProgressBar budgetProgressBar = (ProgressBar)rootView.findViewById(R.id.progressBar);

        fieldInABudgetFieldTitle.setText(fieldBudgetObject.getBudget_field());
        fieldTotalAmountTextView.setText("" + fieldBudgetObject.getField_total_amount());
        fieldAmountRemainingTextView.setText("" + fieldBudgetObject.getField_remaining_amount());

        //set the icon of the field
        Log.d("wr", "gonna find icon number for field named " + fieldBudgetObject.getBudget_field());
        int iconNumber = fieldInABudgetAdapterHelperMethods.mapFieldTitleToIcon(fieldBudgetObject.getBudget_field());
        fieldInABudgetImageView.setImageResource(FieldIconsAdapter.imageList[iconNumber]);

        if(calculateStatus(totalAmountTemp,remainingAmountTemp))
        {
            //budget is in critical status, progress bar is in red
            Drawable drawable = res.getDrawable(R.drawable.progress_background);
            budgetProgressBar.setProgress(percentageSpent(totalAmountTemp,spentAmount));
            budgetProgressBar.setMax(100);
            budgetProgressBar.setProgressDrawable(drawable);
        }
        else
        {
            //budget is not in critical status, progress bar is in green
            Drawable drawable = res.getDrawable(R.drawable.progress_bar_background);
            budgetProgressBar.setProgress(percentageSpent(totalAmountTemp,spentAmount));
            budgetProgressBar.setMax(100);
            budgetProgressBar.setProgressDrawable(drawable);
        }

        fieldsInABudgetEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 EventBus.getDefault().post(fieldBudgetObject); //method can be found in FieldInBudgetDialogFragment
            }
        });

        fieldsInABudgetDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fieldInABudgetAdapterHelperMethods.deleteFieldInABudget(fieldBudgetObject.getField_budget_object_id(),fieldBudgetObject.getField_total_amount(),mainBudgetId);
                EventBus.getDefault().post(new RefreshFieldInABudgetLV());//To update the ListView of fieldBudgetListView.
                EventBus.getDefault().post(new BudgetUpdateObject()); //to refresh the mainBudgetObjectListView.
            }
        });
    }

    @Override
    public int getCount() {
        return fieldsInABudgetList.size();
    }

    @Override
    public Object getItem(int position) {
        return fieldsInABudgetList.get(position);
    }

    @Override
    public long getItemId(int id) {
        return id;
    }

    private boolean calculateStatus(double totalAmount,double remainingAmount) {
        double result = remainingAmount / totalAmount;
        return (result < 0.2);
    }

    private int percentageSpent(double totalAmount, double spentAmount)
    {
        return (int)((spentAmount/totalAmount)*100);
    }
}
