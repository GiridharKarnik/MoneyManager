package com.giridharkarnik.moneymanager.fieldRelatedClasses;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.giridharkarnik.moneymanager.HelperMethodsClasses.HelperMethods;
import com.giridharkarnik.moneymanager.R;
import com.giridharkarnik.moneymanager.adapters.FieldIconsAdapter;
import com.giridharkarnik.moneymanager.adapters.FieldsAdapter;
import com.giridharkarnik.moneymanager.busStand.BusStand;
import com.giridharkarnik.moneymanager.busStand.SampleObject;
import com.giridharkarnik.moneymanager.commObjects.FABObject;
import com.giridharkarnik.moneymanager.dialogFragmentClasses.TestGridDialogFragment;
import com.giridharkarnik.moneymanager.modelobjects.FieldObject;
import com.github.clans.fab.FloatingActionButton;
import com.nineoldandroids.animation.ArgbEvaluator;
import com.nineoldandroids.animation.ObjectAnimator;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

public class FieldActivityNew extends AppCompatActivity {

    Toolbar fieldToolbar;
    RadioGroup incomeExpRadioGroup;
    com.rengwuxian.materialedittext.MaterialEditText fieldTitleEditText;
    ImageView fieldImageView;
    ListView existingFieldsListView;
    RadioButton incomeRadioButton, expRadioButton;
    TextView incomeTextView, expTextView;
    FloatingActionButton fieldDialogFABButton,cancelFAB;
    FieldsAdapter localFieldsAdapter;
    boolean editMode = false;
    private int fieldId;
    private String text;
    RelativeLayout listDimmingLayout;
    ObjectAnimator dimBackground,normalBackground;

    HelperMethods fieldActivityHelperMethods;

    boolean imageSelected = false;
    int imageID;

