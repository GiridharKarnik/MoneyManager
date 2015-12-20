package com.giridharkarnik.moneymanager.HelperMethodsClasses;

import android.content.Context;

import com.giridharkarnik.moneymanager.modelobjects.MoneyObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class LedgerShowAndSortingMethods {

    private Context ledgerSortingMethodsContext;
    private ArrayList<MoneyObject> moneyObjectList;
    HelperMethods ledgerSortingHelperMethods;

    public LedgerShowAndSortingMethods(Context ledgerSortingMethodsContext) {
        this.ledgerSortingMethodsContext = ledgerSortingMethodsContext;
        this.ledgerSortingHelperMethods = new HelperMethods(ledgerSortingMethodsContext);
    }

    /*1. Show expenditure events only
2. Show income events only
3. Show events of current month only
4. Show events of current week only
5. Show events of current day only
6. Show events of a field only
7. Show events of a budget only
8. Show events within a calendar period only*/

    public ArrayList<MoneyObject> sortInNearestEventFirst()
    {
        ArrayList<MoneyObject> tempList = ledgerSortingHelperMethods.getAllEvents();
        Comparator<MoneyObject> compareDateNF = new Comparator<MoneyObject>() {
            @Override
            public int compare(MoneyObject moneyObject1, MoneyObject moneyObject2)
            {
                if(moneyObject1.getDate_int() > moneyObject2.getDate_int())
                {
                    return 1;
                }
                if(moneyObject2.getDate_int()>moneyObject1.getDate_int())
                {
                    return -1;
                }
                else
                {
                    return 0;
                }
            }
        };
        Collections.sort(tempList,compareDateNF);
        return tempList;
    }

    public ArrayList<MoneyObject> sortInFarthestEventsFirst()
    {
        ArrayList<MoneyObject> tempList = ledgerSortingHelperMethods.getAllEvents();
        Comparator<MoneyObject> compareDateNF = new Comparator<MoneyObject>() {
            @Override
            public int compare(MoneyObject moneyObject1, MoneyObject moneyObject2)
            {
                if(moneyObject1.getDate_int() > moneyObject2.getDate_int())
                {
                    return -1;
                }
                if(moneyObject2.getDate_int()>moneyObject1.getDate_int())
                {
                    return 1;
                }
                else
                {
                    return 0;
                }
            }
        };
        Collections.sort(tempList,compareDateNF);
        return tempList;
    }

}
