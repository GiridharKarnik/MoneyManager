package com.giridharkarnik.moneymanager.modelobjects;

public class FieldBudgetObject {

    private int field_budget_object_id;
    private String budget_name;
    private String budget_field;
    private double field_total_amount;
    private double field_remaining_amount;

    public FieldBudgetObject(int field_budget_object_id, String budget_name, String budget_field, double field_total_amount, double field_remaining_amount) {
        this.field_budget_object_id = field_budget_object_id;
        this.budget_name = budget_name;
        this.budget_field = budget_field;
        this.field_total_amount = field_total_amount;
        this.field_remaining_amount = field_remaining_amount;
    }


    public int getField_budget_object_id() {
        return field_budget_object_id;
    }

    public void setField_budget_object_id(int field_budget_object_id) {
        this.field_budget_object_id = field_budget_object_id;
    }

    public String getBudget_name() {
        return budget_name;
    }

    public void setBudget_name(String budget_name) {
        this.budget_name = budget_name;
    }

    public String getBudget_field() {
        return budget_field;
    }

    public void setBudget_field(String budget_field) {
        this.budget_field = budget_field;
    }

    public double getField_total_amount() {
        return field_total_amount;
    }

    public void setField_total_amount(double field_total_amount) {
        this.field_total_amount = field_total_amount;
    }

    public double getField_remaining_amount() {
        return field_remaining_amount;
    }

    public void setField_remaining_amount(double field_remaining_amount) {
        this.field_remaining_amount = field_remaining_amount;
    }
}
