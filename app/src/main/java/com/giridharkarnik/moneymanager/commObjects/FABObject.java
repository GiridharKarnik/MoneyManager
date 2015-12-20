package com.giridharkarnik.moneymanager.commObjects;


public class FABObject {

    private boolean open;

    public FABObject(boolean open) {
        this.setOpen(open);
    }


    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }
}
