package com.david.todo.models;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by David on 2/11/2017.
 */
@Table(database = MyDatabase.class)
public class TodoItem extends BaseModel {
    @Column
    @PrimaryKey(autoincrement = true)
    private int id;

    @Column
    private String text;

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

    @Override
    public String toString() {
        return text;
    }
}
