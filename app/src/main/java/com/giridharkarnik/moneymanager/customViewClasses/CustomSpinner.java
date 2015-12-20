package com.giridharkarnik.moneymanager.customViewClasses;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.giridharkarnik.moneymanager.busStand.BusStand;
import com.giridharkarnik.moneymanager.commObjects.SpinnerComm;

public class CustomSpinner extends Spinner
{

    private boolean isOpened = false;
    private ClickCommunicator clickCommunicator;
    private OnItemSelectedListener listener;

    public CustomSpinner(Context context) {
        super(context);
    }

    public CustomSpinner(Context context, int mode) {
        super(context, mode);
    }

    public CustomSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CustomSpinner(Context context, AttributeSet attrs, int defStyleAttr, int mode) {
        super(context, attrs, defStyleAttr, mode);
    }

    public interface ClickCommunicator
    {
        public void isOpened();
        public void isClosed();
    }

    public void setClickCommunicator(ClickCommunicator clickCommunicator)
    {
        this.clickCommunicator = clickCommunicator;

    }

    public void setUpListener(OnItemSelectedListener listener)
    {
        this.listener = listener;
    }

    @Override
    public boolean performClick() {
        //register that the spinner was opened
        isOpened = true;
        BusStand.getInstance().post(new SpinnerComm());
        if(clickCommunicator != null)
        {
            clickCommunicator.isOpened();
        }
        return super.performClick();
    }

    public void performClosedEvent()
    {
       Log.d("bus","Going to close the popUp");
        isOpened = false;
        BusStand.getInstance().post(new SpinnerComm());
        if(clickCommunicator != null)
        {
            clickCommunicator.isClosed();
        }
    }

    public boolean hasBeenOpened()
    {
        return isOpened;
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        Log.d("bus", "window focus has changed");
        super.onWindowFocusChanged(hasWindowFocus);
        if(hasBeenOpened() && hasWindowFocus)
        {
            //closing popup
            performClosedEvent();
        }
    }

}
