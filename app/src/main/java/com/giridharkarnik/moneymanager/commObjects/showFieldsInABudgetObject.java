package com.giridharkarnik.moneymanager.commObjects;


public class showFieldsInABudgetObject {

    private String budgetName;
    private int budgetId;
    private long budgetStartDate;
    private long budgetEndDate;

    public showFieldsInABudgetObject(String budgetName, int budgetId, long budgetStartDate, long budgetEndDate) {
        this.budgetName = budgetName;
        this.budgetId = budgetId;
        this.setBudgetStartDate(budgetStartDate);
        this.setBudgetEndDate(budgetEndDate);
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

    public long getBudgetStartDate() {
        return budgetStartDate;
    }

    public void setBudgetStartDate(long budgetStartDate) {
        this.budgetStartDate = budgetStartDate;
    }

    public long getBudgetEndDate() {
        return budgetEndDate;
    }

    public void setBudgetEndDate(long budgetEndDate) {
        this.budgetEndDate = budgetEndDate;
    }
}
