package com.david.todo.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.david.todo.R;
import com.david.todo.models.TodoItem;
import com.david.todo.utils.StyleUtil;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by David on 2/11/2017.
 */
public class TodoItemsAdapter extends ArrayAdapter<TodoItem> {

    // View lookup cache
    private static class ViewHolder {
        TextView tvText;
        TextView tvDueDate;
        TextView tvPriority;
        CheckBox cbCompleted;
    }

    public TodoItemsAdapter(Context context, List<TodoItem> todoItems) {
        super(context, 0, todoItems);
    }

    /**
     * Translates the model to view for display.
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        TodoItem todoItem = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            // If there's no view to re-use, inflate a brand new view for row
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_todo, parent, false);
            // Lookup view for data population
            viewHolder.tvText = (TextView) convertView.findViewById(R.id.tvText);
            viewHolder.tvDueDate = (TextView) convertView.findViewById(R.id.tvDueDate);
            viewHolder.tvPriority = (TextView) convertView.findViewById(R.id.tvPriority);
            viewHolder.cbCompleted = (CheckBox) convertView.findViewById(R.id.cbCompleted);
            // Cache the viewHolder object inside the fresh view
            convertView.setTag(viewHolder);
        } else {
            // View is being recycled, retrieve the viewHolder object from tag
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Populate the data into the template view using the data object
        viewHolder.tvText.setText(todoItem.getText());
        Date dueDate = todoItem.getDueDate();
        if (dueDate != null) {
            DateFormat df = DateFormat.getDateInstance();
            CharSequence dateStr = getContext().getResources().getString(R.string.due_date_prefix) + " " + df.format(dueDate);
            if (dueDate.before(new Date())) {
                //if past due date, set to high priority color
                dateStr = StyleUtil.applyColor(dateStr, getContext().getResources().getColor(R.color.highPriority));
            }
            viewHolder.tvDueDate.setText(dateStr);
        } else {
            viewHolder.tvDueDate.setText("");
        }
        CharSequence priorityTxt = todoItem.getPriorityStr(getContext().getResources());
        switch (todoItem.getPriority()) {
            case HIGH:
                priorityTxt = StyleUtil.applyColor(priorityTxt, getContext().getResources().getColor(R.color.highPriority));
                break;
            case MEDIUM:
                priorityTxt = StyleUtil.applyColor(priorityTxt, getContext().getResources().getColor(R.color.medPriority));
                break;
            case LOW:
                priorityTxt = StyleUtil.applyColor(priorityTxt, getContext().getResources().getColor(R.color.lowPriority));
                break;
        }
        viewHolder.tvPriority.setText(priorityTxt);

        //set tags for the checkbox so it can access them in event handler
        //note: need to set these tags before calling setChecked on the checkbox, because the onCheckedChanged event would fire!
        viewHolder.cbCompleted.setTag(R.id.tvText, viewHolder.tvText);
        viewHolder.cbCompleted.setTag(R.id.tvDueDate, viewHolder.tvDueDate);
        viewHolder.cbCompleted.setTag(R.id.tvPriority, viewHolder.tvPriority);
        viewHolder.cbCompleted.setTag(position);

        //set strike through and checked
        if (todoItem.isCompleted()) {
            viewHolder.tvText.setPaintFlags(viewHolder.tvText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            viewHolder.tvDueDate.setPaintFlags(viewHolder.tvDueDate.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            viewHolder.tvPriority.setPaintFlags(viewHolder.tvPriority.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            viewHolder.cbCompleted.setChecked(true);
        } else {
            viewHolder.tvText.setPaintFlags(viewHolder.tvText.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
            viewHolder.tvDueDate.setPaintFlags(viewHolder.tvDueDate.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
            viewHolder.tvPriority.setPaintFlags(viewHolder.tvPriority.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
            viewHolder.cbCompleted.setChecked(false);
        }

        //attach click handler for checkbox
        viewHolder.cbCompleted.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                TextView tvText = (TextView) buttonView.getTag(R.id.tvText);
                TextView tvDueDate = (TextView) buttonView.getTag(R.id.tvDueDate);
                TextView tvPriority = (TextView) buttonView.getTag(R.id.tvPriority);
                int position = (Integer) buttonView.getTag();
                TodoItem todoItem = getItem(position);
                if (isChecked && !todoItem.isCompleted()) {
                    tvText.setPaintFlags(tvText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    tvDueDate.setPaintFlags(tvDueDate.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    tvPriority.setPaintFlags(tvPriority.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    todoItem.setCompleted(true);
                    todoItem.save();
                } else if (!isChecked && todoItem.isCompleted()){
                    tvText.setPaintFlags(tvText.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
                    tvDueDate.setPaintFlags(tvDueDate.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
                    tvPriority.setPaintFlags(tvPriority.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
                    todoItem.setCompleted(false);
                    todoItem.save();
                }
            }
        });

        // Return the completed view to render on screen
        return convertView;
    }


}
