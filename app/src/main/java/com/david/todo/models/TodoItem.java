package com.david.todo.models;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ColumnIgnore;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by David on 2/11/2017.
 */
@Table(database = MyDatabase.class)
public class TodoItem extends BaseModel implements Serializable {
    @Column
    @PrimaryKey(autoincrement = true)
    private int id;

    @Column
    private String text;

    @Column
    private Date dueDate;

    /**
     * Getters and Setters
     */
    public int getId() {
        return id;
    }
    public void setId(int val) {
        id = val;
    }
    public String getText() {
        return text;
    }
    public void setText(String str) {
        text = str;
    }
    public Date getDueDate() {
        return dueDate;
    }
    public void setDueDate(Date date) {
        dueDate = date;
    }

    @Override
    public String toString() {
        return text;
    }
}
