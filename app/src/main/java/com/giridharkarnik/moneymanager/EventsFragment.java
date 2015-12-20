package com.giridharkarnik.moneymanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.giridharkarnik.moneymanager.HelperMethodsClasses.HelperMethods;
import com.giridharkarnik.moneymanager.dialogFragmentClasses.IncomeDialogFragment;
import com.giridharkarnik.moneymanager.fieldRelatedClasses.FieldActivityNew;
import com.giridharkarnik.moneymanager.graphingclasses.PiePlotter;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.nineoldandroids.animation.ArgbEvaluator;
import com.nineoldandroids.animation.ObjectAnimator;

import java.util.Calendar;

import it.neokree.materialtabs.MaterialTabListener;


public class EventsFragment extends Fragment {

    HelperMethods events_helper;
    TextView savingsView, incomeView, expView, balanceView;
    double incomeAmount = 0, expAmount = 0, savingAmount = 0, balanceAmount = 0;
    int incomeFieldFlag = 0, expenditureFieldFlag = 0;
    Activity activity;
    int currentMonth = Calendar.getInstance().get(Calendar.MONTH);
    PiePlotter localPlotter;
    FloatingActionButton incomeMenuButton, expMenuButton, fieldsMenuButton;
    FloatingActionMenu myActionMenu;
    RelativeLayout myLayout;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.events_tab, container, false);



        events_helper = new HelperMethods(getActivity().getApplicationContext());
        localPlotter = new PiePlotter(getActivity().getApplicationContext());

        incomeView = (TextView) v.findViewById(R.id.incomeCardText);
        expView = (TextView) v.findViewById(R.id.expCardText);
        balanceView = (TextView) v.findViewById(R.id.balanceCardText);
        savingsView = (TextView) v.findViewById(R.id.savingsCardText);

        incomeMenuButton = (FloatingActionButton) v.findViewById(R.id.incomeMenuButton);
        expMenuButton = (FloatingActionButton) v.findViewById(R.id.expMenuButton);
        fieldsMenuButton = (FloatingActionButton) v.findViewById(R.id.addFieldMenuButton);

        myLayout = (RelativeLayout) v.findViewById(R.id.tabsRootLayout);

        myActionMenu = (FloatingActionMenu) v.findViewById(R.id.myFloatingActionMenu);
        incomeMenuButton.setOnClickListener(listener);
        expMenuButton.setOnClickListener(listener);
        fieldsMenuButton.setOnClickListener(listener);


        incomeAmount = events_helper.getCurrentMonthTotalIncome(currentMonth);
        expAmount = events_helper.getCurrentMonthTotalExpenditure(currentMonth);
        savingAmount = events_helper.getSavings();
        balanceAmount = incomeAmount - expAmount;

        incomeView.setText(String.valueOf(incomeAmount));
        expView.setText(String.valueOf(expAmount));
        savingsView.setText(String.valueOf(savingAmount));
        balanceView.setText(String.valueOf(savingAmount + balanceAmount));

        //animations used in the fragment
        final ObjectAnimator dimBackground = ObjectAnimator.ofObject(myLayout,
                "backgroundColor",
                new ArgbEvaluator(),
                0x00000000,
                0x70000000);
        dimBackground.setDuration(400);

        final ObjectAnimator normalBackground = ObjectAnimator.ofObject(myLayout,
                "backgroundColor",
                new ArgbEvaluator(),
                0x70000000,
                0x00000000);
        normalBackground.setDuration(400);

        myLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (myActionMenu.isOpened()) {
                    myActionMenu.close(true);
                }
                return true;
            }
        });

        incomeMenuButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast incomeToast = Toast.makeText(getActivity().getApplicationContext(), "Add Income", Toast.LENGTH_SHORT);
                incomeToast.setGravity(Gravity.CENTER, v.getLeft(), v.getTop() + (v.getBottom() - v.getTop()) / 2);
                incomeToast.show();
                return true;
            }
        });

        expMenuButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast expToast = Toast.makeText(getActivity().getApplicationContext(), "Add Expenditure", Toast.LENGTH_SHORT);
                expToast.setGravity(Gravity.CENTER, v.getLeft(), v.getTop() + (v.getBottom() - v.getTop()) / 2);
                expToast.show();
                return true;
            }
        });


        myActionMenu.setOnMenuToggleListener(new FloatingActionMenu.OnMenuToggleListener() {
            @Override
            public void onMenuToggle(boolean opened) {
                if (opened) {
                    try {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dimBackground.start();
                                ((communicationInterface) activity).floatingButtonClicked();
                            }
                        });

                        ((communicationInterface) activity).floatingButtonClicked();

                    } catch (Exception e) {
                        Log.d("dev", " " + e.toString());
                    }
                } else {
                    normalBackground.start();
                    ((communicationInterface) activity).floatingButtonClickedAgain();
                }
            }
        });


        return v;
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.incomeMenuButton: {
                    incomeFieldFlag = events_helper.isIncomeFieldEntered();
                    if (incomeFieldFlag == 1) {
                        IncomeDialogFragment incomeDialogFragment = new IncomeDialogFragment();
                        incomeDialogFragment.setStyle(android.support.v4.app.DialogFragment.STYLE_NO_TITLE, 0);

                        Bundle args = new Bundle();
                        args.putBoolean("isIncome",true);
                        incomeDialogFragment.setArguments(args);

                        incomeDialogFragment.show(getFragmentManager(), "still nothing");
                        myActionMenu.close(true);
                    } else {
                        Toast.makeText(getActivity().getApplicationContext(), "No income field exits", Toast.LENGTH_SHORT).show();
                        myActionMenu.close(true);
                    }
                    break;
                }
                case R.id.expMenuButton: {
                    expenditureFieldFlag = events_helper.isExpFieldEntered();
                    if (expenditureFieldFlag == 1) {
                        IncomeDialogFragment incomeDialogFragment = new IncomeDialogFragment();
                        incomeDialogFragment.setStyle(android.support.v4.app.DialogFragment.STYLE_NO_TITLE, 0);

                        Bundle args = new Bundle();
                        args.putBoolean("isIncome",false);
                        incomeDialogFragment.setArguments(args);

                        incomeDialogFragment.show(getFragmentManager(), "still nothing");
                        myActionMenu.close(true);

                    } else {
                        Toast.makeText(getActivity().getApplicationContext(), "No expenditure field exits", Toast.LENGTH_SHORT).show();
                        myActionMenu.close(true);
                    }
                    break;
                }
                case R.id.addFieldMenuButton: {
                    try {
                        Intent intent = new Intent(getActivity(), FieldActivityNew.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        startActivity(intent);
                        myActionMenu.close(true);
                    } catch (Exception e) {
                        Log.d("tar", " " + e.toString());
                    }
                    break;
                }

            }
        }

    };

    public interface communicationInterface extends MaterialTabListener {
        void floatingButtonClicked();

        void floatingButtonClickedAgain();
    }

}



