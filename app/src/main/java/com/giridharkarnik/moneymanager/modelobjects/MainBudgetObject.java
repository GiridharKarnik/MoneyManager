package com.giridharkarnik.moneymanager.modelobjects;

public class MainBudgetObject {

    private int budgetId;
    private String budgetName;
    private double totalAmount;
    private double remainingAmount;
    private long dateFrom;
    private long dateTill;

    public MainBudgetObject(int budgetId, String budgetName, double totalAmount, double remainingAmount, long dateFrom, long dateTill) {
        this.budgetId = budgetId;
        this.budgetName = budgetName;
        this.totalAmount = totalAmount;
        this.remainingAmount = remainingAmount;
        this.dateFrom = dateFrom;
        this.dateTill = dateTill;
    }


    public String getBudgetName() {
        return budgetName;
    }

    public void setBudgetName(String budgetName) {
        this.budgetName = budgetName;
    }

    public int getBudgetId() {
        return budgetId;
    }

    public void setBudgetId(int budgetId) {
        this.budgetId = budgetId;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public double getRemainingAmount() {
        return remainingAmount;
    }

    public void setRemainingAmount(double remainingAmount) {
        this.remainingAmount = remainingAmount;
    }

    public long getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(long dateFrom) {
        this.dateFrom = dateFrom;
    }

    public long getDateTill() {
        return dateTill;
    }

    public void setDateTill(long dateTill) {
        this.dateTill = dateTill;
    }
}
