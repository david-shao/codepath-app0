package com.david.todo.models;

import android.content.res.Resources;

import com.david.todo.R;
import com.raizlabs.android.dbflow.annotation.Column;
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
    public enum Priority {
        HIGH(0),
        MEDIUM(1),
        LOW(2);

        private final int value;
        private Priority(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    @Column
    @PrimaryKey(autoincrement = true)
    private int id;

    @Column
    private String text;

    @Column
    private Date dueDate;

    @Column
    private Priority priority;

    @Column
    private boolean completed;

    @Column
    private Date created;

    /**
     * Constructor.
     */
    public TodoItem() {
        priority = Priority.MEDIUM;
        created = new Date();
    }

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
    public Priority getPriority() {
        return priority;
    }
    public void setPriority(Priority priority) {
        this.priority = priority;
    }
    public boolean isCompleted() {
        return completed;
    }
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
    public Date getCreated() {
        return created;
    }
    public void setCreated(Date created) {
        //intentionally left empty
    }

    @Override
    public String toString() {
        return text;
    }

    public CharSequence getPriorityStr(Resources resources) {
        switch (priority) {
            case HIGH:
                return resources.getText(R.string.priority_high);
            case MEDIUM:
                return resources.getText(R.string.priority_medium);
            case LOW:
            default:
                return resources.getText(R.string.priority_low);
        }
    }
}
