package com.giridharkarnik.moneymanager.commObjects;

/*This is a EventBus communication object, which is used by the MainActivity settings to convey various
* ArrayList of events as per the user filter*/

import com.giridharkarnik.moneymanager.modelobjects.MoneyObject;

import java.util.ArrayList;

public class LedgerArrayListComm {

    ArrayList<MoneyObject> filteredList;

    public LedgerArrayListComm(ArrayList<MoneyObject> filteredList) {
        this.filteredList = filteredList;
    }
}
