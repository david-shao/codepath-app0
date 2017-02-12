package com.david.todo.models;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Created by David on 2/11/2017.
 */
@Database(name = MyDatabase.NAME, version = MyDatabase.VERSION)
public class MyDatabase {
    public static final String NAME = "todo_db";

    public static final int VERSION = 1;
}