    ArrayList<FieldObject> localFieldObjectList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fields_new);


        //set the Toolbar
        fieldToolbar = (Toolbar) findViewById(R.id.fieldToolbar);
        setSupportActionBar(fieldToolbar);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        fieldToolbar.setBackgroundColor(getResources().getColor(R.color.fieldColorP));

        //register for otto event bus
        BusStand.getInstance().register(this);

        BusStand.getInstance().post(new FABObject(false));

        fieldActivityHelperMethods = new HelperMethods(getApplicationContext());


        fieldTitleEditText = (com.rengwuxian.materialedittext.MaterialEditText) findViewById(R.id.fieldTitleEditText);
        fieldTitleEditText.setErrorColor(getResources().getColor(R.color.materialRed));
        fieldImageView = (ImageView) findViewById(R.id.fieldImageView);
        incomeExpRadioGroup = (RadioGroup) findViewById(R.id.incomeExpRadioGroup);

        incomeRadioButton = (RadioButton) findViewById(R.id.incomeRadioButton);
        expRadioButton = (RadioButton) findViewById(R.id.expRadioButton);
        incomeTextView = (TextView) findViewById(R.id.incomeRadioText);
        expTextView = (TextView) findViewById(R.id.expRadioText);
        existingFieldsListView = (ListView)findViewById(R.id.existingFieldsListView);

        fieldDialogFABButton = (FloatingActionButton) findViewById(R.id.fieldDialogFABButton);
        cancelFAB = (FloatingActionButton)findViewById(R.id.cancelFAB);
        cancelFAB.setVisibility(View.INVISIBLE);
        listDimmingLayout = (RelativeLayout)findViewById(R.id.listDimmingLayout);

        fieldImageView.setOnClickListener(onClickListener);
        fieldDialogFABButton.setOnClickListener(onClickListener);
        incomeTextView.setOnClickListener(onClickListener);
        expTextView.setOnClickListener(onClickListener);
        cancelFAB.setOnClickListener(onClickListener);

        incomeRadioButton.setChecked(true);

        updateListView();

        incomeExpRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                updateListView();
            }
        });

        //animations used in the fragment
        dimBackground = ObjectAnimator.ofObject(listDimmingLayout,
                "backgroundColor",
                new ArgbEvaluator(),
                0x00000000,
                0x70000000);
        dimBackground.setDuration(400);

        normalBackground = ObjectAnimator.ofObject(listDimmingLayout,
                "backgroundColor",
                new ArgbEvaluator(),
                0x70000000,
                0x00000000);
        normalBackground.setDuration(400);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.fieldDialogFABButton: {
                    text = fieldTitleEditText.getText().toString();
                    if (text.isEmpty())
                    {
                        Toast.makeText(getApplicationContext(),"Please enter a valid field name",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        if (imageSelected)
                        {
                            if(incomeRadioButton.isChecked())
                            {
                                if(fieldActivityHelperMethods.doesFieldNameExist(text,true))
                                {
                                    fieldTitleEditText.setError("A income field with the same name already exists, please modify the name");
                                }
                                else {
                                    addOrReplaceField("INCOME", text);
                                    updateListView();
                                }
                            }
                            else
                            {
                                if(fieldActivityHelperMethods.doesFieldNameExist(text,false))
                                {
                                    fieldTitleEditText.setError("A expenditure field with the same name already exists, please modify the name");
                                }
                                else {
                                    Log.d("field", "The text is " + text);
                                    addOrReplaceField("EXP", text);
                                    updateListView();
                                }
                            }
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "Please choose an icon", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                }
                case R.id.cancelFAB:
                {
                    normalBackground.start();
                    existingFieldsListView.setClickable(true);
                    enableRadioAndText(true);
                    cancelFAB.setVisibility(View.INVISIBLE);
                    fieldTitleEditText.setText("");
                    break;
                }
                case R.id.fieldImageView:
                {
                    TestGridDialogFragment testGridDialogFragment = new TestGridDialogFragment();
                    testGridDialogFragment.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
                    testGridDialogFragment.show(getSupportFragmentManager(), "still learning");
                    break;
                }
                case R.id.incomeRadioText:
                {
                    incomeRadioButton.setSelected(true);
                    incomeRadioButton.setChecked(true);
                    break;
                }
                case R.id.expRadioText:
                {
                    expRadioButton.setSelected(true);
                    expRadioButton.setChecked(true);
                    break;
                }
            }
        }
    };

    public void updateListView()
    {
        if(incomeRadioButton.isChecked())
        {
            localFieldObjectList = fieldActivityHelperMethods.getAllIncomeFieldObjects();
        }
        else
        {
            localFieldObjectList = fieldActivityHelperMethods.getAllExpFieldObjects();
        }
        localFieldsAdapter = new FieldsAdapter(getApplicationContext(),localFieldObjectList,FieldActivityNew.this);
        for(int i=0;i<localFieldObjectList.size(); i++)
        {
            Log.d("field","field object name is " + localFieldObjectList.get(i).getField_title());
        }
        existingFieldsListView.setAdapter(localFieldsAdapter);
        localFieldsAdapter.notifyDataSetChanged();
        fieldTitleEditText.setText("");
        fieldTitleEditText.setFloatingLabelText("Enter field name");
        imageSelected = false;

    }

    private void addOrReplaceField(String type,String text)
    {
        Log.d("field","addOrReplaceField method is called");
        if(editMode)
        {
            Log.d("field", "going to edit");
            fieldActivityHelperMethods.editField(type, text, imageID, fieldId);
            normalBackground.start();
            existingFieldsListView.setClickable(true);
            enableRadioAndText(true);
            cancelFAB.setVisibility(View.INVISIBLE);
        }
        else
        {
            Log.d("field","going to add");
            fieldActivityHelperMethods.addField(type,text,imageID);
        }
    }

    //Otto bus event subscriptions
    @Subscribe
    public void imageComm(SampleObject sampleObject) {
        Toast.makeText(getApplicationContext(), "The image clicked is " + sampleObject.getImageID(), Toast.LENGTH_SHORT).show();
        fieldImageView.setImageResource(FieldIconsAdapter.imageList[sampleObject.getImageID()]);
        imageID = sampleObject.getImageID();
        Log.d("field","otto bus delivery image number is " + sampleObject.getImageID());
        imageSelected = true;
    }

    //communication interface between the FieldsAdapter and the FieldActivityNew
    @Subscribe
    public void configureEditMode(FieldObject tempFieldObject)
    {
        dimBackground.start();
        editMode = true;
        fieldTitleEditText.setText(tempFieldObject.getField_title());
        fieldImageView.setImageResource(FieldIconsAdapter.imageList[tempFieldObject.getIcon_number()]);
        imageID = tempFieldObject.getIcon_number();
        text = tempFieldObject.getField_title();
        fieldId = tempFieldObject.getField_id();
        fieldTitleEditText.setFloatingLabelText("Enter the changed field title");
        imageSelected = true;
        existingFieldsListView.setClickable(false);
        enableRadioAndText(false);
        cancelFAB.setVisibility(View.VISIBLE);
    }

    public void enableRadioAndText(boolean enable)
    {
        incomeRadioButton.setEnabled(enable);
        expRadioButton.setEnabled(enable);
        incomeTextView.setEnabled(enable);
        expTextView.setEnabled(enable);
    }
}
