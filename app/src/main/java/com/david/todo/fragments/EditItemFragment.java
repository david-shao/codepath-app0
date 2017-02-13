package com.david.todo.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.david.todo.R;
import com.david.todo.models.TodoItem;
import com.david.todo.utils.StyleUtil;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by David on 2/12/2017.
 */

public class EditItemFragment extends DialogFragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private int position;
    private TodoItem todoItem;
    private EditText etEditItem;
    private DatePicker dpDueDate;
    private Spinner spPriority;

    public EditItemFragment() {
        //Needs to be empty
    }

    public static EditItemFragment newInstance(int position, TodoItem todoItem) {
        EditItemFragment frag = new EditItemFragment();
        Bundle args = new Bundle();
        args.putInt("pos", position);
        args.putSerializable("todoItem", todoItem);
        frag.setArguments(args);
        return frag;
    }

    public interface EditItemDialogListener {
        void onSave(int position, TodoItem todoItem);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_item, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //setup callback for save button
        Button btnSave = (Button) view.findViewById(R.id.btnSaveItem);
        btnSave.setOnClickListener(this);

        //set title
        getDialog().setTitle(getResources().getText(R.string.edit_item_frag_title));

        todoItem = (TodoItem) getArguments().getSerializable("todoItem");
        position = getArguments().getInt("pos");
        etEditItem = (EditText) view.findViewById(R.id.etEditItem);
        etEditItem.setText(todoItem.getText());

        //initialize date picker
        dpDueDate = (DatePicker) view.findViewById(R.id.dpDueDate);
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

        //initialize spinner
        spPriority = (Spinner) view.findViewById(R.id.spPriority);
        CharSequence[] priorities = new CharSequence[] {
                StyleUtil.applyColor(getResources().getText(R.string.priority_high), getResources().getColor(R.color.highPriority)),
                StyleUtil.applyColor(getResources().getText(R.string.priority_medium), getResources().getColor(R.color.medPriority)),
                StyleUtil.applyColor(getResources().getText(R.string.priority_low), getResources().getColor(R.color.lowPriority)),
        };
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(getContext(), android.R.layout.simple_spinner_dropdown_item, priorities);
        spPriority.setAdapter(adapter);
        spPriority.setSelection(todoItem.getPriority().getValue());
        spPriority.setOnItemSelectedListener(this);

        //set cursor to end
        etEditItem.setSelection(todoItem.getText().length());

        //show keyboard
//        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    /**
     * Handler for save button.
     * @param view
     */
    @Override
    public void onClick(View view) {
        String text = etEditItem.getText().toString();
        todoItem.setText(text);

        //return modified todoItem back to activity through implemented listener
        EditItemDialogListener listener = (EditItemDialogListener) getActivity();
        listener.onSave(position, todoItem);
        //close dialog fragment
        dismiss();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
        switch (position) {
            case 0:
                todoItem.setPriority(TodoItem.Priority.HIGH);
                break;
            case 1:
                todoItem.setPriority(TodoItem.Priority.MEDIUM);
                break;
            case 2:
                todoItem.setPriority(TodoItem.Priority.LOW);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //do nothing
    }
}
