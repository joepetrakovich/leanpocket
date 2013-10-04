package com.appshroom.leanpocket.dto;

/**
 * Created by jpetrakovich on 8/27/13.
 */
public class Change {

    private String fieldName;
    private String oldValue;
    private String newValue;
    private String oldDueDate;
    private String newDueDate;

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getOldValue() {
        return oldValue;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }

    public String getOldDueDate() {
        return oldDueDate;
    }

    public void setOldDueDate(String oldDueDate) {
        this.oldDueDate = oldDueDate;
    }

    public String getNewDueDate() {
        return newDueDate;
    }

    public void setNewDueDate(String newDueDate) {
        this.newDueDate = newDueDate;
    }
}
