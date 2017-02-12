package com.david.todo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;

import com.david.todo.models.TodoItem;

import java.util.Calendar;
import java.util.Date;

/**
 * Screen for editing an item.
 */
public class EditItemActivity extends AppCompatActivity {

    private int position;
    private TodoItem todoItem;
    private EditText etEditItem;
    private DatePicker dpDueDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        todoItem = (TodoItem) getIntent().getSerializableExtra("todoItem");
        position = getIntent().getIntExtra("pos", 0);
        etEditItem = (EditText) findViewById(R.id.etEditItem);
        etEditItem.setText(todoItem.getText());

        //initialize date picker
        dpDueDate = (DatePicker) findViewById(R.id.dpDueDate);
        Calendar calendar = Calendar.getInstance();
        Date dueDate = todoItem.getDueDate();
        if (dueDate != null) {
            calendar.setTime(dueDate);
        }
        dpDueDate.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, monthOfYear, dayOfMonth);
                todoItem.setDueDate(calendar.getTime());
            }
        });

        //set cursor to end
        etEditItem.setSelection(todoItem.getText().length());

        //show keyboard
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    /**
     * Handler for save button.
     * @param view
     */
    public void onSave(View view) {
        String text = etEditItem.getText().toString();
        todoItem.setText(text);
        Intent data = new Intent();
        data.putExtra("todoItem", todoItem);
        data.putExtra("pos", position);
        setResult(RESULT_OK, data);
        finish();
    }

}
