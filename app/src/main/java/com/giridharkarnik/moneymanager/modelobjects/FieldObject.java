package com.giridharkarnik.moneymanager.modelobjects;

public class FieldObject {

    private int field_id;
    private String field_type;
    private String field_title;
    private int icon_number;

    public FieldObject(int field_id, String field_type, String field_title, int icon_number) {
        this.field_id = field_id;
        this.field_type = field_type;
        this.field_title = field_title;
        this.icon_number = icon_number;
    }


    public int getField_id() {
        return field_id;
    }

    public void setField_id(int field_id) {
        this.field_id = field_id;
    }

    public String getField_type() {
        return field_type;
    }

    public void setField_type(String field_type) {
        this.field_type = field_type;
    }

    public String getField_title() {
        return field_title;
    }

    public void setField_title(String field_title) {
        this.field_title = field_title;
    }

    public int getIcon_number() {
        return icon_number;
    }

    public void setIcon_number(int icon_number) {
        this.icon_number = icon_number;
    }
}
