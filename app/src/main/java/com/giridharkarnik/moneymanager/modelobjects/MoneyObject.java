package com.giridharkarnik.moneymanager.modelobjects;

/**
 * Created by Giridhar on 1/4/2015.
 */
public class MoneyObject
{
    private double amount;
    private int amount_id;
    private long date_int;
    private String type;
    private String title;

    public MoneyObject(int amount_id,double amount,long date_int, String type, String title)
    {
        this.amount_id = amount_id;
        this.amount = amount;
        this.setDate_int(date_int);
        this.type = type;
        this.title = title;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getAmount_id() {
        return amount_id;
    }

    public void setAmount_id(int amount_id) {
        this.amount_id = amount_id;
    }

    public long getDate_int() {
        return date_int;
    }

    public void setDate_int(long date_int) {
        this.date_int = date_int;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
