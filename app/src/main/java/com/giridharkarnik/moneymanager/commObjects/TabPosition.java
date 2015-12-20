package com.giridharkarnik.moneymanager.commObjects;

/*TabPosition object is used in combination with the EventBus*/
/* TabPosition object is passed from fragments to the MainActivity to indicate which tab is currently shown to the user*/
public class TabPosition {

    int tabPosition;

    public TabPosition(int tabPosition) {
        this.tabPosition = tabPosition;
    }

}
