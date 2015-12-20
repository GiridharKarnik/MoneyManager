package com.giridharkarnik.moneymanager;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.giridharkarnik.moneymanager.HelperMethodsClasses.HelperMethods;
import com.giridharkarnik.moneymanager.adapters.NavigationDrawerAdapter;
import com.giridharkarnik.moneymanager.budgetClases.BudgetActivityNew;
import com.giridharkarnik.moneymanager.busStand.BusStand;
import com.giridharkarnik.moneymanager.modelobjects.MoneyObject;
import com.squareup.otto.Subscribe;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;

import de.greenrobot.event.EventBus;
import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;

public class MainActivity extends ActionBarActivity implements EventsFragment.communicationInterface {
    Toolbar libs_toolbar;
    MaterialTabHost tabhost_libs;
    ViewPager pager_libs;
    MyViewAdapter adapter_libs;
    RelativeLayout myRootLayout;
    DrawerLayout mainDrawerLayout;
    ListView featuresListView;
    android.support.v7.app.ActionBarDrawerToggle mainActionBarDrawerToggle;
    NavigationDrawerAdapter mainNavigationDrawerAdapter;
    HelperMethods mainActivityHelperMethods;
    int tabPosition = 1;
    boolean[] subMenuChecked = {true,false,false,false,false,false,false};


    int from0to1 = 0,
        from2to1 = 0;
    int duration = 500;

    int tab1Color;

