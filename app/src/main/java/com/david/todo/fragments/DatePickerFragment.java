package com.david.todo.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.david.todo.R;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by David on 2/12/2017.
 */

public class DatePickerFragment extends DialogFragment {
    private DatePicker dpDueDate;

    public DatePickerFragment() {
        //Needs to be empty
    }

    public static DatePickerFragment newInstance(Date date) {
        DatePickerFragment frag = new DatePickerFragment();
        Bundle args = new Bundle();
        args.putSerializable("date", date);
        frag.setArguments(args);
        return frag;
    }

    public interface DatePickerDialogListener {
        void onDateSelected(Date date);
}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_date_picker, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //set title
        getDialog().setTitle(getResources().getText(R.string.date_picker_frag_title));

        //initialize date picker
        dpDueDate = (DatePicker) view.findViewById(R.id.dpDueDate);
        Calendar calendar = Calendar.getInstance();
        Date dueDate = (Date) getArguments().getSerializable("date");
        if (dueDate != null) {
            calendar.setTime(dueDate);
        }
        dpDueDate.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, monthOfYear, dayOfMonth);

                DatePickerDialogListener listener = (DatePickerDialogListener) getTargetFragment();
                listener.onDateSelected(calendar.getTime());
                dismiss();
            }
        });
    }
}
