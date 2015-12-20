package com.giridharkarnik.moneymanager.commObjects;


public class RefreshObject {

    private boolean refreshIt;

    public RefreshObject(boolean refreshIt) {
        this.setRefreshIt(refreshIt);
    }

    public boolean isRefreshIt() {
        return refreshIt;
    }

    public void setRefreshIt(boolean refreshIt) {
        this.refreshIt = refreshIt;
    }
}