    com.nineoldandroids.animation.ObjectAnimator dimBackground, normalBackground;
    BusStand busStand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabs_activity_library);

        mainActivityHelperMethods = new HelperMethods(getApplicationContext());
       final int tab0Color = getResources().getColor(R.color.tabZeroColorPrimary),
                tab2Color = getResources().getColor(R.color.tabTwoColorPrimary);

        tab1Color = getResources().getColor(R.color.tabOne);
        BusStand.getInstance().register(this);

        libs_toolbar = (Toolbar)findViewById(R.id.app_bar_libs);
        setSupportActionBar(libs_toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        myRootLayout = (RelativeLayout)findViewById(R.id.magicLayout);
        tabhost_libs = (MaterialTabHost) findViewById(R.id.materialTabHost);

        pager_libs = (ViewPager) findViewById(R.id.pager_libs);
        adapter_libs = new MyViewAdapter(getSupportFragmentManager());
        pager_libs.setAdapter(adapter_libs);

        //Drawer Layout related
        mainDrawerLayout = (DrawerLayout)findViewById(R.id.mainDrawerLayout);
        featuresListView = (ListView)findViewById(R.id.featuresListView);
        ArrayList<NavigationSectionModel> tempList = new ArrayList<>();
        tempList.add(new NavigationSectionModel("Budget Assistant", R.drawable.ic_budget_manager));
        mainNavigationDrawerAdapter = new NavigationDrawerAdapter(getApplicationContext(),tempList);
        featuresListView.setAdapter(mainNavigationDrawerAdapter);

        //transition drawables
        final TransitionDrawable drawable_zero_one = (TransitionDrawable)getResources().getDrawable(R.drawable.transition_tab_zero_one);
        final TransitionDrawable drawable_one_zero = (TransitionDrawable)getResources().getDrawable(R.drawable.transition_tab_one_zero);
        final TransitionDrawable drawable_one_two = (TransitionDrawable)getResources().getDrawable(R.drawable.transition_tab_one_two);
        final TransitionDrawable drawable_two_one = (TransitionDrawable)getResources().getDrawable(R.drawable.transition_tab_two_one);

        mainActionBarDrawerToggle = new ActionBarDrawerToggle(this,mainDrawerLayout,libs_toolbar,
                R.string.drawer_open, R.string.drawer_close)
        {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);

            }
        };


        mainDrawerLayout.setDrawerListener(mainActionBarDrawerToggle);

        dimBackground = com.nineoldandroids.animation.ObjectAnimator.ofObject(myRootLayout,
                "backgroundColor",
                new com.nineoldandroids.animation.ArgbEvaluator(),
                0x00000000,
                0x70000000);
        dimBackground.setDuration(duration);

        normalBackground = com.nineoldandroids.animation.ObjectAnimator.ofObject(myRootLayout,
                "backgroundColor",
                new com.nineoldandroids.animation.ArgbEvaluator(),
                0x70000000,
                0x00000000);
        normalBackground.setDuration(duration);


        featuresListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                switch (position) {
                    case 0: {
                        mainDrawerLayout.closeDrawer(Gravity.LEFT);
                        Intent budgetIntent = new Intent(MainActivity.this, BudgetActivityNew.class);
                        startActivity(budgetIntent);
                        break;
                    }
                }
            }
        });


        pager_libs.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                tabhost_libs.setSelectedNavigationItem(position);
                //get the current selected Fragment from its ID
                //Fragment tempFrag = getSupportFragmentManager().findFragmentById();
                Fragment tempFrag = adapter_libs.getItem(position);

                /*color animation details
                * Color of the app_bar and the material tab host is changed in an animated fashion when user
                 * use swipes from one tab to the other.
                 * by default the use will be in tab1
                 * tab0 - GREEN
                 * tab1 - RED
                 * tab2 - CYAN*/
                switch (position) {
                    case 0: {
                        //swiped from center tab to the left tab, transition color from blue to red
                        from0to1 = 1;
                        int duration = 1000;
                        ObjectAnimator.ofObject(libs_toolbar, "backgroundColor", new ArgbEvaluator(), tab1Color, tab0Color).setDuration(duration)
                                .start();
                        ObjectAnimator.ofObject(tabhost_libs, "primaryColor", new ArgbEvaluator(), tab1Color, tab0Color).setDuration(duration)
                                .start();
                        getWindow().setBackgroundDrawable(drawable_one_zero);
                        drawable_one_zero.startTransition(450);
                        break;
                    }
                    case 1: {
                        if (from0to1 == 1) {
                            from0to1 = 0;

                            ObjectAnimator.ofObject(libs_toolbar, "backgroundColor", new ArgbEvaluator(), tab0Color, tab1Color).setDuration(duration)
                                    .start();
                            ObjectAnimator.ofObject(tabhost_libs, "primaryColor", new ArgbEvaluator(), tab0Color, tab1Color).setDuration(duration)
                                    .start();
                            getWindow().setBackgroundDrawable(drawable_zero_one);
                            drawable_zero_one.startTransition(450);

                        } else if (from2to1 == 1) {
                            from2to1 = 0;

                            ObjectAnimator.ofObject(libs_toolbar, "backgroundColor", new ArgbEvaluator(), tab2Color, tab1Color).setDuration(duration)
                                    .start();
                            ObjectAnimator.ofObject(tabhost_libs, "primaryColor", new ArgbEvaluator(), tab2Color, tab1Color).setDuration(duration)
                                    .start();
                            getWindow().setBackgroundDrawable(drawable_two_one);
                            drawable_two_one.startTransition(450);
                        }
                        break;
                    }
                    case 2: {
                        from2to1 = 1;

                        ObjectAnimator.ofObject(libs_toolbar, "backgroundColor", new ArgbEvaluator(), tab1Color, tab2Color).setDuration(duration)
                                .start();
                        ObjectAnimator.ofObject(tabhost_libs, "primaryColor", new ArgbEvaluator(), tab1Color, tab2Color).setDuration(duration)
                                .start();
                        getWindow().setBackgroundDrawable(drawable_one_two);
                        drawable_one_two.startTransition(450);
                        break;
                    }
                }

            }
        });

        for(int i=0; i<adapter_libs.getCount();i++)
        {
            tabhost_libs.addTab(tabhost_libs.newTab().setText(adapter_libs.getPageTitle(i))
                    .setTabListener(this));
        }

        pager_libs.setCurrentItem(1);



    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mainActionBarDrawerToggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tabs_activity_library, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if(tabPosition == 0)
        {
            menu.clear();
        }
        if(tabPosition == 1)
        {
            menu.clear();
        }
        if(tabPosition == 2)
        {
            menu.clear();
            SubMenu sm = menu.addSubMenu(0,41,0,"Show..");

            sm.add(0,0,0,"All events").setCheckable(true);
            sm.add(0,1,1,"All Incomes").setCheckable(true);
            sm.add(0,2,2,"All Expenses").setCheckable(true);
            sm.add(0,3,3,"month's events").setCheckable(true);
            sm.add(0,4,4,"week's events").setCheckable(true);
            sm.add(0,5,5,"day's events").setCheckable(true);
            sm.add(0,6,6,"Events within..").setCheckable(true);
            sm.setGroupCheckable(0,true,true);

            for(int i=0; i<subMenuChecked.length;i++)
            {
                if(subMenuChecked[i])
                {
                    sm.getItem(i).setChecked(true);
                }
            }
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == 41)
        {
            //Do Nothing...!!!
        }
        else
        {
            storeMenuSelection(id);
            switch(id)
            {
                case 0:
                {
                    //show all events
                    ArrayList<MoneyObject> filteredList = mainActivityHelperMethods.getAllEvents();
                    EventBus.getDefault().post(filteredList);
                   break;
                }
                case 1:
                {
                    //show only incomes
                    ArrayList<MoneyObject> filteredList = mainActivityHelperMethods.getAllIncomeObjects();
                    EventBus.getDefault().post(filteredList);
                    break;
                }
                case 2:
                {
                    //show only expenditure
                    ArrayList<MoneyObject> filteredList = mainActivityHelperMethods.getAllExpObjects();
                    EventBus.getDefault().post(filteredList);
                    break;
                }
                case 3:
                {
                    //current month's all events
                    ArrayList<MoneyObject> filteredList = mainActivityHelperMethods.getCurrentMonthAllEventObjects(Calendar.getInstance().get(Calendar.MONTH));
                    EventBus.getDefault().post(filteredList);
                    break;
                }
                case 4:
                {
                    //current week's all events
                    break;
                }
                case 5:
                {
                    //current day's events
                    break;
                }
                case 6:
                {
                    //event's within
                    break;
                }
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void storeMenuSelection(int selectedPosition)
    {
        for(int i=0; i<subMenuChecked.length; i++)
        {
            if(i == selectedPosition)
            {
                subMenuChecked[i] = true;
            }
            else
            {
                subMenuChecked[i] = false;
            }
        }
    }

    @Override
    public void onTabSelected(MaterialTab materialTab) {
        pager_libs.setCurrentItem(materialTab.getPosition());

    }

    @Override
    public void onTabReselected(MaterialTab materialTab) {

    }

    @Override
    public void onTabUnselected(MaterialTab materialTab) {

    }

    //communication interface between the EventsFragment and the MainActivity
    @Override
    public void floatingButtonClicked() {
        MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dimBackground.start();
            }
        });
    }

    @Override
    public void floatingButtonClickedAgain() {
        MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                normalBackground.start();
            }
        });
    }

    public void showGraphInDialog(View v,Context c)
    {
        Dialog dialog = new Dialog(c);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.stats_view_dialog);

        ViewGroup viewGroup = (ViewGroup)dialog.findViewById(R.id.graphing_area_in_dialog);
        viewGroup.addView(v);
        dialog.show();
    }


    class MyViewAdapter extends FragmentPagerAdapter {
        String[] tabs;

        MyViewAdapter(FragmentManager fm) {
            super(fm);
            tabs = getResources().getStringArray(R.array.tabs);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment temp_fragment = null;
            if (position == 0) {

                temp_fragment = new StatsFragment();
                updateTabPosition();
                invalidateOptionsMenu();
            }
            if (position == 1) {

                temp_fragment = new EventsFragment();
                updateTabPosition();
                invalidateOptionsMenu();
            }
            if (position == 2) {

                temp_fragment = new LedgerFragment();
                updateTabPosition();
                invalidateOptionsMenu();
            }
            return temp_fragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabs[position];
        }

        @Override
        public int getCount() {
            return 3;
        }
    }

    //Otto bus event subscriptions
    @Subscribe
    public void testSubscription(View v)
    {
        if(v == null)
        {
            Toast.makeText(getApplicationContext(),"View is null",Toast.LENGTH_SHORT).show();
        }
        Dialog statsDialog = new Dialog(MainActivity.this);
        statsDialog.setContentView(R.layout.stats_view_dialog);
        ViewGroup statsViewGroup = (ViewGroup)statsDialog.findViewById(R.id.graphing_area_in_dialog);

        statsViewGroup.addView(v);
        statsDialog.show();

    }

    private void updateTabPosition()
    {
        if(pager_libs.getCurrentItem() == 0)
        {
            tabPosition = 0;
            invalidateOptionsMenu();
        }
        else if(pager_libs.getCurrentItem() == 1)
        {
            tabPosition = 1;
            invalidateOptionsMenu();
        }
        else if(pager_libs.getCurrentItem() == 2)
        {
            tabPosition = 2;
            invalidateOptionsMenu();
        }
    }

    //EventBus  subscription methods
    public void onEvent()
    {

    }



}
