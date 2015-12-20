package com.giridharkarnik.moneymanager.dialogFragmentClasses;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.giridharkarnik.moneymanager.HelperMethodsClasses.HelperMethods;
import com.giridharkarnik.moneymanager.R;
import com.giridharkarnik.moneymanager.adapters.FieldsInABudgetAdapter;
import com.giridharkarnik.moneymanager.adapters.FieldsSpinnerAdapter;
import com.giridharkarnik.moneymanager.commObjects.BudgetUpdateObject;
import com.giridharkarnik.moneymanager.commObjects.RefreshFieldInABudgetLV;
import com.giridharkarnik.moneymanager.modelobjects.FieldBudgetObject;
import com.giridharkarnik.moneymanager.modelobjects.FieldObject;
import com.github.clans.fab.FloatingActionButton;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

public class FieldInBudgetDialogFragment extends DialogFragment {

    ListView fieldsInABudgetListView;
    FieldsInABudgetAdapter fieldsInABudgetAdapter;
    ArrayList<FieldBudgetObject> fieldInABudgetList;
    HelperMethods fieldsInABudgetHelperMethods;
    android.support.v7.widget.CardView addNewFieldsToBudgetCardView;
    ViewGroup alteredLinearLayout;
    com.rengwuxian.materialedittext.MaterialEditText fieldBudgetAmountMaterialEditText;
    FloatingActionButton addFieldToBudgetFAB;
    Spinner fieldSuggestionSpinner;
    private String mainBudgetName, fieldName, oldBudgetFieldName;
    private int mainBudgetObjectId;
    private double budgetAmountRemaining;
    ArrayList<FieldObject> fieldsNotIncludedInBudgetObjectList;
    boolean inEditMode = false;
    private double spentAmountForField;
    private FieldBudgetObject editModeFieldBudgetObject;
    private long mainBudgetStartDate,mainBudgetEndDate;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fields_in_budget_dialog_fragment, container);

        mainBudgetName = getArguments().getString("mainBudgetName");
        mainBudgetObjectId = getArguments().getInt("mainBudgetId");
        mainBudgetStartDate = getArguments().getLong("budgetStartDate");
        mainBudgetEndDate = getArguments().getLong("budgetEndDate");

        fieldsInABudgetHelperMethods = new HelperMethods(getActivity().getApplicationContext());

        getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        //getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        fieldsInABudgetListView = (ListView) rootView.findViewById(R.id.fieldsInABudgetListView);
        addNewFieldsToBudgetCardView = (android.support.v7.widget.CardView) rootView.findViewById(R.id.addNewFieldsToBudgetCardView);
        alteredLinearLayout = (ViewGroup) rootView.findViewById(R.id.alteredLinearLayout);

        fieldBudgetAmountMaterialEditText = (MaterialEditText) rootView.findViewById(R.id.fieldBudgetAmountMaterialEditText);
        fieldSuggestionSpinner = (Spinner) rootView.findViewById(R.id.fieldSuggestionSpinner);
        addFieldToBudgetFAB = (FloatingActionButton) rootView.findViewById(R.id.addFieldToBudgetFAB);

        fieldBudgetAmountMaterialEditText.setVisibility(View.GONE);
        fieldSuggestionSpinner.setVisibility(View.GONE);
        addFieldToBudgetFAB.setVisibility(View.GONE);

        //set the adapter for the spinner
        fieldsNotIncludedInBudgetObjectList = fieldsInABudgetHelperMethods.getAllExpFieldsNotInBudget(mainBudgetName);
        FieldsSpinnerAdapter budgetFieldSpinnerAdapter = new FieldsSpinnerAdapter(getActivity().getApplicationContext(), fieldsInABudgetHelperMethods.getAllExpFieldsNotInBudget(mainBudgetName));
        fieldSuggestionSpinner.setAdapter(budgetFieldSpinnerAdapter);
        budgetFieldSpinnerAdapter.notifyDataSetChanged();
        fieldSuggestionSpinner.callOnClick();
        fieldSuggestionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                int pos = fieldSuggestionSpinner.getSelectedItemPosition();
                FieldObject tempObjectField = (FieldObject) fieldSuggestionSpinner.getItemAtPosition(pos);
                fieldName = tempObjectField.getField_title();
                Log.d("dev", "The selected title is " + fieldName);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        addFieldToBudgetFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fieldAmount = fieldBudgetAmountMaterialEditText.getText().toString();
                if (fieldAmount.isEmpty()) {
                    fieldBudgetAmountMaterialEditText.setError("Enter a valid amount");
                } else {
                    Double budgetAmount = Double.parseDouble(fieldBudgetAmountMaterialEditText.getText().toString());
                    if (budgetAmount > budgetAmountRemaining) {
                        fieldBudgetAmountMaterialEditText.setError("Value exceeds available budget amount");
                        fieldBudgetAmountMaterialEditText.setShowClearButton(true);
                    } else {
                        Log.d("debug","going to set field for a budget called " + mainBudgetName);
                        if(inEditMode)
                        {
                            Toast.makeText(getActivity().getApplicationContext(), "Edit clicked", Toast.LENGTH_SHORT).show();
                            if(budgetAmount < spentAmountForField)
                            {
                                fieldBudgetAmountMaterialEditText.setError("Value is less than the amount already spent, Please increase the allocated amount");
                            }
                            else
                            {
                                Toast.makeText(getActivity().getApplicationContext(), "Gonna update", Toast.LENGTH_SHORT).show();
                                //public void editFieldInABudget(int mainBudgetId, String budget_name,int fieldBudgetObjectId, String fieldName, double newTotalFieldAmount, double oldTotalFieldAmount, double remainingAmount)
                                fieldsInABudgetHelperMethods.editFieldInABudget(mainBudgetObjectId, mainBudgetName,editModeFieldBudgetObject.getField_budget_object_id(),fieldName,budgetAmount,editModeFieldBudgetObject.getField_total_amount(),editModeFieldBudgetObject.getField_remaining_amount());
                                updateFieldInTheBudgetLV();
                                inEditMode = false;
                                if(!(fieldName.equals(oldBudgetFieldName)))
                                {
                                    //the budget field has changed, so we have to refresh the it's records(I mean it's remaining amount)
                                    //public void refreshEventsForABudgetField(int budgetFieldId,double totalFieldAmount,String budgetFieldName,long budgetStartDateInMillis,long budgetEndDateInMillis)
                                    fieldsInABudgetHelperMethods.refreshEventsForABudgetField(editModeFieldBudgetObject.getField_budget_object_id(),editModeFieldBudgetObject.getField_total_amount(),fieldName,mainBudgetStartDate,mainBudgetEndDate);
                                }
                            }
                        }
                        else {
                            fieldsInABudgetHelperMethods.setFieldForABudget(mainBudgetObjectId, mainBudgetName, fieldName, budgetAmount, budgetAmount);
                        }
                        fieldBudgetAmountMaterialEditText.setText("");
                        fieldBudgetAmountMaterialEditText.setShowClearButton(false);
                        fieldBudgetAmountMaterialEditText.setVisibility(View.GONE);
                        fieldSuggestionSpinner.setVisibility(View.GONE);
                        addFieldToBudgetFAB.setVisibility(View.GONE);

                        addNewFieldsToBudgetCardView.setVisibility(View.VISIBLE);
                        updateFieldInTheBudgetLV();
                        EventBus.getDefault().post(new BudgetUpdateObject());
                    }
                }
            }
        });

        //getting arguments from budget activity
        updateFieldInTheBudgetLV();

        addNewFieldsToBudgetCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ArrayList<FieldObject> tempList = fieldsInABudgetHelperMethods.getAllExpFieldsNotInBudget(mainBudgetName);
                if (tempList.isEmpty()) {
                    Toast.makeText(getActivity().getApplicationContext(), "There are no fields left to be added to the current budget", Toast.LENGTH_SHORT).show();
                } else {
                    addNewFieldsToBudgetCardView.setVisibility(View.GONE);
                    budgetAmountRemaining = fieldsInABudgetHelperMethods.getAmountRemainingInBudget(mainBudgetName);
                    fieldBudgetAmountMaterialEditText.setFloatingLabelText(budgetAmountRemaining + " is remaining in budget");
                    fieldBudgetAmountMaterialEditText.setVisibility(View.VISIBLE);
                    fieldSuggestionSpinner.setVisibility(View.VISIBLE);
                    addFieldToBudgetFAB.setVisibility(View.VISIBLE);
                }
            }
        });
        // updatePendingFieldLV();
        return rootView;
    }

    private void updateFieldInTheBudgetLV() {
        Log.d("wr","updateFieldInTheBudgetLV() is called");
        //get fields in the budget that was clicked
        fieldInABudgetList = fieldsInABudgetHelperMethods.getFieldsInABudget(mainBudgetName);
        //attach adapter for fieldsInABudgetListView
        fieldsInABudgetAdapter = new FieldsInABudgetAdapter(getActivity().getApplicationContext(), fieldInABudgetList, mainBudgetName, mainBudgetObjectId);
        fieldsInABudgetListView.setAdapter(fieldsInABudgetAdapter);
        fieldsInABudgetAdapter.notifyDataSetChanged();
    }

    //called when a field in the current budget is to be edited. Called from FieldsInABudgetAdapter.
    public void onEvent(FieldBudgetObject fieldBudgetObject)
    {
        inEditMode = true;
        oldBudgetFieldName = fieldBudgetObject.getBudget_field();
        editModeFieldBudgetObject = fieldBudgetObject;
        addNewFieldsToBudgetCardView.setVisibility(View.GONE);
        budgetAmountRemaining = fieldsInABudgetHelperMethods.getAmountRemainingInBudget(mainBudgetName);
        fieldBudgetAmountMaterialEditText.setFloatingLabelText(budgetAmountRemaining + " is remaining in budget");
        fieldBudgetAmountMaterialEditText.setVisibility(View.VISIBLE);
        fieldSuggestionSpinner.setVisibility(View.VISIBLE);
        addFieldToBudgetFAB.setVisibility(View.VISIBLE);

        fieldBudgetAmountMaterialEditText.setText(fieldBudgetObject.getField_total_amount() + "");
        spentAmountForField = fieldBudgetObject.getField_total_amount() - fieldBudgetObject.getField_remaining_amount();

        //reset the spinner adapter by adding the fieldObject along with the fieldObjects which are not included
        //in the present mainBudgetObject
        ArrayList<FieldObject> spinnerListInEditMode = fieldsInABudgetHelperMethods.getAllExpFieldsNotInBudget(mainBudgetName);
        spinnerListInEditMode.add(0,fieldsInABudgetHelperMethods.matchFieldObjectToFieldBudgetObject(fieldBudgetObject));
        FieldsSpinnerAdapter budgetFieldSpinnerAdapter = new FieldsSpinnerAdapter(getActivity().getApplicationContext(), spinnerListInEditMode);
        fieldSuggestionSpinner.setAdapter(budgetFieldSpinnerAdapter);
        budgetFieldSpinnerAdapter.notifyDataSetChanged();
        fieldSuggestionSpinner.callOnClick();
    }

    public void onEvent(RefreshFieldInABudgetLV refreshFieldInABudgetLV)
    {
        updateFieldInTheBudgetLV();
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
