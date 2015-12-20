package com.giridharkarnik.moneymanager.modelobjects;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.giridharkarnik.moneymanager.R;

public class FullStatsActivity extends ActionBarActivity {

    ViewGroup fullGraphingView;
    public static View showThis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_stats);

        Log.d("tar", "onCreate method is called");
        if(showThis == null)
        {
            Log.d("tar","showThis is null");
        }
        fullGraphingView = (ViewGroup)findViewById(R.id.fullGraphingView);
        fullGraphingView.removeAllViews();
        showThis.setLayoutParams(new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT
        ));
        fullGraphingView.addView(showThis);
        showThis.bringToFront();

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("tar", "onPause method is called");
        fullGraphingView.removeAllViews();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("tar", "onDestroy method is called");
        fullGraphingView.removeAllViews();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("tar", "onStop method is called");
        fullGraphingView.removeAllViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("tar", "onResume method is called");
        fullGraphingView.removeAllViews();
        showThis.setLayoutParams(new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT
        ));
        fullGraphingView.addView(showThis);
        showThis.bringToFront();
    }
}
